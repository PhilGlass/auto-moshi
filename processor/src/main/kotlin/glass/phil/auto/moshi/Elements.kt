package glass.phil.auto.moshi

import com.google.auto.common.MoreElements.getPackage
import com.google.auto.common.MoreElements.isType
import com.google.auto.common.Visibility
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier.STATIC

val Element.packageName: String get() = getPackage(this).qualifiedName.toString()

fun Element.hasAnnotation(simpleName: String) = annotationMirrors
    .map { it.annotationType.asElement().simpleName }.any { it.contentEquals(simpleName) }

fun Element.isVisibleTo(other: Element) = getPackage(this) == getPackage(other) ||
    Visibility.effectiveVisibilityOfElement(this) == Visibility.PUBLIC

fun Element.isEffectivelyStatic(): Boolean {
  if (enclosingElement == null || !isType(enclosingElement)) {
    return true
  }
  return modifiers.contains(STATIC) && enclosingElement.isEffectivelyStatic()
}

fun Element.generatedName(name: String = simpleName.toString()): String {
  if (enclosingElement == null || !isType(enclosingElement)) {
    return name
  }
  return enclosingElement.generatedName("${enclosingElement.simpleName}_$name")
}
