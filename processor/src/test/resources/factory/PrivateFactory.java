package test;

import com.squareup.moshi.JsonAdapter;
import glass.phil.auto.moshi.AutoMoshi;

class PrivateFactory {
  @AutoMoshi.Factory private static abstract class Factory implements JsonAdapter.Factory {}
}
