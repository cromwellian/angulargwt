package com.google.gwt.angular.client;

/**
 * Function called when a watch event triggers.
 */
public interface WatchFunction<T> {
  void exec(T value);
}
