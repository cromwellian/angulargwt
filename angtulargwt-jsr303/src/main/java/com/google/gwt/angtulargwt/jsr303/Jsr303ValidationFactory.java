package com.google.gwt.angtulargwt.jsr303;

import com.google.gwt.core.client.GWT;
import com.google.gwt.validation.client.AbstractGwtValidatorFactory;
import com.google.gwt.validation.client.GwtValidation;
import com.google.gwt.validation.client.impl.AbstractGwtValidator;
import javax.validation.Validator;

/**
 * Bootstrap the JSR-303 validator
 */
public class Jsr303ValidationFactory extends AbstractGwtValidatorFactory {
	
  @Override
  public AbstractGwtValidator createValidator() {
    return GWT.create(AngularValidator.class);
  }
  
}
