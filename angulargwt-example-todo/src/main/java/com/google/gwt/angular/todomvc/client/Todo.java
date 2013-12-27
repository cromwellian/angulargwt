package com.google.gwt.angular.todomvc.client;

import com.google.gwt.angular.client.Model;

/**
 * Model object for individual Todo entry.
 */
public interface Todo extends Model<Todo> {
  String getTitle();

  Todo setTitle(String title);

  boolean getCompleted();

  Todo setCompleted(boolean b);
}
