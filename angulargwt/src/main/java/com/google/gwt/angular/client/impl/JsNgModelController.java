package com.google.gwt.angular.client.impl;

import com.google.gwt.angular.client.JsNgParserFunction;
import com.google.gwt.angular.client.NgModelController;

import elemental.json.JsonObject;
import elemental.util.ArrayOf;

/**
 * Native implementation of NgModelController
 */
public class JsNgModelController extends JsCanBeJson implements NgModelController {
  protected JsNgModelController() {
  }

  final public native void $render() /*-{
    this.$render();
  }-*/;

  final public native void $setValidity(String validationErrorKey, boolean isValid) /*-{
   this.$setValidity(validationErrorKey, isValid);
  }-*/;

  final public native void $setViewValue(String value) /*-{
   this.$setViewValue(value);
  }-*/;

  final public native JsonObject $modelValue() /*-{
    return this.$modelValue;
  }-*/;

  final public native ArrayOf<JsNgParserFunction> $parsers() /*-{
    return this.$parsers;
  }-*/;

  final public native JsonObject $error() /*-{
    return this.$error;
  }-*/;

  final public native boolean $pristine() /*-{
    return this.$pristine;
  }-*/;

  final public native boolean $dirty() /*-{
    return  this.$dirty;
  }-*/;

  final public native boolean $valid() /*-{
    return this.$valid;
  }-*/;

  final public native boolean $invalid() /*-{
    return this.$invalid;
  }-*/;
}
