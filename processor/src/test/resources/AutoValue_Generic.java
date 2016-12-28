package test;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

final class AutoValue_Generic<T1, T2> extends $AutoValue_Generic<T1, T2> {
  AutoValue_Generic(String a, T1 b, List<T2> c, Map<T2, List<T1>> d) {
    super(a, b, c, d);
  }

  static final class AutoMoshi_JsonAdapter<T1, T2> extends JsonAdapter<Generic<T1, T2>> {
    private final JsonAdapter<String> aAdapter;
    private final JsonAdapter<T1> bAdapter;
    private final JsonAdapter<List<T2>> cAdapter;
    private final JsonAdapter<Map<T2, List<T1>>> dAdapter;

    AutoMoshi_JsonAdapter(Moshi moshi, Type[] typeArgs) {
      aAdapter = moshi.adapter(String.class);
      bAdapter = moshi.adapter(typeArgs[0]);
      cAdapter = moshi.adapter(Types.newParameterizedType(List.class, typeArgs[1]));
      dAdapter = moshi.adapter(Types.newParameterizedType(Map.class, typeArgs[1],
          Types.newParameterizedType(List.class, typeArgs[0])));
    }

    @Override public Generic<T1, T2> fromJson(JsonReader reader) throws IOException {
      String a = null;
      T1 b = null;
      List<T2> c = null;
      Map<T2, List<T1>> d = null;
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
          case "d": {
            d = dAdapter.fromJson(reader);
            break;
          }
          default: {
            reader.skipValue();
          }
        }
      }
      reader.endObject();
      return new AutoValue_Generic<>(a, b, c, d);
    }

    @Override public void toJson(JsonWriter writer, Generic<T1, T2> value) throws IOException {
      writer.beginObject();
      writer.name("a");
      aAdapter.toJson(writer, value.a());
      writer.name("b");
      bAdapter.toJson(writer, value.b());
      writer.name("c");
      cAdapter.toJson(writer, value.c());
      writer.name("d");
      dAdapter.toJson(writer, value.d());
      writer.endObject();
    }
  }
}
