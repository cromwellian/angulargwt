package com.google.gwt.angular.client;

import com.google.gwt.angular.client.impl.AngularModuleBase;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.Promise;
import com.google.gwt.query.client.plugins.deferred.PromiseFunction;

import elemental.client.Browser;
import elemental.js.util.JsArrayOfString;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public abstract class AngularApp extends AngularEntryPoint { 

	private static final String ANGULAR_MIN_JS_URL = "https://ajax.googleapis.com/ajax/libs/angularjs/1.2.13/angular.min.js";

	/**
	 * This is the entry point method.
	 */
	public final void onModuleLoad() {
		loadAngular();
	}

	private void loadAngular() {
		if(!isInjected()) {
			Promise loadAngular = new ScriptLoader();

			loadAngular.done(new Function() {
				public void f() {
					Browser.getWindow().getConsole().info("loading angularjs succeeded.");
					injectModules();					
				}
			});

			loadAngular.fail(new Function() {
				public void f() {
					Browser.getWindow().getConsole().warn("loading angularjs failed.");
				}
			});

		} else {
			Browser.getWindow().getConsole().info("angularjs already loaded.");
			injectModules();
		}
	}

	private void injectModules() {
		super.onModuleLoad();
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

	/**
	 * Override this and invoke GWT.create() on your modules.
	 */
	
	private final static class ScriptLoader extends PromiseFunction {
	
		@Override
		public void f(final Deferred dfd) {
			ScriptInjector
			.fromUrl(ANGULAR_MIN_JS_URL)
			.setCallback(new Callback<Void, Exception>() {
				@Override
				public void onFailure(Exception reason) {
					dfd.reject(reason);
				}
	
				@Override
				public void onSuccess(Void result) {
					dfd.resolve();
				}
			}).setWindow(ScriptInjector.TOP_WINDOW).inject();				
		}
	
	
	}
}

