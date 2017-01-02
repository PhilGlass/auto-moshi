package different;

import com.google.auto.value.AutoValue;
import glass.phil.auto.moshi.AutoMoshi;

@AutoValue @AutoMoshi abstract class DifferentPackageGeneric<T> {
  abstract T a();
}
