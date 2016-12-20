package glass.phil.auto.moshi

import com.google.auto.value.processor.AutoValueProcessor
import com.google.common.truth.Truth.assertThat
import com.google.testing.compile.CompilationSubject.assertThat
import com.google.testing.compile.Compiler
import org.junit.Test

class AutoMoshiExtensionTest {
  @Test fun `Only generates subclass when @AutoMoshi is present`() {
    compiler().compileResource("NoAutoMoshi").let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it.generatedSourceFileWithSimpleName("AutoValue_NoAutoMoshi")).isTrue()
      assertThat(it.generatedSourceFileWithSimpleName("\$AutoValue_NoAutoMoshi")).isFalse()
    }
  }

  @Test fun `Supports simple properties`() {
    compiler().compileResource("Simple").let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFileEquivalentToResource("AutoValue_Simple")
    }
  }

  private fun compiler() = Compiler.javac().withProcessors(AutoValueProcessor(listOf(AutoMoshiExtension())))
}
