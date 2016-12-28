package glass.phil.auto.moshi

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import com.squareup.javapoet.TypeVariableName

fun typeName(rawType: ClassName, typeVariables: Collection<TypeVariableName>): TypeName =
    if (typeVariables.isEmpty()) rawType else ParameterizedTypeName.get(rawType, *typeVariables.toTypedArray())

fun TypeSpec.Builder.addMethods(vararg methods: MethodSpec) = apply { methods.forEach { addMethod(it) } }

fun MethodSpec.Builder.addParameters(vararg params: ParameterSpec) = apply { params.forEach { addParameter(it) } }

inline fun MethodSpec.Builder.controlFlow(controlFlow: String, vararg args: Any, func: MethodSpec.Builder.() -> Unit) {
  beginControlFlow(controlFlow, *args)
  func(this)
  endControlFlow()
}
