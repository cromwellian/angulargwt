package com.google.gwt.angular.client;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Used when a object needs to be registered as a directive.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface NgDirective {
  String value();
  String templateUrl() default "";
  String restrict() default "AEC";
  boolean transclude() default false;
  boolean inlineTemplate() default true;
}
