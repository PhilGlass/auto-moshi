package glass.phil.auto.moshi

import com.google.testing.compile.Compilation
import com.google.testing.compile.CompilationSubject
import com.google.testing.compile.Compiler
import com.google.testing.compile.JavaFileObjects
import javax.tools.JavaFileObject.Kind.SOURCE

data class JavaResource(val simpleName: String, val pathPrefix: String = "", val packageName: String = "test") {
  val filePath = "$pathPrefix$simpleName.java"
  val qualifiedName = if (packageName.isNullOrBlank()) simpleName else "$packageName.$simpleName"
}

fun Compiler.compile(vararg javaResources: JavaResource): Compilation =
    compile(javaResources.map { JavaFileObjects.forResource(it.filePath) })

fun Compilation.generatedSourceFileWithSimpleName(simpleName: String) =
    generatedSourceFiles().any { it.isNameCompatible(simpleName, SOURCE) }

fun CompilationSubject.generatedSourceFileEquivalentTo(resource: JavaResource) =
    generatedSourceFile(resource.qualifiedName).hasSourceEquivalentTo(JavaFileObjects.forResource(resource.filePath))
