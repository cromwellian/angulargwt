package com.google.gwt.angular.client;

import elemental.json.JsonObject;
import elemental.util.ArrayOf;

/**
 * Angular's builtin NgModelController
 */
@NgInject(name = "ngModel")
public interface NgModelController {
  void $render();
  void $setValidity(String validationErrorKey, boolean isValid);
  void $setViewValue(String value);
  JsonObject $modelValue();
  ArrayOf<JsNgParserFunction> $parsers();
  JsonObject $error();
  boolean $pristine();
  boolean $dirty();
  boolean $valid();
  boolean $invalid();
}
