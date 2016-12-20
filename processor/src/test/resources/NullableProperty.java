package test;

import com.google.auto.value.AutoValue;
import glass.phil.auto.moshi.AutoMoshi;

@AutoValue @AutoMoshi abstract class NullableProperty {
  abstract String a();
  @Nullable abstract String b();
}
