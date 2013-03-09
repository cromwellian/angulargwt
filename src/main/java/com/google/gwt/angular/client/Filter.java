package com.google.gwt.angular.client;

import elemental.util.ArrayOf;

/**
 * Created with IntelliJ IDEA.
 * User: cromwellian
 * Date: 3/9/13
 * Time: 1:46 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Filter<T> {
  ArrayOf<T> filter(ArrayOf<T> elts, T elt);
}
