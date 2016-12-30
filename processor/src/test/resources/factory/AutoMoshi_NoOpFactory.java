package test;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.lang.Override;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

final class AutoMoshi_NoOpFactory extends NoOpFactory {
  @Override public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
    return null;
  }
}
