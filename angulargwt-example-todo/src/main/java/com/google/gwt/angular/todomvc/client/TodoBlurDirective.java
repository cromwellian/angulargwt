package com.google.gwt.angular.todomvc.client;

import com.google.gwt.angular.client.Directive;
import com.google.gwt.angular.client.NgDirective;
import com.google.gwt.angular.client.NgElement;
import elemental.json.JsonObject;

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
