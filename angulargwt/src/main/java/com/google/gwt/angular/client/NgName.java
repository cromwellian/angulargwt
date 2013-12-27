package com.google.gwt.angular.client;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Used when a given function or object needs to be given a name in Javascript for registration.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface NgName {
  String value();
}
