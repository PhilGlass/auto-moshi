package glass.phil.auto.moshi

import com.google.auto.common.MoreElements.isAnnotationPresent
import com.google.auto.common.Visibility
import com.google.auto.common.Visibility.effectiveVisibilityOfElement
import com.google.auto.service.AutoService
import com.google.auto.value.AutoValue
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import com.squareup.javapoet.WildcardTypeName
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.Modifier.ABSTRACT
import javax.lang.model.element.Modifier.FINAL
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic.Kind.ERROR

private val FACTORY_ANNOTATION = "@AutoMoshi.Factory"
private val UNBOUNDED_CLASS = parameterizedTypeName(Class::class.java, WildcardTypeName.subtypeOf(TypeName.OBJECT))

@AutoService(Processor::class)
class AutoMoshiFactoryProcessor : AbstractProcessor() {
  override fun getSupportedSourceVersion() = SourceVersion.latestSupported()!!

  override fun getSupportedAnnotationTypes() = setOf(AutoMoshi::class.java.canonicalName,
      AutoMoshi.Factory::class.java.canonicalName)

  override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
    val autoMoshiTypes = roundEnv.getElementsAnnotatedWith(AutoValue::class.java)
        .filter { isAnnotationPresent(it, AutoMoshi::class.java) }
        .map { it as TypeElement }

    roundEnv.getElementsAnnotatedWith(AutoMoshi.Factory::class.java).map { it as TypeElement }.forEach {
      if (!it.modifiers.contains(ABSTRACT)) {
        return error("$FACTORY_ANNOTATION annotated classes must be abstract", it)
      }
      if (!isSubtype(it, JsonAdapter.Factory::class.java)) {
        return error("$FACTORY_ANNOTATION annotated classes must implement JsonAdapter.Factory", it)
      }
      if (effectiveVisibilityOfElement(it) < Visibility.DEFAULT) {
        return error("$FACTORY_ANNOTATION annotated classes must have at least package visibility", it)
      }
      if (!it.isEffectivelyStatic()) {
        return error("Nested $FACTORY_ANNOTATION annotated classes must be static", it)
      }
      try {
        JavaFile.builder(it.packageName, adapterFactory(it, autoMoshiTypes)).build().writeTo(processingEnv.filer)
      } catch (exception: IOException) {
        return error("Unable to write generated class: $exception", it)
      }
    }
    return false
  }

  private fun isSubtype(type: TypeElement, supertype: Class<*>): Boolean {
    val expectedSupertype = processingEnv.elementUtils.getTypeElement(supertype.canonicalName).asType()
    return processingEnv.typeUtils.isSubtype(type.asType(), expectedSupertype)
  }

  private fun adapterFactory(factoryClass: TypeElement, autoMoshiTypes: List<TypeElement>): TypeSpec {
    fun create(): MethodSpec {
      fun sameClassClause(candidateType: TypeElement, classObject: Any): CodeBlock {
        if (candidateType.isVisibleTo(factoryClass)) {
          return CodeBlock.of("\$T.class.equals(\$N)", ClassName.get(candidateType), classObject)
        }
        return CodeBlock.of("\$S.equals(\$N.getCanonicalName())", candidateType.qualifiedName, classObject)
      }

      fun adapterType(autoMoshiType: TypeElement) = ClassName.get(autoMoshiType.packageName, adapterName(autoMoshiType))

      val (simple, generic) = autoMoshiTypes.sortedByDescending { it.isVisibleTo(factoryClass) }
          .partition { it.typeParameters.isEmpty() }

      val type = ParameterSpec.builder(Type::class.java, "type").build()
      val annotations = ParameterSpec.builder(parameterizedTypeName(Set::class.java,
          WildcardTypeName.subtypeOf(Annotation::class.java)), "annotations").build()
      val moshi = ParameterSpec.builder(Moshi::class.java, "moshi").build()
      val builder = MethodSpec.methodBuilder("create")
          .addAnnotation(Override::class.java)
          .addModifiers(Modifier.PUBLIC)
          .returns(parameterizedTypeName(JsonAdapter::class.java, WildcardTypeName.subtypeOf(TypeName.OBJECT)))
          .addParameters(type, annotations, moshi)

      return builder.apply {
        if (simple.isNotEmpty()) {
          controlFlow("if (\$N instanceof \$T)", type, Class::class.java) {
            addStatement("final \$1T clazz = (\$1T) \$2N", UNBOUNDED_CLASS, type)
            simple.forEach {
              controlFlow("if (\$L)", sameClassClause(it, "clazz")) {
                addStatement("return new \$T(\$N)", adapterType(it), moshi)
              }
            }
          }
        }
        if (generic.isNotEmpty()) {
          controlFlow("if (\$N instanceof \$T)", type, ParameterizedType::class.java) {
            addStatement("final \$1T rawType = (\$1T) ((\$2T) \$3N).getRawType()", UNBOUNDED_CLASS,
                ParameterizedType::class.java, type)
            addStatement("final \$T typeArgs = ((\$T) \$N).getActualTypeArguments()", Array<Type>::class.java,
                ParameterizedType::class.java, type)
            generic.forEach {
              controlFlow("if (\$L)", sameClassClause(it, "rawType")) {
                addStatement("return new \$T<>(\$N, typeArgs)", adapterType(it), moshi)
              }
            }
          }
        }
        addStatement("return null")
      }.build()
    }

    return TypeSpec.classBuilder("AutoMoshi_${factoryClass.generatedName()}")
        .superclass(TypeName.get(factoryClass.asType()))
        .addModifiers(FINAL)
        .addMethod(create())
        .build()
  }

  private fun error(message: String, element: Element): Boolean {
    processingEnv.messager.printMessage(ERROR, message, element)
    return false
  }
}
