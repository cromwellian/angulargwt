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
public interface ${moduleName}Scope extends Scope<${moduleName}Scope> {
	String name();
	${moduleName}Scope name(String name);
	
	String result();
	${moduleName}Scope result(String result);
}
