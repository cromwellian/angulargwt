package com.google.gwt.angular.client;

import elemental.util.ArrayOf;

/**
 * Interface used to implement filter functions.
 */
public interface Filter<T> {
  ArrayOf<T> filter(ArrayOf<T> elts, T elt);
}
