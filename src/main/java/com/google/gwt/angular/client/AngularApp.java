package com.google.gwt.angular.client;

import com.google.gwt.core.client.EntryPoint;
import elemental.client.Browser;
import elemental.dom.TimeoutHandler;
import elemental.events.Event;
import elemental.events.EventListener;
import elemental.html.ScriptElement;
import elemental.html.Window;
import elemental.js.html.JsWindow;

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
        main();
        getModuleWindow().setTimeout(new TimeoutHandler() {
          public void onTimeoutHandler() {
            bootstrap();
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
  protected abstract void main();

  private native void bootstrap() /*-{
      $wnd.angular.bootstrap($doc, ['todomvc']);
  }-*/;
}
