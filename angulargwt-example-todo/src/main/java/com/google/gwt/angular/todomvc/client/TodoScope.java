package com.google.gwt.angular.todomvc.client;

import com.google.gwt.angular.client.Location;
import com.google.gwt.angular.client.Scope;
import elemental.util.ArrayOf;

/**
 * TodoScope declaration.  Follows the {@link com.google.gwt.angular.client.Model} spec for setting up fields on the scope object.
 */
public interface TodoScope extends Scope<TodoScope> {
  int remainingCount();

  TodoScope remainingCount(int count);

  int doneCount();

  TodoScope doneCount(int count);

  boolean allChecked();

  TodoScope allChecked(boolean b);

  Todo editedTodo();

  TodoScope editedTodo(Todo editedTodo);

  String newTodo();

  TodoScope newTodo(String newTodo);

  ArrayOf<Todo> todos();

  TodoScope todos(ArrayOf<Todo> items);

  TodoScope statusFilter(Todo statusFilter);

  TodoScope location(Location location);
}
