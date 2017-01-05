# AutoMoshi

An [AutoValue][autovalue] extension that supports JSON serialization and deserialization through [Moshi][moshi].

## Usage

To opt in to Moshi support, annotate your AutoValue class with `@AutoMoshi`:

```java
@AutoValue @AutoMoshi abstract class Foo {
  abstract String a();
}
```

AutoMoshi will generate a [`JsonAdapter`][jsonadapter] for each `@AutoMoshi` annotated class.
There is generally no need to reference these generated `JsonAdapter`s directly - instead, create an abstract class that implements [`JsonAdapter.Factory`][jsonadapter.factory] and annotate it with `@AutoMoshi.Factory`.
AutoMoshi will generate a package-private implementation of this factory class, which should be passed to the builder used to create your `Moshi` instance:

```java
@AutoMoshi.Factory abstract class AdapterFactory implements JsonAdapter.Factory {
  static JsonAdapter.Factory create() {
    return new AutoMoshi_AdapterFactory();
  }
}
```

```java
Moshi moshi = new Moshi.Builder().add(AdapterFactory.create()).build();
```

If your factory class is nested, it must be static and must have at least package visibility.
The generated implementation will have a name of the form `AutoMoshi_Outer_Middle_Inner`, where `Inner` is the name of your factory class:

```java
class Outer {
  @AutoMoshi.Factory static abstract class InnerAdapterFactory implements JsonAdapter.Factory {
    static JsonAdapter.Factory create() {
      return new AutoMoshi_Outer_InnerAdapterFactory();
    }
  }
}
```

## Download

To use AutoMoshi in an Android project, add the following lines to your `build.gradle`:

```groovy
dependencies {
  provided 'glass.phil.auto.moshi:auto-moshi-annotations:0.1.0'
  annotationProcessor 'glass.phil.auto.moshi:auto-moshi-processor:0.1.0'
}
```

As these artifacts are available only at compile time, an explicit dependency on Moshi is required:

```groovy
dependencies {
  compile 'com.squareup.moshi:moshi:1.3.1'
}
```

Releases are published to [Maven Central][central].
Snapshots are published to Sonatype's [snapshots repository][snapshots].

## Acknowledgements
This library is heavily influenced by [auto-value-moshi][auto-value-moshi].

[autovalue]: https://github.com/google/auto/tree/master/value
[moshi]: https://github.com/square/moshi
[jsonadapter]: http://square.github.io/moshi/1.x/moshi/com/squareup/moshi/JsonAdapter.html
[jsonadapter.factory]: http://square.github.io/moshi/1.x/moshi/com/squareup/moshi/JsonAdapter.Factory.html
[central]: https://search.maven.org/#search%7Cga%7C1%7Cglass.phil.auto.moshi
[snapshots]: https://oss.sonatype.org/content/repositories/snapshots/glass/phil/auto/moshi
[auto-value-moshi]: https://github.com/rharter/auto-value-moshi
