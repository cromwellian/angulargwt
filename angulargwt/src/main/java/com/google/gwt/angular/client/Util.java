package com.google.gwt.angular.client;

import elemental.json.JsonValue;

public class Util {
  /**
   * Only safe to do with JavaScriptObjects.
   */
  public static native <T> T reinterpret_cast(Object o) /*-{
      return o;
  }-*/;

  public static String toJson(Object o) {
    JsonValue value = reinterpret_cast(o);
    return value.toJson();
  }

  public static <T> T make(Object factory) {
    return ((Factory<T>) factory).create();
  }
}
