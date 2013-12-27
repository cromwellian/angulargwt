package com.google.gwt.angular.client;

import com.google.gwt.angular.client.impl.AngularModuleBase;
import com.google.gwt.core.client.EntryPoint;
import elemental.js.util.JsArrayOfString;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public abstract class AngularApp implements EntryPoint {

  /**
   * This is the entry point method.
   */
  public final void onModuleLoad() {
    final AngularModule[] modules = main();
    final JsArrayOfString moduleNames = JsArrayOfString.create();
    for (int i = 0; i < modules.length; i++) {
      moduleNames.push(((AngularModuleBase) modules[i]).moduleName());
    }
    bootstrap(moduleNames);
  }

  /**
   * Override this and invoke GWT.create() on your modules.
   */
  protected abstract AngularModule[] main();

  private native void bootstrap(JsArrayOfString moduleNames) /*-{
      $wnd.angular.bootstrap($doc, moduleNames);
  }-*/;
}
