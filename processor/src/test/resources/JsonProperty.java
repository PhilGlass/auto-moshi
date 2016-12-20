package test;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.Json;
import glass.phil.auto.moshi.AutoMoshi;

@AutoValue @AutoMoshi abstract class JsonProperty {
  abstract String a();
  @Json(name = "xyz") abstract String b();
}
