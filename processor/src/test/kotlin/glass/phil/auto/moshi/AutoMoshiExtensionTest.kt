package glass.phil.auto.moshi

import com.google.auto.value.processor.AutoValueProcessor
import com.google.common.truth.Truth.assertThat
import com.google.testing.compile.CompilationSubject.assertThat
import com.google.testing.compile.Compiler
import org.junit.Test

class AutoMoshiExtensionTest {
  @Test fun `Class generation only triggered when @AutoMoshi is present`() {
    compiler().compileResources("NoAutoMoshi").let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it.generatedSourceFileWithSimpleName("AutoValue_NoAutoMoshi")).isTrue()
      assertThat(it.generatedSourceFileWithSimpleName("\$AutoValue_NoAutoMoshi")).isFalse()
      assertThat(it.generatedSourceFileWithSimpleName("\$AutoMoshi_NoAutoMoshi_JsonAdapter")).isFalse()
    }
  }

  @Test fun `Supports simple properties`() {
    compiler().compileResources("Simple").let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFilesEquivalentToResources("AutoValue_Simple", "\$AutoMoshi_Simple_JsonAdapter")
    }
  }

  @Test fun `Supports @Json`() {
    compiler().compileResources("JsonProperty").let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFilesEquivalentToResources("AutoValue_JsonProperty",
          "\$AutoMoshi_JsonProperty_JsonAdapter")
    }
  }

  @Test fun `Supports @Nullable`() {
    compiler().compileResources("Nullable", "NullableProperty").let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFilesEquivalentToResources("AutoValue_NullableProperty",
          "\$AutoMoshi_NullableProperty_JsonAdapter")
    }
  }

  @Test fun `Supports parameterized properties`() {
    compiler().compileResources("Parameterized").let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFilesEquivalentToResources("AutoValue_Parameterized",
          "\$AutoMoshi_Parameterized_JsonAdapter")
    }
  }

  @Test fun `Supports simple classes with a generic superclass`() {
    compiler().compileResources("Base", "SimpleExtendsGeneric").let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFilesEquivalentToResources("AutoValue_SimpleExtendsGeneric",
          "\$AutoMoshi_SimpleExtendsGeneric_JsonAdapter")
    }
  }

  @Test fun `Supports generic classes`() {
    compiler().compileResources("Generic").let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFilesEquivalentToResources("AutoValue_Generic",
          "\$AutoMoshi_Generic_JsonAdapter")
    }
  }

  @Test fun `Supports generic classes with a generic superclass`() {
    compiler().compileResources("Base", "GenericExtendsGeneric").let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFilesEquivalentToResources("AutoValue_GenericExtendsGeneric",
          "\$AutoMoshi_GenericExtendsGeneric_JsonAdapter")
    }
  }

  @Test fun `Supports nested classes`() {
    compiler().compileResources("Nested").let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFilesEquivalentToResources("AutoValue_Nested_Inner",
          "\$AutoMoshi_Nested_Inner_JsonAdapter")
    }
  }

  @Test fun `Supports bean prefixed properties`() {
    compiler().compileResources("PrefixedProperties").let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFilesEquivalentToResources("AutoValue_PrefixedProperties",
          "\$AutoMoshi_PrefixedProperties_JsonAdapter")
    }
  }

  private fun compiler() = Compiler.javac().withProcessors(AutoValueProcessor(listOf(AutoMoshiExtension())))
}
