package com.google.gwt.angular.client.impl;

import com.google.gwt.angular.client.NgElement;
import elemental.js.dom.JsElement;

/**
 * JSO implementation of NgElement.
 */
public class JsNgElement extends JsElement implements NgElement {
  protected JsNgElement() {
  }

  final public native void bind(String eventType, Runnable func) /*-{
      this.bind(eventType, function () {
          func.@java.lang.Runnable::run()();
      });
  }-*/;

  final public native <T> T inheritedData(String dataType) /*-{
      return this.inheritedData(dataType);
  }-*/;
}
