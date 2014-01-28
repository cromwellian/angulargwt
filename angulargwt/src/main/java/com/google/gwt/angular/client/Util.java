package com.google.gwt.angular.client;

import java.util.Iterator;

import elemental.json.JsonValue;
import elemental.util.ArrayOf;

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
  
  public static <S> Iterable<S> iterable(final ArrayOf<S> array) {
	    return new Iterable<S>() {
	      public Iterator<S> iterator() {
	        return new Iterator<S>() {
	          int next = 0;

	          public boolean hasNext() {
	            return next < array.length();
	          }

	          public S next() {
	            return array.get(next++);
	          }

	          public void remove() {
	          }
	        };
	      }
	    };
	  }
}
