package glass.phil.auto.moshi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface AutoMoshi {
  @Retention(RetentionPolicy.SOURCE)
  @Target(ElementType.TYPE)
  @interface Factory {}
}
