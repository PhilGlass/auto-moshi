package test;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;

final class AutoValue_NullableProperty extends $AutoValue_NullableProperty {
  AutoValue_NullableProperty(String a, String b) {
    super(a, b);
  }

  static final class AutoMoshi_JsonAdapter extends JsonAdapter<NullableProperty> {
    private final JsonAdapter<String> aAdapter;
    private final JsonAdapter<String> bAdapter;

    AutoMoshi_JsonAdapter(Moshi moshi) {
      aAdapter = moshi.adapter(String.class);
      bAdapter = moshi.adapter(String.class).nullSafe();
    }

    @Override public NullableProperty fromJson(JsonReader reader) throws IOException {
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
          case "b": {
            b = bAdapter.fromJson(reader);
            break;
          }
          default: {
            reader.skipValue();
          }
        }
      }
      reader.endObject();
      return new AutoValue_NullableProperty(a, b);
    }

    @Override public void toJson(JsonWriter writer, NullableProperty value) throws IOException {
      writer.beginObject();
      writer.name("a");
      aAdapter.toJson(writer, value.a());
      writer.name("b");
      bAdapter.toJson(writer, value.b());
      writer.endObject();
    }
  }
}
