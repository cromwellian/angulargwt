package com.google.gwt.angular.tipcalc.client;

import com.google.gwt.angtulargwt.jsr303.client.Jsr303Module;
import com.google.gwt.angular.client.AngularApp;
import com.google.gwt.angular.client.AngularModule;
import com.google.gwt.core.client.GWT;

/**
 * TipCalculator demonstrating JSR-303 validation integration.
 */
public class TipCalcApp extends AngularApp {
  @Override
  protected AngularModule[] main() {
    return new AngularModule[] {(AngularModule) GWT.create(TipCalcAppModule.class),(AngularModule) GWT.create(Jsr303Module.class)};
  }
}
