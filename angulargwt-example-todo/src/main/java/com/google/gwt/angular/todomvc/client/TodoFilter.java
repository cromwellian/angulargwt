package com.google.gwt.angular.todomvc.client;

import com.google.gwt.angular.client.Filter;
import com.google.gwt.angular.client.NgInject;

import elemental.js.util.JsArrayOf;
import elemental.json.JsonValue;
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
        allMatch = allMatch && areEqual(todo.json().get(key),todoFilter.json().get(key));
      }
      if (allMatch) {
        result.push(todo);
      }
    }
    return result;
  }

private boolean areEqual(JsonValue val, JsonValue reference) {
	//FIXME this is a workaround which might result in decreased performance
	// Elemental boxing disturbes comparision as Object(false)!=false
	boolean res = (reference.toJson().equals(val.toJson()));
	return res;
}
}
