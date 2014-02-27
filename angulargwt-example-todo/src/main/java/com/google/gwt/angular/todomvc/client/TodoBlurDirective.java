package com.google.gwt.angular.todomvc.client;

import com.google.gwt.angular.client.Directive;
import com.google.gwt.angular.client.NgDirective;
import com.google.gwt.angular.client.NgElement;
import com.google.gwt.angular.client.Scope;

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

	@Override
	public void link(Scope scope, ArrayOf<NgElement> element, JsonObject attrs) {
		blur((TodoScope) scope,element.get(0),attrs);
	}

	@Override
	public void init() {

	}
}
