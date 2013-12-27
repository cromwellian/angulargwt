package com.google.gwt.angular.todomvc.client;

import com.google.gwt.angular.client.AngularModule;
import com.google.gwt.angular.client.NgDepends;
import com.google.gwt.angular.client.NgName;

/**
 * Angular Module Declaring our dependencies.
 */
@NgName("todomvc")
@NgDepends({TodoStorage.class, TodoFilter.class, TodoController.class, TodoBlurDirective.class,
    TodoFocusDirective.class})
public class TodoAppModule implements AngularModule {

}
