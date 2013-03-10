package com.google.gwt.angular.client;

import elemental.json.JsonObject;

/**
 * Model classes are essentially ways of automatically creating Javascript Object backed
 * Javabeans via interfaces. Declare getters and setters according to the JavaBean pattern,
 * with or without 'fluent' setters, or omit the 'get' and 'set' prefixes.
 * <p/>
 * Create new instances via {@code Util.make(GWT.create(MyModel.class))}.
 */
public interface Model<T extends Model<T>> {
  JsonObject json();
}
