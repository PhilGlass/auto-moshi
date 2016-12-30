package test;

import com.squareup.moshi.JsonAdapter;
import glass.phil.auto.moshi.AutoMoshi;

class NestedFactory {
  @AutoMoshi.Factory static abstract class Factory implements JsonAdapter.Factory {}
}
