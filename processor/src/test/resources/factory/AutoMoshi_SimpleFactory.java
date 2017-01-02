package test;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.lang.Class;
import java.lang.Override;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

final class AutoMoshi_SimpleFactory extends SimpleFactory {
  @Override public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
    if (type instanceof Class) {
      final Class<?> clazz = (Class<?>) type;
      if (Simple.class.equals(clazz)) {
        return new $AutoMoshi_Simple_JsonAdapter(moshi);
      }
      if (JsonProperty.class.equals(clazz)) {
        return new $AutoMoshi_JsonProperty_JsonAdapter(moshi);
      }
    }
    return null;
  }
}
