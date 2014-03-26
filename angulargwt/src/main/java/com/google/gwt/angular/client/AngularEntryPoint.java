package com.google.gwt.angular.client;

import com.google.gwt.angular.client.impl.AngularModuleBase;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.query.client.impl.ConsoleBrowser;

/**
 * Entry Point for AngularGWT scripts. 
 * Every EntryPoint exposes one or more {@link AngularModule}s to the AngularJS injector
 * 
 * Due to the GWT async loader, an existing AngularJS App depending on GWT modules needs to be informed to start bootstrapping.
 * This is done via the callback {@code window.angularGwtModuleLoaded()} with the module's name as parameter.  
 * 
 * The subclass AngularApp also loads AngularJS if necessary and initiates bootstrapping, it is to be used when the top-module is written in AngularGWT.
 *  
 * @author h0ru5
 *
 */
public abstract class AngularEntryPoint implements EntryPoint {

	protected abstract AngularModule[] main();
	
	@Override
	public void onModuleLoad() {
		ConsoleBrowser cb = new ConsoleBrowser();
		for(AngularModule module : main()) {
			String moduleName = ((AngularModuleBase)module).moduleName();
			cb.log("onModuleLoad is called for " +  moduleName);
			informApp(moduleName);
		}
	}

	private native void informApp(String moduleName) /*-{
		if($wnd.angularGwtModuleLoaded) 
			$wnd.angularGwtModuleLoaded(moduleName);
	}-*/;
	
	
}
