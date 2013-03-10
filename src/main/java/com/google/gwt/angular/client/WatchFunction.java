package com.google.gwt.angular.client;

import elemental.json.JsonValue;

/**
 * Function called when a watch event triggers.
 */
public interface WatchFunction<T> {
  void exec(T value);
}
