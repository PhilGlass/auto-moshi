package test;

import com.google.auto.value.AutoValue;
import java.util.List;
import glass.phil.auto.moshi.AutoMoshi;

@AutoValue @AutoMoshi abstract class ExtendsGeneric extends Base<String, Integer> {
  abstract Long a();
  abstract List<Long> b();
}
