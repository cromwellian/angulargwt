package com.google.gwt.angular.tipcalc.client;

import com.google.gwt.angular.client.AngularModule;
import com.google.gwt.angular.client.NgDepends;
import com.google.gwt.angular.client.NgInject;
import com.google.gwt.angular.client.NgName;

/**
 * Angular Module Declaring our dependencies.
 */
@NgName("tipcalc")
@NgDepends({TipCalcController.class})
public class TipCalcAppModule implements AngularModule {
}
