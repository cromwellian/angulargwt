#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import com.google.gwt.angular.client.Scope;

/**
 * This is your Scope, a shared memory between a controller and the view
 * a.k.a. ViewModel
 * Declare further properties as bean-patterns
 * 
 */
public interface ${appName}Scope extends Scope<${appName}Scope> {
	String name();
	${appName}Scope name(String name);
	
	String result();
	${appName}Scope result(String result);
}
