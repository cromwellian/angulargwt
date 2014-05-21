package ${package};

import com.google.gwt.angular.client.Directive;
import com.google.gwt.angular.client.NgDirective;
import com.google.gwt.angular.client.NgElement;
import com.google.gwt.angular.client.NgInjected;
import com.google.gwt.angular.client.Scope;
import com.google.gwt.angular.client.WatchFunction;
import com.google.gwt.core.client.JavaScriptObject;

import elemental.dom.Element;
import elemental.dom.Node;
import elemental.json.JsonObject;
import elemental.util.ArrayOf;

@NgDirective("$moduleName.toLowerCase()Greet")
public class ${moduleName}Directive implements Directive {

	private static final String NAME_ATTR = "$moduleName.toLowerCase()Greet";
	
	@NgInjected
	public ${moduleName}SampleService sample;

	//general initialization
	@Override
	public void init() {

	}
	
	@Override
	public void link(final Scope scope, final ArrayOf<NgElement> element,
			final JsonObject attrs) {

		//set default expression
		String srcExpr = "name";
		
		//override default if attribute value is given
		if(attrs.hasKey(NAME_ATTR)) {
			srcExpr = attrs.get(NAME_ATTR).asString();
		}

		//init text in element
		element.get(0).setTextContent(sample.greet("World"));

		//setup $watch to bind graph's text property to the graph-source
		scope.$watch(srcExpr, new WatchFunction<String>() {
			@Override
			public void exec(String value) {
				element.get(0).setTextContent(sample.greet(value));
			}
		});
	}
}
