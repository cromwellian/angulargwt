package com.google.gwt.angular.client.impl;

import com.google.gwt.angular.client.NgTimeout;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * JSO implementation of NgTimeout.
 */
public class JsNgTimeout extends JavaScriptObject implements NgTimeout {
  protected JsNgTimeout() {
  }

  final public native void schedule(Runnable func, int millis, boolean flag) /*-{
      this(function () {
          func.@java.lang.Runnable::run()();
      }, millis, flag);
  }-*/;
}
