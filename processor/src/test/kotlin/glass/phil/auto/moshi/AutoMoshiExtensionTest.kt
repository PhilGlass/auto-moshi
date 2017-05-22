package glass.phil.auto.moshi

import com.google.auto.value.processor.AutoValueProcessor
import com.google.common.truth.Truth.assertThat
import com.google.testing.compile.CompilationSubject.assertThat
import com.google.testing.compile.Compiler
import org.junit.Test

class AutoMoshiExtensionTest {
  @Test fun `Adapter generation only triggered when @AutoMoshi is present`() {
    compiler().compile(JavaResource("NoAutoMoshi")).let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it.generatedSourceFileWithSimpleName("AutoValue_NoAutoMoshi")).isTrue()
      assertThat(it.generatedSourceFileWithSimpleName("\$AutoMoshi_NoAutoMoshi_JsonAdapter")).isFalse()
    }
  }

  @Test fun `Supports simple properties`() {
    compiler().compile(JavaResource("Simple")).let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFileEquivalentTo(JavaResource("\$AutoMoshi_Simple_JsonAdapter"))
    }
  }

  @Test fun `Supports @Json`() {
    compiler().compile(JavaResource("JsonProperty")).let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFileEquivalentTo(JavaResource("\$AutoMoshi_JsonProperty_JsonAdapter"))
    }
  }

  @Test fun `Supports @Nullable`() {
    compiler().compile(JavaResource("Nullable"), JavaResource("NullableProperties")).let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFileEquivalentTo(JavaResource("\$AutoMoshi_NullableProperties_JsonAdapter"))
    }
  }

  @Test fun `Supports parameterized properties`() {
    compiler().compile(JavaResource("Parameterized")).let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFileEquivalentTo(JavaResource("\$AutoMoshi_Parameterized_JsonAdapter"))
    }
  }

  @Test fun `Supports simple classes with a generic superclass`() {
    compiler().compile(JavaResource("Base"), JavaResource("SimpleExtendsGeneric")).let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFileEquivalentTo(JavaResource("\$AutoMoshi_SimpleExtendsGeneric_JsonAdapter"))
    }
  }

  @Test fun `Supports generic classes`() {
    compiler().compile(JavaResource("Generic")).let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFileEquivalentTo(JavaResource("\$AutoMoshi_Generic_JsonAdapter"))
    }
  }

  @Test fun `Supports generic classes with a generic superclass`() {
    compiler().compile(JavaResource("Base"), JavaResource("GenericExtendsGeneric")).let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFileEquivalentTo(JavaResource("\$AutoMoshi_GenericExtendsGeneric_JsonAdapter"))
    }
  }

  @Test fun `Supports nested classes`() {
    compiler().compile(JavaResource("Nested")).let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFileEquivalentTo(JavaResource("\$AutoMoshi_Nested_Inner_JsonAdapter"))
    }
  }

  @Test fun `Handles bean prefixed properties`() {
    compiler().compile(JavaResource("PrefixedProperties")).let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFileEquivalentTo(JavaResource("\$AutoMoshi_PrefixedProperties_JsonAdapter"))
    }
  }

  @Test fun `Handles properties with colliding names`() {
    compiler().compile(JavaResource("CollidingNames")).let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFileEquivalentTo(JavaResource("\$AutoMoshi_CollidingNames_JsonAdapter"))
    }
  }

  private fun compiler() = Compiler.javac().withProcessors(AutoValueProcessor(listOf(AutoMoshiExtension())))
}
