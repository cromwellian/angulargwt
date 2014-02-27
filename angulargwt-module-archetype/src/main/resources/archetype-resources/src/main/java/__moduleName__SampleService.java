package ${package};

import com.google.gwt.angular.client.NgInject;

@NgInject(name="$moduleName.toLowerCase()service")
public class ${moduleName}SampleService {

	public String greet(String name) {
		return "Hello " + name;
	}
	
	public String dismiss(String name) {
		return "Goodbye " + name;
	}
}
