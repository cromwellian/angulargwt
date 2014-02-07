package com.github.h0ru5.gwt.SomeModule;

import com.google.gwt.angular.client.NgInject;

@NgInject(name="sample")
public class SampleService {

	public String greet(String name) {
		return "Hello " + name;
	}
	
	public String dismiss(String name) {
		return "Goodbye " + name;
	}
}
