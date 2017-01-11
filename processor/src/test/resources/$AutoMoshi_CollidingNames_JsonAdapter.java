package test;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;

public final class $AutoMoshi_CollidingNames_JsonAdapter extends JsonAdapter<CollidingNames> {
  private final JsonAdapter<String> nameAdapter;
  private final JsonAdapter<String> readerAdapter;
  private final JsonAdapter<String> nameAdapterAdapter;

  public $AutoMoshi_CollidingNames_JsonAdapter(Moshi moshi) {
    nameAdapter = moshi.adapter(String.class);
    readerAdapter = moshi.adapter(String.class);
    nameAdapterAdapter = moshi.adapter(String.class);
  }

  @Override public CollidingNames fromJson(JsonReader reader) throws IOException {
    String name = null;
    String reader_ = null;
    String nameAdapter_ = null;
    reader.beginObject();
    while (reader.hasNext()) {
      final String name_ = reader.nextName();
      switch (name_) {
        case "name": {
          name = nameAdapter.fromJson(reader);
          break;
        }
        case "reader": {
          reader_ = readerAdapter.fromJson(reader);
          break;
        }
        case "nameAdapter": {
          nameAdapter_ = nameAdapterAdapter.fromJson(reader);
          break;
        }
        default: {
          reader.skipValue();
        }
      }
    }
    reader.endObject();
    return new AutoValue_CollidingNames(name, reader_, nameAdapter_);
  }

  @Override public void toJson(JsonWriter writer, CollidingNames value) throws IOException {
    writer.beginObject();
    writer.name("name");
    nameAdapter.toJson(writer, value.name());
    writer.name("reader");
    readerAdapter.toJson(writer, value.reader());
    writer.name("nameAdapter");
    nameAdapterAdapter.toJson(writer, value.nameAdapter());
    writer.endObject();
  }
}
