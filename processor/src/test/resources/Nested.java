package test;

import com.google.auto.value.AutoValue;
import glass.phil.auto.moshi.AutoMoshi;

class Parent {
  @AutoValue @AutoMoshi static abstract class Nested {
    abstract String a();
  }
}
