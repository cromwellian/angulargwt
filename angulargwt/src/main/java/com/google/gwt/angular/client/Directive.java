package com.google.gwt.angular.client;

import elemental.json.JsonObject;
import elemental.util.ArrayOf;

/**
 * Interface to implement directives. Needs to be annotated with a {@link NgDirective} annotation
 * and is published to the module that depends on it.
 */
public interface Directive {
	public void link(final Scope scope, final ArrayOf<NgElement> element, final JsonObject attrs);
	public void init();
}
