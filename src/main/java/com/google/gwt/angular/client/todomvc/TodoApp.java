package com.google.gwt.angular.client.todomvc;

import com.google.gwt.angular.client.AngularApp;
import com.google.gwt.angular.client.Factory;
import com.google.gwt.angular.client.Util;
import com.google.gwt.core.client.GWT;

import static com.google.gwt.angular.client.Util.make;

/**
 * Entry point for Angular TodoMvc App.
 */
public class TodoApp extends AngularApp {
  @Override
  protected void main() {
    // setup module
    TodoScope scope = make(GWT.create(TodoScope.class));
  }
}
