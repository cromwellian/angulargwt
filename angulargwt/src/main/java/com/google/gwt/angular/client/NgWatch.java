package com.google.gwt.angular.client;

/**
 * Tags a function in a controller to be automatically registered via $scope.$watch using
 * the given expression and optional deep object equality.
 */
public @interface NgWatch {
  boolean objEq() default false;

  String value();
}
