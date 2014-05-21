package com.google.gwt.angular.todomvc.client;

import com.google.gwt.angular.client.*;

import elemental.json.JsonObject;
import elemental.json.JsonValue;
import elemental.util.ArrayOf;

/**
 * TodoMVC todo-focus directive.
 */
@NgDirective("todoFocus")
public class TodoFocusDirective implements Directive {
	@NgInjected
	public NgTimeout timeout;

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

	@Override
	public void link(Scope scope, ArrayOf<NgElement> element, JsonObject attrs) {
		focus((TodoScope) scope, element, attrs);
	}

	@Override
	public void init() {

	}
}
