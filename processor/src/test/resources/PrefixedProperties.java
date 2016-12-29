package test;

import com.google.auto.value.AutoValue;
import glass.phil.auto.moshi.AutoMoshi;

@AutoValue @AutoMoshi abstract class PrefixedProperties {
  abstract boolean isA();
  abstract String getB();
}
