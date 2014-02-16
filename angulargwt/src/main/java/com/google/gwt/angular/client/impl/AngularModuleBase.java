package com.google.gwt.angular.client.impl;

import com.google.gwt.angular.client.AngularModule;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.query.client.impl.ConsoleBrowser;

/**
 * Abstract Module base class for implementations.
 */
public abstract class AngularModuleBase implements AngularModule {
	public abstract String moduleName();
}
