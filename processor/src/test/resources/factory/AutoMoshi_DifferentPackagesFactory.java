package test;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import different.$AutoMoshi_DifferentPackageGeneric_JsonAdapter;
import different.$AutoMoshi_DifferentPackage_JsonAdapter;
import java.lang.Class;
import java.lang.Override;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

final class AutoMoshi_DifferentPackagesFactory extends DifferentPackagesFactory {
  @Override public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
    if (type instanceof Class) {
      final Class<?> clazz = (Class<?>) type;
      if (Simple.class.equals(clazz)) {
        return new $AutoMoshi_Simple_JsonAdapter(moshi);
      }
      if ("different.DifferentPackage".equals(clazz.getCanonicalName())) {
        return new $AutoMoshi_DifferentPackage_JsonAdapter(moshi);
      }
    }
    if (type instanceof ParameterizedType) {
      final Class<?> rawType = (Class<?>) ((ParameterizedType) type).getRawType();
      final Type[] typeArgs = ((ParameterizedType) type).getActualTypeArguments();
      if (Generic.class.equals(rawType)) {
        return new $AutoMoshi_Generic_JsonAdapter<>(moshi, typeArgs);
      }
      if ("different.DifferentPackageGeneric".equals(rawType.getCanonicalName())) {
        return new $AutoMoshi_DifferentPackageGeneric_JsonAdapter<>(moshi, typeArgs);
      }
    }
    return null;
  }
}
