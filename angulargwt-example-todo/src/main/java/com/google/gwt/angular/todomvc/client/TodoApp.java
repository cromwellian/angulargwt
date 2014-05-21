package com.google.gwt.angular.todomvc.client;

import com.google.gwt.angular.client.AngularApp;
import com.google.gwt.angular.client.AngularModule;
import com.google.gwt.core.client.GWT;

/**
 * Entry point for Angular TodoMvc App.
 */
public class TodoApp extends AngularApp {
  @Override
  protected AngularModule[] main() {
    return new AngularModule[] {(AngularModule) GWT.create(TodoAppModule.class)};
  }
}
