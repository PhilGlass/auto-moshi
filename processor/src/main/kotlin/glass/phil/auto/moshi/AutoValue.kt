package glass.phil.auto.moshi

import com.google.auto.value.extension.AutoValueExtension.Context
import com.squareup.javapoet.TypeVariableName
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.lang.model.util.Types

val Context.filer: Filer get() = processingEnvironment().filer

val Context.messager: Messager get() = processingEnvironment().messager

val Context.types: Types get() = processingEnvironment().typeUtils

val Context.generic: Boolean get() = autoValueClass().typeParameters.isNotEmpty()

val Context.typeVariables: List<TypeVariableName> get() = autoValueClass().typeParameters
    .map { TypeVariableName.get(it) }
