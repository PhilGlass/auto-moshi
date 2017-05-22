package glass.phil.auto.moshi

import com.google.auto.value.processor.AutoValueProcessor
import com.google.common.truth.Truth.assertThat
import com.google.testing.compile.CompilationSubject.assertThat
import com.google.testing.compile.Compiler
import org.junit.Test

class AutoMoshiFactoryProcessorTest {
  @Test fun `Class generation only triggered when @AutoMoshi_Factory is present`() {
    compiler().compile(JavaResource("Simple"), factoryResource("NoAutoMoshiFactory")).let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it.generatedSourceFileWithSimpleName("AutoMoshi_NoAutoMoshiFactory")).isFalse()
    }
  }

  @Test fun `Generates no-op factory when there are no @AutoMoshi classes`() {
    compiler().compile(factoryResource("NoOpFactory")).let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFileEquivalentTo(factoryResource("AutoMoshi_NoOpFactory"))
    }
  }

  @Test fun `Supports simple types`() {
    compiler().compile(JavaResource("Simple"), JavaResource("JsonProperty"), factoryResource("SimpleFactory")).let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFileEquivalentTo(factoryResource("AutoMoshi_SimpleFactory"))
    }
  }

  @Test fun `Supports generic types`() {
    compiler().compile(
        JavaResource("Base"), JavaResource("Generic"), JavaResource("GenericExtendsGeneric"),
        factoryResource("GenericFactory")
    ).let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFileEquivalentTo(factoryResource("AutoMoshi_GenericFactory"))
    }
  }

  @Test fun `Supports simple and generic types`() {
    compiler().compile(JavaResource("Simple"), JavaResource("Generic"), factoryResource("MixedFactory")).let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFileEquivalentTo(factoryResource("AutoMoshi_MixedFactory"))
    }
  }

  @Test fun `Supports simple and generic types in different packages`() {
    compiler().compile(
        JavaResource("Simple"), JavaResource(packageName = "different", simpleName = "DifferentPackage"),
        JavaResource("Generic"), JavaResource(packageName = "different", simpleName = "DifferentPackageGeneric"),
        factoryResource("DifferentPackagesFactory")
    ).let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFileEquivalentTo(factoryResource("AutoMoshi_DifferentPackagesFactory"))
    }
  }

  @Test fun `Supports nested factory`() {
    compiler().compile(JavaResource("Simple"), factoryResource("NestedFactory")).let {
      assertThat(it).succeededWithoutWarnings()
      assertThat(it).generatedSourceFileEquivalentTo(factoryResource("AutoMoshi_NestedFactory_Factory"))
    }
  }

  @Test fun `Emits error if factory is not abstract`() {
    compiler().compile(factoryResource("NonAbstractFactory")).let {
      assertThat(it).failed()
      assertThat(it).hadErrorContaining("must be abstract")
    }
  }

  @Test fun `Emits error if factory does not implement JsonAdapter_Factory`() {
    compiler().compile(factoryResource("NoInterfaceFactory")).let {
      assertThat(it).failed()
      assertThat(it).hadErrorContaining("must implement JsonAdapter.Factory")
    }
  }

  @Test fun `Emits error if nested factory is private`() {
    compiler().compile(factoryResource("PrivateFactory")).let {
      assertThat(it).failed()
      assertThat(it).hadErrorContaining("must have at least package visibility")
    }
  }

  @Test fun `Emits error if nested factory is non-static`() {
    compiler().compile(factoryResource("NonStaticFactory")).let {
      assertThat(it).failed()
      assertThat(it).hadErrorContaining("must be static")
    }
  }

  private fun compiler() = Compiler.javac().withProcessors(
      AutoValueProcessor(listOf(AutoMoshiExtension())),
      AutoMoshiFactoryProcessor()
  )

  private fun factoryResource(simpleName: String) = JavaResource(pathPrefix = "factory/", simpleName = simpleName)
}
