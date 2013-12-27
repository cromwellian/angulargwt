package com.google.gwt.angular.todomvc.client;

import com.google.gwt.angular.client.*;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import elemental.util.ArrayOf;

/**
 * TodoMVC todo-focus directive.
 */
public class TodoFocusDirective implements Directive {
  private NgTimeout timeout;

  /**
   * Called to supply dependency injection for todo-focus.
   */

  public void onFocus(NgTimeout timeout) {
    this.timeout = timeout;
  }

  @NgDirective("todoFocus")
  public void focus(TodoScope scope, final ArrayOf<NgElement> element, JsonObject attrs) {
    scope.$watch(attrs.getString("todoFocus"), new WatchFunction<JsonValue>() {
      public void exec(JsonValue value) {
        if (!value.asBoolean()) {
          timeout.schedule(new Runnable() {

            public void run() {
              element.get(0).focus();
            }
          }, 0, false);
        }
      }
    });
  }
}
