package com.google.gwt.angular.client.impl;

import com.google.gwt.angular.client.JsNgParserFunction;
import com.google.gwt.angular.client.NgFormController;
import com.google.gwt.angular.client.NgModelController;
import com.google.gwt.core.client.JavaScriptObject;
import elemental.json.JsonObject;
import elemental.util.ArrayOf;

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
