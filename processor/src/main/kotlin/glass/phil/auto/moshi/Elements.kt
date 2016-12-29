package glass.phil.auto.moshi

import com.google.auto.common.MoreElements
import javax.lang.model.element.Element

fun Element.hasAnnotation(simpleName: String) = annotationMirrors
    .map { it.annotationType.asElement().simpleName }.any { it.contentEquals(simpleName) }

fun Element.generatedName(name: String = simpleName.toString()): String {
  if (enclosingElement == null || !MoreElements.isType(enclosingElement)) {
    return name
  }
  return enclosingElement.generatedName("${enclosingElement.simpleName}_$name")
}
