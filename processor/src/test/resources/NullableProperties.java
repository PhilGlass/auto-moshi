package test;

import com.google.auto.value.AutoValue;
import java.util.List;
import glass.phil.auto.moshi.AutoMoshi;

@AutoValue @AutoMoshi abstract class NullableProperties<T> {
  abstract String a();
  @Nullable abstract String b();
  @Nullable abstract List<String> c();
  @Nullable abstract T d();
  @Nullable abstract List<T> e();
}
