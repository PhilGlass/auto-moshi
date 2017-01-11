package glass.phil.auto.moshi

import com.google.auto.common.MoreElements.isAnnotationPresent
import com.google.auto.service.AutoService
import com.google.auto.value.extension.AutoValueExtension
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.io.IOException
import java.lang.reflect.Type
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier.ABSTRACT
import javax.lang.model.element.Modifier.FINAL
import javax.lang.model.element.Modifier.PRIVATE
import javax.lang.model.element.Modifier.PUBLIC
import javax.lang.model.element.Name
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.ExecutableType
import javax.lang.model.type.TypeKind.TYPEVAR
import javax.lang.model.type.TypeMirror
import javax.lang.model.type.TypeVariable
import javax.tools.Diagnostic.Kind.ERROR

/** Generated adapter names are prefixed with a $, to reduce autocomplete noise */
fun adapterName(autoValueClass: TypeElement) = "\$AutoMoshi_${autoValueClass.generatedName()}_JsonAdapter"

@AutoService(AutoValueExtension::class)
class AutoMoshiExtension : AutoValueExtension() {
  private data class Property(
      val name: String,
      val methodName: Name,
      val type: TypeMirror,
      val serializedName: String,
      val nullable: Boolean
  )

  override fun applicable(context: Context) = isAnnotationPresent(context.autoValueClass(), AutoMoshi::class.java)

  override fun generateClass(context: Context, className: String, classToExtend: String, isFinal: Boolean): String {
    fun toProperty(name: String, method: ExecutableElement): Property {
      val resolvedMethod = context.types.asMemberOf(context.autoValueClass().asType() as DeclaredType, method)
      val resolvedType = (resolvedMethod as ExecutableType).returnType
      val serializedName = method.getAnnotation(Json::class.java)?.name ?: name
      return Property(name = name, methodName = method.simpleName, type = resolvedType,
          serializedName = serializedName, nullable = method.hasAnnotation("Nullable"))
    }

    val properties = context.properties().map { toProperty(it.key, it.value) }
    val adapter = jsonAdapter(context, className, properties)
    try {
      JavaFile.builder(context.packageName(), adapter).build().writeTo(context.filer)
    } catch (exception: IOException) {
      context.messager.printMessage(ERROR, "Unable to write generated class: $exception", context.autoValueClass())
    }

    // As of AutoValue 1.3, generating a valid subclass in generateClass() is mandatory.
    // Generating the adapter class in applicable() and skipping generateClass() entirely is unfortunately not
    // possible, because applicable() is called before methods like consumeMethods() and consumeProperties(). This
    // means that the set of properties it is passed may not match the finalised set passed to generateClass().
    val constructor = MethodSpec.constructorBuilder()
        .addParameters(properties.map { ParameterSpec.builder(TypeName.get(it.type), it.name).build() })
        .addStatement("super(\$L)", properties.map { it.name }.joinToString())
        .build()

    val type = TypeSpec.classBuilder(className)
        .superclass(typeName(ClassName.get(context.packageName(), classToExtend), context.typeVariables))
        .addTypeVariables(context.typeVariables)
        .addModifiers(if (isFinal) FINAL else ABSTRACT)
        .addMethod(constructor)
        .build()

    return JavaFile.builder(context.packageName(), type).build().toString()
  }

