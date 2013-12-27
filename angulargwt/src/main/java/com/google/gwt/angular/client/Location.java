package com.google.gwt.angular.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * The angular.js $location object.
 */
@NgInject(name = "$location")
final public class Location extends JavaScriptObject {
  protected Location() {
  }

  public native String path() /*-{
      return this.path();
  }-*/;

  public native void path(String p) /*-{
      return this.path(p);
  }-*/;
}
