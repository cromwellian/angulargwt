package com.google.gwt.angular.client.todomvc;

import com.google.gwt.angular.client.Location;
import com.google.gwt.angular.client.Scope;
import elemental.util.ArrayOf;

/**
 * TodoScope declaration.  Follows the {@link com.google.gwt.angular.client.Model} spec for setting up fields on the scope object.
 */
public interface TodoScope extends Scope<TodoScope> {
  int getRemainingCount();

  TodoScope setRemainingCount(int count);

  int getDoneCount();

  TodoScope setDoneCount(int count);

  boolean getAllChecked();

  TodoScope setAllChecked(boolean b);

  Todo getEditedTodo();

  TodoScope setEditedTodo(Todo editedTodo);

  String getNewTodo();

  TodoScope setNewTodo(String newTodo);

  ArrayOf<Todo> getTodos();

  TodoScope setTodos(ArrayOf<Todo> items);

  TodoScope setStatusFilter(Todo statusFilter);

  TodoScope setLocation(Location location);
}
