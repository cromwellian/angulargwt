package com.google.gwt.angular.client;

import com.google.gwt.core.client.EntryPoint;
import elemental.client.Browser;
import elemental.events.Event;
import elemental.events.EventListener;
import elemental.html.ScriptElement;

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
    Browser.getDocument().getHead().appendChild(se);
    se.setOnload(new EventListener() {
      public void handleEvent(Event evt) {
        main();
        bootstrap();
      }
    });
  }

  /**
   * Override this and invoke GWT.create() on your modules.
   */
  protected abstract void main();

  private native void bootstrap() /*-{
      $wnd.angular.bootstrap($doc);
  }-*/;
}