  private fun jsonAdapter(context: Context, className: String, properties: List<Property>): TypeSpec {
    val autoValueClass = TypeName.get(context.autoValueClass().asType())

    val adapters: Map<Property, FieldSpec> = properties.associateBy({ it }, {
      val boxedType = TypeName.get(it.type).let { if (it.isPrimitive) it.box() else it }
      val fieldType = ParameterizedTypeName.get(ClassName.get(JsonAdapter::class.java), boxedType)
      FieldSpec.builder(fieldType, "${it.name}Adapter", PRIVATE, FINAL).build()
    })

    fun constructor(): MethodSpec {
      val moshi = ParameterSpec.builder(Moshi::class.java, "moshi").build()
      val typeArgs = ParameterSpec.builder(Array<Type>::class.java, "typeArgs").build()

      fun resolve(type: TypeMirror): CodeBlock {
        return if (type.parameterized) {
          val rawType = context.types.erasure(type)
          CodeBlock.builder().apply {
            add("\$T.newParameterizedType(\$T.class", Types::class.java, rawType)
            (type as DeclaredType).typeArguments.forEach { add(", \$L", resolve(it)) }
            add(")")
          }.build()
        } else if (type.kind == TYPEVAR) {
          val index = context.autoValueClass().typeParameters.indexOf((type as TypeVariable).asElement())
          CodeBlock.of("\$N[$index]", typeArgs)
        } else {
          CodeBlock.of("\$T.class", type)
        }
      }

      return MethodSpec.constructorBuilder().addModifiers(PUBLIC).addParameter(moshi).apply {
        if (context.generic) {
          addParameter(typeArgs)
        }
        for ((property, adapter) in adapters) {
          if (property.nullable) {
            // When we aren't assigning the adapter directly to a field (because we're calling nullSafe()), javac
            // is only able to infer the correct type for the adapter(Class<T>) overload. A type witness is required
            // when using the adapter(Type) overload.
            val witness = if (property.type.parameterized || property.type.kind == TYPEVAR) {
              CodeBlock.of("<\$T>", property.type)
            } else {
              CodeBlock.of("")
            }
            addStatement("\$N = \$N.\$Ladapter(\$L).nullSafe()", adapter, moshi, witness, resolve(property.type))
          } else {
            addStatement("\$N = \$N.adapter(\$L)", adapter, moshi, resolve(property.type))
          }
        }
      }.build()
    }

    fun fromJson(): MethodSpec {
      val reader = ParameterSpec.builder(JsonReader::class.java, "reader").build()
      val builder = MethodSpec.methodBuilder("fromJson")
          .addAnnotation(Override::class.java)
          .addModifiers(PUBLIC)
          .returns(autoValueClass)
          .addParameter(reader)
          .addException(IOException::class.java)

      return builder.apply {
        properties.forEach {
          addStatement("\$T \$N = \$L", it.type, it.name, it.type.defaultValue)
        }
        addStatement("\$N.beginObject()", reader)
        controlFlow("while (\$N.hasNext())", reader) {
          addStatement("final \$T name = \$N.nextName()", String::class.java, reader)
          controlFlow("switch (name)") {
            for ((property, adapter) in adapters) {
              controlFlow("case \$S:", property.serializedName) {
                addStatement("\$N = \$N.fromJson(\$N)", property.name, adapter, reader)
                addStatement("break")
              }
            }
            controlFlow("default:") {
              addStatement("\$N.skipValue()", reader)
            }
          }
        }
        addStatement("\$N.endObject()", reader)
        addStatement("return new \$L\$L(\$L)", className.removeAll('$'), if (context.generic) "<>" else "",
            properties.map { it.name }.joinToString())
      }.build()
    }

    fun toJson(): MethodSpec {
      val writer = ParameterSpec.builder(JsonWriter::class.java, "writer").build()
      val value = ParameterSpec.builder(autoValueClass, "value").build()
      val builder = MethodSpec.methodBuilder("toJson")
          .addAnnotation(Override::class.java)
          .addModifiers(PUBLIC)
          .addParameters(writer, value)
          .addException(IOException::class.java)

      return builder.apply {
        addStatement("\$N.beginObject()", writer)
        for ((property, adapter) in adapters) {
          addStatement("\$N.name(\$S)", writer, property.serializedName)
          addStatement("\$N.toJson(\$N, \$N.\$L())", adapter, writer, value, property.methodName)
        }
        addStatement("\$N.endObject()", writer)
      }.build()
    }

    return TypeSpec.classBuilder(adapterName(context.autoValueClass()))
        .superclass(ParameterizedTypeName.get(ClassName.get(JsonAdapter::class.java), autoValueClass))
        .addTypeVariables(context.typeVariables)
        .addModifiers(PUBLIC, FINAL)
        .addFields(adapters.values)
        .addMethods(constructor(), fromJson(), toJson())
        .build()
  }
}
