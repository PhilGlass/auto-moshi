package test;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;

public final class $AutoMoshi_Nested_Inner_JsonAdapter extends JsonAdapter<Nested.Inner> {
  private final JsonAdapter<String> aAdapter;

  public $AutoMoshi_Nested_Inner_JsonAdapter(Moshi moshi) {
    aAdapter = moshi.adapter(String.class);
  }

  @Override public Nested.Inner fromJson(JsonReader reader) throws IOException {
    String a = null;
    reader.beginObject();
    while (reader.hasNext()) {
      final String name = reader.nextName();
      switch (name) {
        case "a": {
          a = aAdapter.fromJson(reader);
          break;
        }
        default: {
          reader.skipValue();
        }
      }
    }
    reader.endObject();
    return new AutoValue_Nested_Inner(a);
  }

  @Override public void toJson(JsonWriter writer, Nested.Inner value) throws IOException {
    writer.beginObject();
    writer.name("a");
    aAdapter.toJson(writer, value.a());
    writer.endObject();
  }
}
