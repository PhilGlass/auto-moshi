package glass.phil.auto.moshi

import javax.lang.model.element.ExecutableElement

fun ExecutableElement.hasAnnotation(simpleName: String) = annotationMirrors
    .map { it.annotationType.asElement().simpleName }.any { it.contentEquals(simpleName) }
