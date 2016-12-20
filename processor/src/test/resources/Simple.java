package test;

import com.google.auto.value.AutoValue;
import glass.phil.auto.moshi.AutoMoshi;

@AutoValue @AutoMoshi abstract class Simple {
  abstract int a();
  @SuppressWarnings("mutable")
  abstract long[] b();
  abstract String c();
}
