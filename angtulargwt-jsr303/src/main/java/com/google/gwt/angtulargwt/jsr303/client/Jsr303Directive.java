package com.google.gwt.angtulargwt.jsr303.client;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.google.gwt.angular.client.Directive;
import com.google.gwt.angular.client.Model;
import com.google.gwt.angular.client.NgDirective;
import com.google.gwt.angular.client.NgElement;
import com.google.gwt.angular.client.Scope;
import com.google.gwt.angular.client.WatchFunction;
import com.google.gwt.validation.client.impl.Validation;

import elemental.client.Browser;
import elemental.js.json.JsJsonObject;
import elemental.json.JsonObject;
import elemental.util.ArrayOf;

/**
 * Adds JSR-303 validator support.
 */
@NgDirective("beanValidate")
public class Jsr303Directive implements Directive {
	private Validator validator;

	@Override
	public void init() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	@Override
	public void link(Scope scope, ArrayOf<NgElement> element, JsonObject attrs) {
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
