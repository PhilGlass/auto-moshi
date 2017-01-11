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

public final class $AutoMoshi_NullableProperties_JsonAdapter<T> extends JsonAdapter<NullableProperties<T>> {
  private final JsonAdapter<String> aAdapter;
  private final JsonAdapter<String> bAdapter;
  private final JsonAdapter<List<String>> cAdapter;
  private final JsonAdapter<T> dAdapter;
  private final JsonAdapter<List<T>> eAdapter;

  public $AutoMoshi_NullableProperties_JsonAdapter(Moshi moshi, Type[] typeArgs) {
    aAdapter = moshi.adapter(String.class);
    bAdapter = moshi.adapter(String.class).nullSafe();
    cAdapter = moshi.<List<String>>adapter(Types.newParameterizedType(List.class, String.class)).nullSafe();
    dAdapter = moshi.<T>adapter(typeArgs[0]).nullSafe();
    eAdapter = moshi.<List<T>>adapter(Types.newParameterizedType(List.class, typeArgs[0])).nullSafe();
  }

  @Override public NullableProperties<T> fromJson(JsonReader reader) throws IOException {
    String a = null;
    String b = null;
    List<String> c = null;
    T d = null;
    List<T> e = null;
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
        case "e": {
          e = eAdapter.fromJson(reader);
          break;
        }
        default: {
          reader.skipValue();
        }
      }
    }
    reader.endObject();
    return new AutoValue_NullableProperties<>(a, b, c, d, e);
  }

  @Override public void toJson(JsonWriter writer, NullableProperties<T> value) throws IOException {
    writer.beginObject();
    writer.name("a");
    aAdapter.toJson(writer, value.a());
    writer.name("b");
    bAdapter.toJson(writer, value.b());
    writer.name("c");
    cAdapter.toJson(writer, value.c());
    writer.name("d");
    dAdapter.toJson(writer, value.d());
    writer.name("e");
    eAdapter.toJson(writer, value.e());
    writer.endObject();
  }
}
