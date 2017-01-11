package test;

import com.google.auto.value.AutoValue;
import glass.phil.auto.moshi.AutoMoshi;

@AutoValue @AutoMoshi abstract class CollidingNames {
  abstract String name();
  abstract String reader();
  abstract String nameAdapter();
}
