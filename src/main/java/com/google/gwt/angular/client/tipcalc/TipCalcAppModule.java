package com.google.gwt.angular.client.tipcalc;

import com.google.gwt.angular.client.AngularModule;
import com.google.gwt.angular.client.NgDepends;
import com.google.gwt.angular.client.NgInject;
import com.google.gwt.angular.client.NgName;

/**
 * Angular Module Declaring our dependencies.
 */
@NgName("tipcalc")
@NgDepends({TipCalcController.class, Jsr303Directive.class})
public class TipCalcAppModule implements AngularModule {
}
