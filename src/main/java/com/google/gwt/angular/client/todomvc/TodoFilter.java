package com.google.gwt.angular.client.todomvc;

import com.google.gwt.angular.client.Filter;
import com.google.gwt.angular.client.NgInject;
import elemental.js.util.JsArrayOf;
import elemental.util.ArrayOf;

/**
 * Injectable filter.
 */
@NgInject(name = "todoFilter")
public class TodoFilter implements Filter<Todo> {
  public ArrayOf<Todo> filter(ArrayOf<Todo> todos, Todo todoFilter) {
    ArrayOf<Todo> result = JsArrayOf.create();
    for (int i = 0; i < todos.length(); i++) {
      Todo todo = todos.get(i);
      boolean allMatch = true;
      for (String key : todoFilter.json().keys()) {
        allMatch = allMatch && todo.json().get(key) == todoFilter.json().get(key);
      }
      if (allMatch) {
        result.push(todo);
      }
    }
    return result;
  }
}
