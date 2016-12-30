package glass.phil.auto.moshi

import com.google.auto.value.processor.AutoValueProcessor
import com.google.common.truth.Truth.assertThat
import com.google.testing.compile.CompilationSubject.assertThat
import com.google.testing.compile.Compiler
import org.junit.Test

class AutoMoshiExtensionTest {
  @Test fun `Class generation only triggered when @AutoMoshi is present`() {
    compiler().compile(JavaResource("NoAutoMoshi")).let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it.generatedSourceFileWithSimpleName("AutoValue_NoAutoMoshi")).isTrue()
      assertThat(it.generatedSourceFileWithSimpleName("\$AutoValue_NoAutoMoshi")).isFalse()
      assertThat(it.generatedSourceFileWithSimpleName("\$AutoMoshi_NoAutoMoshi_JsonAdapter")).isFalse()
    }
  }

  @Test fun `Supports simple properties`() {
    compiler().compile(JavaResource("Simple")).let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFilesEquivalentTo(JavaResource("AutoValue_Simple"),
          JavaResource("\$AutoMoshi_Simple_JsonAdapter"))
    }
  }

  @Test fun `Supports @Json`() {
    compiler().compile(JavaResource("JsonProperty")).let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFilesEquivalentTo(JavaResource("AutoValue_JsonProperty"),
          JavaResource("\$AutoMoshi_JsonProperty_JsonAdapter"))
    }
  }

  @Test fun `Supports @Nullable`() {
    compiler().compile(JavaResource("Nullable"), JavaResource("NullableProperty")).let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFilesEquivalentTo(JavaResource("AutoValue_NullableProperty"),
          JavaResource("\$AutoMoshi_NullableProperty_JsonAdapter"))
    }
  }

  @Test fun `Supports parameterized properties`() {
    compiler().compile(JavaResource("Parameterized")).let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFilesEquivalentTo(JavaResource("AutoValue_Parameterized"),
          JavaResource("\$AutoMoshi_Parameterized_JsonAdapter"))
    }
  }

  @Test fun `Supports simple classes with a generic superclass`() {
    compiler().compile(JavaResource("Base"), JavaResource("SimpleExtendsGeneric")).let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFilesEquivalentTo(JavaResource("AutoValue_SimpleExtendsGeneric"),
          JavaResource("\$AutoMoshi_SimpleExtendsGeneric_JsonAdapter"))
    }
  }

  @Test fun `Supports generic classes`() {
    compiler().compile(JavaResource("Generic")).let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFilesEquivalentTo(JavaResource("AutoValue_Generic"),
          JavaResource("\$AutoMoshi_Generic_JsonAdapter"))
    }
  }

  @Test fun `Supports generic classes with a generic superclass`() {
    compiler().compile(JavaResource("Base"), JavaResource("GenericExtendsGeneric")).let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFilesEquivalentTo(JavaResource("AutoValue_GenericExtendsGeneric"),
          JavaResource("\$AutoMoshi_GenericExtendsGeneric_JsonAdapter"))
    }
  }

  @Test fun `Supports nested classes`() {
    compiler().compile(JavaResource("Nested")).let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFilesEquivalentTo(JavaResource("AutoValue_Nested_Inner"),
          JavaResource("\$AutoMoshi_Nested_Inner_JsonAdapter"))
    }
  }

  @Test fun `Supports bean prefixed properties`() {
    compiler().compile(JavaResource("PrefixedProperties")).let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFilesEquivalentTo(JavaResource("AutoValue_PrefixedProperties"),
          JavaResource("\$AutoMoshi_PrefixedProperties_JsonAdapter"))
    }
  }

  private fun compiler() = Compiler.javac().withProcessors(AutoValueProcessor(listOf(AutoMoshiExtension())))
}
