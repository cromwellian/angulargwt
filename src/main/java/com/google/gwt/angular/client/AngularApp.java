package com.google.gwt.angular.client;

import com.google.gwt.angular.client.impl.AngularModuleBase;
import com.google.gwt.core.client.EntryPoint;
import elemental.client.Browser;
import elemental.dom.TimeoutHandler;
import elemental.events.Event;
import elemental.events.EventListener;
import elemental.html.ScriptElement;
import elemental.js.html.JsWindow;
import elemental.js.util.JsArrayOfString;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public abstract class AngularApp implements EntryPoint {

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    ScriptElement se = Browser.getDocument().createScriptElement();
    se.setSrc("angular.js");
    se.setType("text/javascript");
    getModuleWindow().getDocument().getBody().appendChild(se);
    se.setOnload(new EventListener() {
      public void handleEvent(Event evt) {
        unbind();
        final AngularModule[] modules = main();
        final JsArrayOfString moduleNames = JsArrayOfString.create();
        for (int i = 0; i < modules.length; i++) {
          moduleNames.push(((AngularModuleBase) modules[i]).moduleName());
        }
        getModuleWindow().setTimeout(new TimeoutHandler() {
          public void onTimeoutHandler() {
            bootstrap(moduleNames);
          }
        }, 500);
      }

      private native void unbind() /*-{
          $wnd.angular.element(document).unbind('DOMContentLoaded');
      }-*/;
    });
  }

  private native JsWindow getModuleWindow() /*-{
      return window;
  }-*/;

  /**
   * Override this and invoke GWT.create() on your modules.
   */
  protected abstract AngularModule[] main();

  private native void bootstrap(JsArrayOfString moduleNames) /*-{
      $wnd.angular.bootstrap($doc, moduleNames);
  }-*/;
}
