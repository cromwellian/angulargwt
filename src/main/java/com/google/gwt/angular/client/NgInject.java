package com.google.gwt.angular.client;

/**
 * Used to teach the code generators the associated name for a given type. So for example,
 * if you are trying to inject type T, this annotation will give its name. If basetype is specified,
 * this name will apply to all subtypes of T as well.
 */
public @interface NgInject {
  String name();

  Class<?> basetype() default ExactMatchOnly.class;

  static final class ExactMatchOnly {
  }
}
