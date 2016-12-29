package test;

import com.google.auto.value.AutoValue;
import glass.phil.auto.moshi.AutoMoshi;

class Nested {
  @AutoValue @AutoMoshi static abstract class Inner {
    abstract String a();
  }
}
