package com.google.gwt.angtulargwt.jsr303.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.validation.client.AbstractGwtValidatorFactory;
import com.google.gwt.validation.client.impl.AbstractGwtValidator;

/**
 * Bootstrap the JSR-303 validator
 */
public class Jsr303ValidationFactory extends AbstractGwtValidatorFactory {
	
  @Override
  public AbstractGwtValidator createValidator() {
    return GWT.create(AngularValidatorImpl.class);
  }
  
}
