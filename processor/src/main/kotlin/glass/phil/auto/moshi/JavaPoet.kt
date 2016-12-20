package glass.phil.auto.moshi

import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeSpec

fun TypeSpec.Builder.addMethods(vararg methods: MethodSpec) = apply { methods.forEach { addMethod(it) } }

fun MethodSpec.Builder.addParameters(vararg params: ParameterSpec) = apply { params.forEach { addParameter(it) } }

inline fun MethodSpec.Builder.controlFlow(controlFlow: String, vararg args: Any, func: MethodSpec.Builder.() -> Unit) {
  beginControlFlow(controlFlow, *args)
  func(this)
  endControlFlow()
}
