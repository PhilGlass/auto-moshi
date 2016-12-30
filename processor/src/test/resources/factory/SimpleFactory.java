package test;

import com.squareup.moshi.JsonAdapter;
import glass.phil.auto.moshi.AutoMoshi;

@AutoMoshi.Factory abstract class SimpleFactory implements JsonAdapter.Factory {}
