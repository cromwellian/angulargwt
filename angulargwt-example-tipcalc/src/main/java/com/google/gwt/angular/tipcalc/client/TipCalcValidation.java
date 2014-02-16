package com.google.gwt.angular.tipcalc.client;

import com.google.gwt.angtulargwt.jsr303.client.NgValidate;
import com.google.gwt.angtulargwt.jsr303.client.NgValidator;

@NgValidate(TipModel.class)
public interface TipCalcValidation extends NgValidator {
}
