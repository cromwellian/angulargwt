package com.google.gwt.angular.client.todomvc;

import com.google.gwt.angular.client.*;
import com.google.gwt.core.client.GWT;
import elemental.js.util.JsArrayOf;
import elemental.util.ArrayOf;

import static com.google.gwt.angular.client.Util.make;

@NgInject(name = "TodoCtrl")
public class TodoController extends AngularController<TodoScope> {

  private ArrayOf<Todo> todos;
  private Location location;
  private TodoStorage store;
  private TodoFilter filterFilter;

  public void onInit(TodoScope scope, Location location, TodoStorage store, TodoFilter filter) {
    this.location = location;
    this.store = store;
    this.filterFilter = filter;
    scope.newTodo("")
        .editedTodo(null)
        .location(location);
    this.todos = store.get();
    scope.todos(todos);
    if ("".equals(location.path())) {
      location.path("/");
    }
  }

  @NgWatch("todos")
  public void $watchTodos() {
    Todo todoPredicate = makeTodo();
    todoPredicate.setCompleted(false);
    scope.remainingCount(filterFilter.filter(todos, todoPredicate).length())
        .doneCount(todos.length() - scope.remainingCount())
        .allChecked(scope.remainingCount() != 0);
    store.put(todos);
  }

  @NgWatch("location.path()")
  public void $watchPath(String path) {
    scope.statusFilter("/active".equals(path) ?
        makeTodo().setCompleted(false) :
        "/completed".equals(path) ?
            makeTodo().setCompleted(true) : null);
  }

  private Todo makeTodo() {
    return make(GWT.create(Todo.class));
  }

  public void addTodo() {
    if (scope.newTodo().length() == 0) {
      return;
    }
    Todo newTodo = makeTodo();
    newTodo.setTitle(scope.newTodo())
        .setCompleted(false);
    todos.push(newTodo);
    scope.newTodo("");
  }

  public void doneEditing(Todo todo) {
    scope.editedTodo(null);
    if ("".equals(todo.getTitle())) {
      removeTodo(todo);
    }
  }

  public void editTodo(Todo todo) {
    scope.editedTodo(todo);
  }

  public void removeTodo(Todo todo) {
    todos.splice(todos.indexOf(todo), 1);
  }

  public void markAll(boolean done) {
    for (Todo todo : iterable(todos)) {
      todo.setCompleted(done);
    }
  }

  public void clearDoneTodos() {
    ArrayOf<Todo> result = JsArrayOf.create();
    for (Todo todo : iterable(todos)) {
      if (!todo.getCompleted()) {
        result.push(todo);
      }
    }
    scope.todos(result);
  }
}
