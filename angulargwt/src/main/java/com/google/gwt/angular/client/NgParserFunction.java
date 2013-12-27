package com.google.gwt.angular.client;

/**
 * Used for writing directive parsers.
 */
public interface NgParserFunction<T> {
  Object parse(T value);
}
