package com.google.gwt.angular.client.todomvc;

import com.google.gwt.angular.client.AngularModule;
import com.google.gwt.angular.client.NgDepends;
import com.google.gwt.angular.client.NgName;

@NgName("todomvc")
@NgDepends({TodoStorage.class, TodoFilter.class, TodoController.class})
public class TodoAppModule implements AngularModule {

}
