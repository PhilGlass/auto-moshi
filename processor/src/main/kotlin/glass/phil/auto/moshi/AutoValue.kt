package glass.phil.auto.moshi

import com.google.auto.value.extension.AutoValueExtension.Context
import com.squareup.javapoet.TypeVariableName
import javax.lang.model.util.Types

val Context.types: Types get() = processingEnvironment().typeUtils

val Context.generic: Boolean get() = autoValueClass().typeParameters.isNotEmpty()

val Context.typeVariables: List<TypeVariableName> get() = autoValueClass().typeParameters
    .map { TypeVariableName.get(it) }
