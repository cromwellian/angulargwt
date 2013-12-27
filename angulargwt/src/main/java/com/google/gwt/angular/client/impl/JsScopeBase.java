package com.google.gwt.angular.client.impl;

import com.google.gwt.angular.client.Scope;
import com.google.gwt.angular.client.WatchFunction;

/**
 * Base class of generated scopes.
 */
public class JsScopeBase<T extends Scope<T>> extends JsModelBase<T> implements Scope<T> {
  protected JsScopeBase() {
  }

  final public native <S> void $watch(String expr, WatchFunction<S> func, boolean objEq) /*-{
      this.$watch(expr, function (newVal) {
          func.@com.google.gwt.angular.client.WatchFunction::exec(Ljava/lang/Object;)(newVal);
      }, objEq);
  }-*/;

  final public <S> void $watch(String expr, WatchFunction<S> func) {
    $watch(expr, func, false);
  }

  final public native void $apply(String expr) /*-{
      this.$apply(expr);
  }-*/;
}
