package com.google.gwt.angular.client;

/**
 * Represents Angular form controller.
 */
@NgInject(name = "form")
public interface NgFormController extends CanBeJson {
  NgModelController getModelController(String key);
}
