package com.google.gwt.angular.client.impl;

import com.google.gwt.angular.client.Scope;
import com.google.gwt.angular.client.WatchFunction;

/**
 * Base class of generated scopes.
 */
public class JsScopeBase<T extends Scope<T>> extends JsModelBase<T> implements Scope<T> {
  protected JsScopeBase() {
  }

  final public native <S> void $watch(String expr, WatchFunction<S> func) /*-{
      this.$watch(expr, function (newVal) {
          func.@com.google.gwt.angular.client.WatchFunction::exec(Ljava/lang/Object;)(newVal);
      });
  }-*/;

  final public native void $apply(String expr) /*-{
      this.$apply(expr);
  }-*/;
}
