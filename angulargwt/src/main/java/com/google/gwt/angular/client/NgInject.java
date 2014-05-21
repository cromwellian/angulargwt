package com.google.gwt.angular.client;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to teach the code generators the associated name for a given type. So for example,
 * if you are trying to inject type T, this annotation will give its name. If basetype is specified,
 * this name will apply to all subtypes of T as well.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NgInject {
  String name();

  Class<?> basetype() default ExactMatchOnly.class;

  static final class ExactMatchOnly {
  }
}
