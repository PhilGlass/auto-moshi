package test;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.util.List;
import java.util.Map;

public final class $AutoMoshi_SimpleExtendsGeneric_JsonAdapter extends JsonAdapter<SimpleExtendsGeneric> {
  private final JsonAdapter<String> _aAdapter;
  private final JsonAdapter<List<Integer>> _bAdapter;
  private final JsonAdapter<Map<Integer, List<String>>> _cAdapter;
  private final JsonAdapter<Long> aAdapter;
  private final JsonAdapter<List<Long>> bAdapter;

  public $AutoMoshi_SimpleExtendsGeneric_JsonAdapter(Moshi moshi) {
    _aAdapter = moshi.adapter(String.class);
    _bAdapter = moshi.adapter(Types.newParameterizedType(List.class, Integer.class));
    _cAdapter = moshi.adapter(Types.newParameterizedType(Map.class, Integer.class,
        Types.newParameterizedType(List.class, String.class)));
    aAdapter = moshi.adapter(Long.class);
    bAdapter = moshi.adapter(Types.newParameterizedType(List.class, Long.class));
  }

  @Override public SimpleExtendsGeneric fromJson(JsonReader reader) throws IOException {
    String _a = null;
    List<Integer> _b = null;
    Map<Integer, List<String>> _c = null;
    Long a = null;
    List<Long> b = null;
    reader.beginObject();
    while (reader.hasNext()) {
      final String name = reader.nextName();
      switch (name) {
        case "_a": {
          _a = _aAdapter.fromJson(reader);
          break;
        }
        case "_b": {
          _b = _bAdapter.fromJson(reader);
          break;
        }
        case "_c": {
          _c = _cAdapter.fromJson(reader);
          break;
        }
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
    return new AutoValue_SimpleExtendsGeneric(_a, _b, _c, a, b);
  }

  @Override public void toJson(JsonWriter writer, SimpleExtendsGeneric value) throws IOException {
    writer.beginObject();
    writer.name("_a");
    _aAdapter.toJson(writer, value._a());
    writer.name("_b");
    _bAdapter.toJson(writer, value._b());
    writer.name("_c");
    _cAdapter.toJson(writer, value._c());
    writer.name("a");
    aAdapter.toJson(writer, value.a());
    writer.name("b");
    bAdapter.toJson(writer, value.b());
    writer.endObject();
  }
}
