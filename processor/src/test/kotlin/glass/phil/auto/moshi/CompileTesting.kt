package glass.phil.auto.moshi

import com.google.testing.compile.Compilation
import com.google.testing.compile.CompilationSubject
import com.google.testing.compile.Compiler
import com.google.testing.compile.JavaFileObjects
import javax.tools.JavaFileObject
import javax.tools.JavaFileObject.Kind.SOURCE

fun Compiler.compileResources(vararg simpleNames: String): Compilation = compile(simpleNames.map(::forJavaResource))

fun Compilation.generatedSourceFileWithSimpleName(simpleName: String) =
    generatedSourceFiles().any { it.isNameCompatible(simpleName, SOURCE) }

fun CompilationSubject.generatedSourceFilesEquivalentToResources(vararg simpleNames: String) = simpleNames.forEach {
  generatedSourceFile("test.$it").hasSourceEquivalentTo(forJavaResource(it))
}

private fun forJavaResource(simpleName: String): JavaFileObject = JavaFileObjects.forResource("$simpleName.java")
