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
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier.ABSTRACT
import javax.lang.model.element.Modifier.FINAL
import javax.lang.model.element.Modifier.PRIVATE
import javax.lang.model.element.Modifier.PUBLIC
import javax.lang.model.element.Modifier.STATIC
import javax.lang.model.element.Name
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.ExecutableType
import javax.lang.model.type.TypeKind.DECLARED
import javax.lang.model.type.TypeMirror

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

    val constructor = MethodSpec.constructorBuilder()
        .addParameters(properties.map { ParameterSpec.builder(TypeName.get(it.type), it.name).build() })
        .addStatement("super(\$L)", properties.map { it.name }.joinToString())
        .build()

    val type = TypeSpec.classBuilder(className)
        .superclass(ClassName.get(context.packageName(), classToExtend))
        .addModifiers(if (isFinal) FINAL else ABSTRACT)
        .addMethod(constructor)
        .addType(jsonAdapter(context, properties))
        .build()

    return JavaFile.builder(context.packageName(), type).build().toString()
  }

  private fun jsonAdapter(context: Context, properties: List<Property>): TypeSpec {
    val autoValueClass = ClassName.get(context.autoValueClass())

    val adapters: Map<Property, FieldSpec> = properties.associateBy({ it }, {
      val boxedType = TypeName.get(it.type).let { if (it.isPrimitive) it.box() else it }
      val fieldType = ParameterizedTypeName.get(ClassName.get(JsonAdapter::class.java), boxedType)
      FieldSpec.builder(fieldType, "${it.name}Adapter", PRIVATE, FINAL).build()
    })

    fun resolve(type: TypeMirror): CodeBlock {
      return if (type.kind == DECLARED && (type as DeclaredType).typeArguments.isNotEmpty()) {
        val rawType = context.types.erasure(type)
        CodeBlock.builder().apply {
          add("\$T.newParameterizedType(\$T.class", Types::class.java, rawType)
          type.typeArguments.forEach { add(", \$L", resolve(it)) }
          add(")")
        }.build()
      } else {
        CodeBlock.of("\$T.class", type)
      }
    }

    fun constructor(): MethodSpec {
      val moshi = ParameterSpec.builder(Moshi::class.java, "moshi").build()
      val builder = MethodSpec.constructorBuilder().addParameter(moshi)
      for ((property, adapter) in adapters) {
        builder.addStatement("\$N = \$N.adapter(\$L)\$L", adapter, moshi, resolve(property.type),
            if (property.nullable) ".nullSafe()" else "")
      }
      return builder.build()
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
        addStatement("return new AutoValue_\$L(\$L)", context.autoValueClass().simpleName,
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

    return TypeSpec.classBuilder("AutoMoshi_JsonAdapter")
        .addModifiers(STATIC, FINAL)
        .superclass(ParameterizedTypeName.get(ClassName.get(JsonAdapter::class.java), autoValueClass))
        .addFields(adapters.values)
        .addMethods(constructor(), fromJson(), toJson())
        .build()
  }
}