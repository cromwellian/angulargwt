package com.google.gwt.angular.todomvc.client;

import static com.google.gwt.angular.client.Util.make;

import com.google.gwt.angular.client.AngularController;
import com.google.gwt.angular.client.Location;
import com.google.gwt.angular.client.NgInject;
import com.google.gwt.angular.client.NgInjected;
import com.google.gwt.angular.client.NgWatch;
import com.google.gwt.core.client.GWT;

import elemental.util.ArrayOf;

@NgInject(name = "TodoCtrl")
public class TodoController extends AngularController<TodoScope> {

	private ArrayOf<Todo> todos;
	
	@NgInjected	public Location location;
	@NgInjected	public TodoStorage store;
	@NgInjected	public TodoFilter todoFilter;

	@Override
	protected void initialize(TodoScope scope) {
		scope.newTodo("")
		.editedTodo(null)
		.location(location);
		this.todos = store.get();
		scope.todos(todos);
		if ("".equals(location.path())) {
			location.path("/");
		}
	}

	@NgWatch(value = "todos", objEq = true)
	public void $watchTodos() {
		Todo todoPredicate = makeTodo();
		todoPredicate.setCompleted(false);
		scope.remainingCount(todoFilter.filter(todos, todoPredicate).length())
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

	//Note: you can leave this out and do the filtering on the template
	// todos | todo:statusFilter (as TodoFilter is in fact the filterFilter, you can also use filter)
	//performance could be better if filtered in controller (less watches)
	@NgWatch("statusFilter")
	public void $watchStatusFilter(Todo statusFilter) {
		scope.todos(todoFilter.filter(todos,statusFilter));
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
		scope.todos(todos);
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
		todos.remove(todo);
	}

	public void markAll(boolean done) {
		for (Todo todo : iterable(todos)) {
			todo.setCompleted(done);
		}
	}

	public void clearDoneTodos() {
		Todo todoPredicate = makeTodo();
		todoPredicate.setCompleted(false);

		ArrayOf<Todo> result = todoFilter.filter(todos, todoPredicate);

		todos=result;
		scope.todos(result);
	}
}
