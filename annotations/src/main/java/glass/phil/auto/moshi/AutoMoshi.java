package glass.phil.auto.moshi;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Applied to an {@link AutoValue} class to opt in to Moshi support.
 *
 * <pre><code>
 * &#64;AutoValue &#64;AutoMoshi abstract class Foo {
 *   abstract String a();
 * }
 * </code></pre>
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface AutoMoshi {
  /**
   * Applied to an abstract class that implements {@link JsonAdapter.Factory}, triggering the
   * generation of a package-private subclass. The generated subclass will be capable of creating
   * a {@link JsonAdapter} for any {@link AutoMoshi} annotated class.
   *
   * <pre><code>
   * &#64;AutoMoshi.Factory abstract class AdapterFactory implements JsonAdapter.Factory {
   *   static JsonAdapter.Factory create() {
   *     return new AutoMoshi_AdapterFactory();
   *   }
   * }
   * </code></pre>
   *
   * If the annotated class is nested, it must be static and must have at least package visibility.
   * The generated subclass will have a name of the form {@code AutoMoshi_Outer_Middle_Inner},
   * where {@code Inner} is the name of the annotated class.
   *
   * <pre><code>
   * class Outer {
   *   &#64;AutoMoshi.Factory static abstract class InnerAdapterFactory implements JsonAdapter.Factory {
   *     static JsonAdapter.Factory create() {
   *       return new AutoMoshi_Outer_InnerAdapterFactory();
   *     }
   *   }
   * }
   * </code></pre>
   */
  @Retention(RetentionPolicy.SOURCE)
  @Target(ElementType.TYPE)
  @interface Factory {}
}
