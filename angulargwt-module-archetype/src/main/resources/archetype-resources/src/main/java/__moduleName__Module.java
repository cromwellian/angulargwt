#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import com.google.gwt.angular.client.AngularModule;
import com.google.gwt.angular.client.NgDepends;
import com.google.gwt.angular.client.NgName;

/**
 * This is your Module, injectable via the given mnemonic
 * add all exposed Components (Services and Directives) to the Depends-Annotation
 *
 */
@NgName("$moduleName.toLowerCase()")
@NgDepends({${moduleName}SampleService.class, ${moduleName}Controller.class, ${moduleName}Directive.class})
public class ${moduleName}Module implements AngularModule {

}