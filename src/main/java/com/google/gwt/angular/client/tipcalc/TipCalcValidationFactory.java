package com.google.gwt.angular.client.tipcalc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.validation.client.AbstractGwtValidatorFactory;
import com.google.gwt.validation.client.GwtValidation;
import com.google.gwt.validation.client.impl.AbstractGwtValidator;

import javax.validation.Validation;
import javax.validation.Validator;

/**
 * Bootstrap the JSR-303 validator
 */
public class TipCalcValidationFactory extends AbstractGwtValidatorFactory {
  @Override
  public AbstractGwtValidator createValidator() {
    return GWT.create(GwtValidator.class);
  }

  @GwtValidation(TipModel.class)
  public interface GwtValidator extends Validator {
  }
}
