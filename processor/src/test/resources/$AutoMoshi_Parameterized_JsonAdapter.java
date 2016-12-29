package test;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.util.List;
import java.util.Map;

public final class $AutoMoshi_Parameterized_JsonAdapter extends JsonAdapter<Parameterized> {
  private final JsonAdapter<List<String>> aAdapter;
  private final JsonAdapter<Map<String, Integer>> bAdapter;
  private final JsonAdapter<Map<String, List<Integer>>> cAdapter;

  public $AutoMoshi_Parameterized_JsonAdapter(Moshi moshi) {
    aAdapter = moshi.adapter(Types.newParameterizedType(List.class, String.class));
    bAdapter = moshi.adapter(Types.newParameterizedType(Map.class, String.class, Integer.class));
    cAdapter = moshi.adapter(Types.newParameterizedType(Map.class, String.class,
        Types.newParameterizedType(List.class, Integer.class)));
  }

  @Override public Parameterized fromJson(JsonReader reader) throws IOException {
    List<String> a = null;
    Map<String, Integer> b = null;
    Map<String, List<Integer>> c = null;
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
    return new AutoValue_Parameterized(a, b, c);
  }

  @Override public void toJson(JsonWriter writer, Parameterized value) throws IOException {
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

