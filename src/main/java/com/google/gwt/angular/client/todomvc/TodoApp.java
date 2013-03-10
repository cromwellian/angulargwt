package com.google.gwt.angular.client.todomvc;

import com.google.gwt.angular.client.AngularApp;
import com.google.gwt.core.client.GWT;

/**
 * Entry point for Angular TodoMvc App.
 */
public class TodoApp extends AngularApp {
  @Override
  protected void main() {
    GWT.create(TodoAppModule.class);
  }
}
