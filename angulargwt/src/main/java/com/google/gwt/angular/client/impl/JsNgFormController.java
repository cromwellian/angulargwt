package com.google.gwt.angular.client.impl;

import com.google.gwt.angular.client.NgFormController;
import com.google.gwt.angular.client.NgModelController;

/**
 * Native implementation of NgModelController
 */
public class JsNgFormController extends JsCanBeJson implements NgFormController {

  protected JsNgFormController() {
  }

  final public native NgModelController getModelController(String key) /*-{
    return this[key];
  }-*/;
}
