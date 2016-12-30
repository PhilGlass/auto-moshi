package test;

import com.squareup.moshi.JsonAdapter;
import glass.phil.auto.moshi.AutoMoshi;

class NonStaticFactory {
  @AutoMoshi.Factory abstract class Factory implements JsonAdapter.Factory {}
}
