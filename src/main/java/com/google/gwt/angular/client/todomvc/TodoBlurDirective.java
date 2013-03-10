package com.google.gwt.angular.client.todomvc;

import com.google.gwt.angular.client.*;
import elemental.json.JsonObject;
import elemental.util.ArrayOf;

/**
 * TodoMVC todo-blur directive.
 */
public class TodoBlurDirective implements Directive {

  @NgDirective("todoBlur")
  public void blur(final TodoScope scope, final NgElement element,
                  final JsonObject attrs) {
    element.bind("blur", new Runnable() {
      public void run() {
        scope.$apply(attrs.getString("todoBlur"));
      }
    });
  }
}
