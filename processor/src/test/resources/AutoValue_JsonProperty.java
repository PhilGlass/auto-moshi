package test;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;

final class AutoValue_JsonProperty extends $AutoValue_JsonProperty {
  AutoValue_JsonProperty(String a, String b) {
    super(a, b);
  }

  static final class AutoMoshi_JsonAdapter extends JsonAdapter<JsonProperty> {
    private final JsonAdapter<String> aAdapter;
    private final JsonAdapter<String> bAdapter;

    AutoMoshi_JsonAdapter(Moshi moshi) {
      aAdapter = moshi.adapter(String.class);
      bAdapter = moshi.adapter(String.class);
    }

    @Override public JsonProperty fromJson(JsonReader reader) throws IOException {
      String a = null;
      String b = null;
      reader.beginObject();
      while (reader.hasNext()) {
        final String name = reader.nextName();
        switch (name) {
          case "a": {
            a = aAdapter.fromJson(reader);
            break;
          }
          case "xyz": {
            b = bAdapter.fromJson(reader);
            break;
          }
          default: {
            reader.skipValue();
          }
        }
      }
      reader.endObject();
      return new AutoValue_JsonProperty(a, b);
    }

    @Override public void toJson(JsonWriter writer, JsonProperty value) throws IOException {
      writer.beginObject();
      writer.name("a");
      aAdapter.toJson(writer, value.a());
      writer.name("xyz");
      bAdapter.toJson(writer, value.b());
      writer.endObject();
    }
  }
}
