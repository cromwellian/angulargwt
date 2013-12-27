package com.google.gwt.angular.client;

/**
 * Used to obtain Javascript overlay types produced by generators.
 */
public interface Factory<T> {
  T create();
}
