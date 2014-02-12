package com.google.gwt.angtulargwt.jsr303.client;

import com.google.gwt.angular.client.*;
import com.google.gwt.validation.client.impl.Validation;
import elemental.client.Browser;
import elemental.dom.TimeoutHandler;
import elemental.js.json.JsJsonObject;
import elemental.json.JsonObject;
import elemental.util.ArrayOf;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

/**
 * Adds JSR-303 validator support.
 */
public class Jsr303Directive implements Directive {
  private Validator validator;

  public void onValid() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @NgDirective("beanValidate")
  public void valid(final Scope scope, final ArrayOf<NgElement> element, final JsonObject attrs,
                    final NgFormController ctrl) {
    final NgFormController ctrl2 = ctrl;
    scope.$watch(attrs.getString("beanValidate"), new WatchFunction<Model>() {
      public void exec(Model value) {
    	  Browser.getWindow().getConsole().log("Validating " + value.json().toJson());
        Set<ConstraintViolation<Model>> violations = validator.validate(value);
        JsonObject obj = JsJsonObject.create();
        value.json().put("$beanErrors", obj);
        if (!violations.isEmpty()) {
          for (ConstraintViolation<Model> viol : violations) {
        	  Browser.getWindow().getConsole().log("Found violation:" + viol.getPropertyPath().toString() + ": " + viol.getMessage());
             obj.put(viol.getPropertyPath().toString(), viol.getMessage());
          }
        }
      }
    }, true);
  }
}
