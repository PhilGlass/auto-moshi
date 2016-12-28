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

final class AutoValue_GenericExtendsGeneric<T1, T2> extends $AutoValue_GenericExtendsGeneric<T1, T2> {
  AutoValue_GenericExtendsGeneric(T2 _a, List<String> _b, Map<String, List<T2>> _c, String a, T1 b,
      List<T2> c, Map<T2, List<T1>> d) {
    super(_a, _b, _c, a, b, c, d);
  }

  static final class AutoMoshi_JsonAdapter<T1, T2> extends JsonAdapter<GenericExtendsGeneric<T1, T2>> {
    private final JsonAdapter<T2> _aAdapter;
    private final JsonAdapter<List<String>> _bAdapter;
    private final JsonAdapter<Map<String, List<T2>>> _cAdapter;
    private final JsonAdapter<String> aAdapter;
    private final JsonAdapter<T1> bAdapter;
    private final JsonAdapter<List<T2>> cAdapter;
    private final JsonAdapter<Map<T2, List<T1>>> dAdapter;

    AutoMoshi_JsonAdapter(Moshi moshi, Type[] typeArgs) {
      _aAdapter = moshi.adapter(typeArgs[1]);
      _bAdapter = moshi.adapter(Types.newParameterizedType(List.class, String.class));
      _cAdapter = moshi.adapter(Types.newParameterizedType(Map.class, String.class,
          Types.newParameterizedType(List.class, typeArgs[1])));
      aAdapter = moshi.adapter(String.class);
      bAdapter = moshi.adapter(typeArgs[0]);
      cAdapter = moshi.adapter(Types.newParameterizedType(List.class, typeArgs[1]));
      dAdapter = moshi.adapter(Types.newParameterizedType(Map.class, typeArgs[1],
          Types.newParameterizedType(List.class, typeArgs[0])));
    }

    @Override public GenericExtendsGeneric<T1, T2> fromJson(JsonReader reader) throws IOException {
      T2 _a = null;
      List<String> _b = null;
      Map<String, List<T2>> _c = null;
      String a = null;
      T1 b = null;
      List<T2> c = null;
      Map<T2, List<T1>> d = null;
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
      return new AutoValue_GenericExtendsGeneric<>(_a, _b, _c, a, b, c, d);
    }

    @Override public void toJson(JsonWriter writer, GenericExtendsGeneric<T1, T2> value) throws IOException {
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
      writer.name("c");
      cAdapter.toJson(writer, value.c());
      writer.name("d");
      dAdapter.toJson(writer, value.d());
      writer.endObject();
    }
  }
}
