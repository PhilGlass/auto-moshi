package test;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.lang.Boolean;
import java.lang.Override;
import java.lang.String;

public final class $AutoMoshi_PrefixedProperties_JsonAdapter extends JsonAdapter<PrefixedProperties> {
  private final JsonAdapter<Boolean> aAdapter;
  private final JsonAdapter<String> bAdapter;

  public $AutoMoshi_PrefixedProperties_JsonAdapter(Moshi moshi) {
    aAdapter = moshi.adapter(boolean.class);
    bAdapter = moshi.adapter(String.class);
  }

  @Override public PrefixedProperties fromJson(JsonReader reader) throws IOException {
    boolean a = false;
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
    return new AutoValue_PrefixedProperties(a, b);
  }

  @Override public void toJson(JsonWriter writer, PrefixedProperties value) throws IOException {
    writer.beginObject();
    writer.name("a");
    aAdapter.toJson(writer, value.isA());
    writer.name("b");
    bAdapter.toJson(writer, value.getB());
    writer.endObject();
  }
}
