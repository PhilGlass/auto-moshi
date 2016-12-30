package test;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.lang.Override;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

final class AutoMoshi_GenericFactory extends GenericFactory {
  @Override public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
    if (type instanceof ParameterizedType) {
      final Type rawType = ((ParameterizedType) type).getRawType();
      final Type[] typeArgs = ((ParameterizedType) type).getActualTypeArguments();
      if (Generic.class.equals(rawType)) {
        return new $AutoMoshi_Generic_JsonAdapter<>(moshi, typeArgs);
      }
      if (GenericExtendsGeneric.class.equals(rawType)) {
        return new $AutoMoshi_GenericExtendsGeneric_JsonAdapter<>(moshi, typeArgs);
      }
    }
    return null;
  }
}
