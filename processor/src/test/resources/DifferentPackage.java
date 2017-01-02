package different;

import com.google.auto.value.AutoValue;
import glass.phil.auto.moshi.AutoMoshi;

@AutoValue @AutoMoshi abstract class DifferentPackage {
  abstract String a();
}
