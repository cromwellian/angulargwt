#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import com.google.gwt.angular.client.AngularController;
import com.google.gwt.angular.client.NgInject;
import com.google.gwt.angular.client.NgWatch;

import elemental.client.Browser;

/**
 * This is your Controller
 * append injected services to the onInit-Method
 * all public methods are exposed to the View
 */
@NgInject(name="${appName}Controller")
public class ${appName}Controller extends AngularController<${appName}Scope> {
	public void onInit(${appName}Scope scope) {
		scope.name("World");
	}
	
	public void resetName() {
		scope.name("World");
	}
	
	public void toUpper() {
		scope.name(scope.name().toUpperCase());
	}
	
	public void toLower() {
		scope.name(scope.name().toLowerCase());
	}
	
	@NgWatch("name")
	public void watchName(String newName){
		Browser.getWindow().getConsole().log("Name changed to " + newName);
	}
}
