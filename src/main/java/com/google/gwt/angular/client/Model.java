package com.google.gwt.angular.client;

import elemental.json.JsonObject;

/**
 * Model classes are essentially ways of automatically creating Javascript Object backed
 * Javabeans via interfaces. Declare getters and setters according to the JavaBean pattern,
 * with or without 'fluent' setters.
 * <p/>
 * Create new instances via {@code GWT.create(MyModel.class)}.
 */
public interface Model<T extends Model<T>> {
  JsonObject json();
}
