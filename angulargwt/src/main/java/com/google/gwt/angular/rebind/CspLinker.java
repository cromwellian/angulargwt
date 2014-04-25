package com.google.gwt.angular.rebind;

import com.google.gwt.core.ext.LinkerContext;
import com.google.gwt.core.ext.linker.LinkerOrder;
import com.google.gwt.core.linker.CrossSiteIframeLinker;

@LinkerOrder(LinkerOrder.Order.PRIMARY)
public class CspLinker extends CrossSiteIframeLinker {

  @Override
	public String getDescription() {
		return "CSP-Safe Linker";
	}

/**
   * Returns the name of the {@code JsInstallLocation} script.  By default,
   * returns {@code "com/google/gwt/core/ext/linker/impl/installLocationIframe.js"}.
   *
   * @param context a LinkerContext
   */
 @Override
  protected String getJsInstallLocation(LinkerContext context) {
    return "com/google/gwt/angular/rebind/installCspSafe.js";
  }

  /**
   * Returns the name of the {@code JsInstallScript} script.  By default,
   * returns {@code "com/google/gwt/core/ext/linker/impl/installScriptEarlyDownload.js"}.
   *
   * <p> If you override this to return {@code installScriptDirect.js}, then you
   * should also override {@link #shouldInstallCode(LinkerContext)} to return
   * {@code false}.
   *
   * @param context a LinkerContext
   */
 @Override
  protected String getJsInstallScript(LinkerContext context) {
    return "com/google/gwt/core/ext/linker/impl/installScriptDirect.js";
  }

  @Override
  protected boolean shouldInstallCode(LinkerContext context) {
    return false;
  }

}
