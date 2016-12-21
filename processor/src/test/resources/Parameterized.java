package test;

import com.google.auto.value.AutoValue;
import java.util.List;
import java.util.Map;
import glass.phil.auto.moshi.AutoMoshi;

@AutoValue @AutoMoshi abstract class Parameterized {
  abstract List<String> a();
  abstract Map<String, Integer> b();
  abstract Map<String, List<Integer>> c();
}
