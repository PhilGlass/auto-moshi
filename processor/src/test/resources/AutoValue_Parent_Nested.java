package test;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;

final class AutoValue_Parent_Nested extends $AutoValue_Parent_Nested {
  AutoValue_Parent_Nested(String a) {
    super(a);
  }

  static final class AutoMoshi_JsonAdapter extends JsonAdapter<Parent.Nested> {
    private final JsonAdapter<String> aAdapter;

    AutoMoshi_JsonAdapter(Moshi moshi) {
      aAdapter = moshi.adapter(String.class);
    }

    @Override public Parent.Nested fromJson(JsonReader reader) throws IOException {
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
      return new AutoValue_Parent_Nested(a);
    }

    @Override public void toJson(JsonWriter writer, Parent.Nested value) throws IOException {
      writer.beginObject();
      writer.name("a");
      aAdapter.toJson(writer, value.a());
      writer.endObject();
    }
  }
}
