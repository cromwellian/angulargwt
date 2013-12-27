package com.google.gwt.angular.client.impl;

import com.google.gwt.angular.client.CanBeJson;
import com.google.gwt.angular.client.NgElement;
import com.google.gwt.core.client.JavaScriptObject;
import elemental.js.dom.JsElement;
import elemental.js.json.JsJsonObject;

/**
 * JSO implementation of NgElement.
 */
public class JsCanBeJson extends JavaScriptObject implements CanBeJson {
  protected JsCanBeJson() {
  }

  final public native JsJsonObject json() /*-{
     return this;
  }-*/;
}
