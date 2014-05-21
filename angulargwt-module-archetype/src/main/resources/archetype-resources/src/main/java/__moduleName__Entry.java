#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import com.google.gwt.angular.client.AngularEntryPoint;
import com.google.gwt.angular.client.AngularModule;
import com.google.gwt.core.shared.GWT;

/**
 * This represents your javascript file containing the Ng-Modules, 
 * each one is injected via the given mnemonic in NgName
 * add all exposed Modules to the main function 
 */
public class ${moduleName}Entry extends AngularEntryPoint {

	@Override
	protected AngularModule[] main() {
		return new AngularModule[] { GWT.create(${moduleName}Module.class) };
	}

}
