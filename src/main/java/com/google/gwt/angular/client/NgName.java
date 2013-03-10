package com.google.gwt.angular.client;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used when a given function or object needs to be given a name in Javascript for registration.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface NgName {
  String value();
}
