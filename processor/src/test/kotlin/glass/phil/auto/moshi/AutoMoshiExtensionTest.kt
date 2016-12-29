package glass.phil.auto.moshi

import com.google.auto.value.processor.AutoValueProcessor
import com.google.common.truth.Truth.assertThat
import com.google.testing.compile.CompilationSubject.assertThat
import com.google.testing.compile.Compiler
import org.junit.Test

class AutoMoshiExtensionTest {
  @Test fun `Only generates subclass when @AutoMoshi is present`() {
    compiler().compileResources("NoAutoMoshi").let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it.generatedSourceFileWithSimpleName("AutoValue_NoAutoMoshi")).isTrue()
      assertThat(it.generatedSourceFileWithSimpleName("\$AutoValue_NoAutoMoshi")).isFalse()
    }
  }

  @Test fun `Supports simple properties`() {
    compiler().compileResources("Simple").let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFileEquivalentToResource("AutoValue_Simple")
    }
  }

  @Test fun `Supports @Json`() {
    compiler().compileResources("JsonProperty").let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFileEquivalentToResource("AutoValue_JsonProperty")
    }
  }

  @Test fun `Supports @Nullable`() {
    compiler().compileResources("Nullable", "NullableProperty").let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFileEquivalentToResource("AutoValue_NullableProperty")
    }
  }

  @Test fun `Supports parameterized properties`() {
    compiler().compileResources("Parameterized").let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFileEquivalentToResource("AutoValue_Parameterized")
    }
  }

  @Test fun `Supports simple classes with a generic superclass`() {
    compiler().compileResources("Base", "SimpleExtendsGeneric").let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFileEquivalentToResource("AutoValue_SimpleExtendsGeneric")
    }
  }

  @Test fun `Supports generic classes`() {
    compiler().compileResources("Generic").let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFileEquivalentToResource("AutoValue_Generic")
    }
  }

  @Test fun `Supports generic classes with a generic superclass`() {
    compiler().compileResources("Base", "GenericExtendsGeneric").let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFileEquivalentToResource("AutoValue_GenericExtendsGeneric")
    }
  }

  @Test fun `Supports nested classes`() {
    compiler().compileResources("Nested").let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFileEquivalentToResource("AutoValue_Parent_Nested")
    }
  }

  private fun compiler() = Compiler.javac().withProcessors(AutoValueProcessor(listOf(AutoMoshiExtension())))
}
