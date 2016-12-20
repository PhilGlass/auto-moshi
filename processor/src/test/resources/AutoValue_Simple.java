package test;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;

final class AutoValue_Simple extends $AutoValue_Simple {
  AutoValue_Simple(int a, long[] b, String c) {
    super(a, b, c);
  }

  static final class AutoMoshi_JsonAdapter extends JsonAdapter<Simple> {
    private final JsonAdapter<Integer> aAdapter;
    private final JsonAdapter<long[]> bAdapter;
    private final JsonAdapter<String> cAdapter;

    AutoMoshi_JsonAdapter(Moshi moshi) {
      aAdapter = moshi.adapter(int.class);
      bAdapter = moshi.adapter(long[].class);
      cAdapter = moshi.adapter(String.class);
    }

    @Override public Simple fromJson(JsonReader reader) throws IOException {
      int a = 0;
      long[] b = null;
      String c = null;
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
          case "c": {
            c = cAdapter.fromJson(reader);
            break;
          }
          default: {
            reader.skipValue();
          }
        }
      }
      reader.endObject();
      return new AutoValue_Simple(a, b, c);
    }

    @Override public void toJson(JsonWriter writer, Simple value) throws IOException {
      writer.beginObject();
      writer.name("a");
      aAdapter.toJson(writer, value.a());
      writer.name("b");
      bAdapter.toJson(writer, value.b());
      writer.name("c");
      cAdapter.toJson(writer, value.c());
      writer.endObject();
    }
  }
}
