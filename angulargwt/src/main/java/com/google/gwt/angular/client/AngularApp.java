package com.google.gwt.angular.client;

import com.google.gwt.angular.client.impl.AngularModuleBase;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.ScriptInjector;

import elemental.client.Browser;
import elemental.js.util.JsArrayOfString;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public abstract class AngularApp implements EntryPoint {

	private static final String ANGULAR_MIN_JS_URL = "http://ajax.googleapis.com/ajax/libs/angularjs/1.2.1/angular.min.js";

	/**
	 * This is the entry point method.
	 */
	public final void onModuleLoad() {
		loadAngular();
	}

	/**
	 * Override this and invoke GWT.create() on your modules.
	 */
	protected abstract AngularModule[] main();

	private void loadAngular() {
		if(!isInjected()) {
		ScriptInjector
				.fromUrl(
						ANGULAR_MIN_JS_URL)
				.setCallback(new Callback<Void, Exception>() {
					@Override
					public void onFailure(Exception reason) {
						Browser.getWindow().getConsole()
								.warn("loading angularjs failed.");
					}

					@Override
					public void onSuccess(Void result) {
						Browser.getWindow().getConsole()
								.info("loading angularjs succeeded.");
						injectModules();
					}
				}).setWindow(ScriptInjector.TOP_WINDOW).inject();
		} else {
			Browser.getWindow().getConsole()
			.info("angularjs already loaded.");
			injectModules();
		}
	}

	private void injectModules() {
		final JsArrayOfString moduleNames = JsArrayOfString.create();
		final AngularModule[] modules = main();
		for (int i = 0; i < modules.length; i++) {
			moduleNames.push(((AngularModuleBase) modules[i]).moduleName());
		}
		bootstrap(moduleNames);
	}

	private native final boolean isInjected() /*-{
	    if (!(typeof $wnd.angular === "undefined") && !(null===$wnd.angular)) {
	        return true;
	    }
	    	return false;
	}-*/;
	
	private native void bootstrap(JsArrayOfString moduleNames)	/*-{
			$wnd.angular.bootstrap($doc, moduleNames);
	}-*/;
}
