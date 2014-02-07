#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import com.google.gwt.angular.client.AngularApp;
import com.google.gwt.angular.client.AngularModule;
import com.google.gwt.core.shared.GWT;

/**
 * This is your Module, injectable via the given mnemonic
 * add all exposed Components (Services and Directives) to the Depends-Annotation
 *
 */
public class ${appName}App extends AngularApp {

	@Override
	protected AngularModule[] main() {
		return new AngularModule[] { GWT.create(${appName}Module.class) };
	}

}
