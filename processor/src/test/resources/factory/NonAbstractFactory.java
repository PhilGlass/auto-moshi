package test;

import com.squareup.moshi.JsonAdapter;
import glass.phil.auto.moshi.AutoMoshi;

@AutoMoshi.Factory class NonAbstractFactory implements JsonAdapter.Factory {}
