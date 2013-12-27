package com.google.gwt.angular.todomvc.client;

import com.google.gwt.angular.client.NgInject;
import elemental.client.Browser;
import elemental.js.util.JsArrayOf;
import elemental.js.util.Json;
import elemental.util.ArrayOf;

import static com.google.gwt.angular.client.Util.reinterpret_cast;
import static com.google.gwt.angular.client.Util.toJson;

/**
 * Injected service for storage.
 */
@NgInject(name = "todoStorage")
public class TodoStorage {
  private static final String STORAGE_ID = "todos-angularjs";

  public ArrayOf<Todo> get() {
    ArrayOf<Todo> todos = reinterpret_cast(Json.parse(Browser.getWindow().getLocalStorage().getItem
        (STORAGE_ID)));
    return todos == null ? JsArrayOf.<Todo>create() : todos;
  }

  public void put(ArrayOf<Todo> todos) {
    Browser.getWindow().getLocalStorage().setItem(STORAGE_ID, toJson(todos));
  }
}
