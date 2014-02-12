package com.google.gwt.angtulargwt.jsr303.client;

import com.google.gwt.angular.client.AngularModule;
import com.google.gwt.angular.client.NgDepends;
import com.google.gwt.angular.client.NgName;

/**
 * This is your Module, injectable via the given mnemonic
 * add all exposed Components (Services and Directives) to the Depends-Annotation
 *
 */
@NgName("jsr303")
@NgDepends({Jsr303Directive.class})
public class Jsr303Module implements AngularModule {

}