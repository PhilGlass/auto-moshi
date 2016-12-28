package test;

import com.google.auto.value.AutoValue;
import java.util.List;
import java.util.Map;
import glass.phil.auto.moshi.AutoMoshi;

@AutoValue @AutoMoshi abstract class Generic<T1, T2> {
  abstract String a();
  abstract T1 b();
  abstract List<T2> c();
  abstract Map<T2, List<T1>> d();
}
