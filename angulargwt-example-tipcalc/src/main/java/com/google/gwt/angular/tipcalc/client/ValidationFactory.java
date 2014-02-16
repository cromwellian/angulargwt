package com.google.gwt.angular.tipcalc.client;

import javax.validation.Validator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.validation.client.AbstractGwtValidatorFactory;
import com.google.gwt.validation.client.GwtValidation;
import com.google.gwt.validation.client.impl.AbstractGwtValidator;

public class ValidationFactory extends AbstractGwtValidatorFactory {
		
		  @Override
		  public AbstractGwtValidator createValidator() {
		    return GWT.create(TipCalcValidator.class);
		  }
		  
		  @GwtValidation({TipModel.class})
		  public interface TipCalcValidator extends Validator {}
}
