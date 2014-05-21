package com.google.gwt.angular.rebind;

import com.google.gwt.angular.client.NgInject;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.user.rebind.SourceWriter;

public class FilterGenerator {

	public static void generateFilter(GeneratorContext context,
			SourceWriter sw, Class<?> clazz, NgInject ngInject, String compInstance) {		
		sw.println("module.filter('" + determineName(clazz.getSimpleName(),ngInject)
				+ "', function() {");
		sw.indent();
		JClassType filterType = context.getTypeOracle().findType(clazz.getName()); 
		JMethod filter = ClassHelper.methodByName(filterType,"filter");
		sw.println("return %s.%s;", compInstance, filter.getJsniSignature());
		sw.outdent();
		sw.println("});");
	}

	static String determineName(String simpleName, NgInject ngInject) {
		String fname = (ngInject!=null) ? ngInject.name() : simpleName;
		if(fname!=null && fname.endsWith("Filter")) {
			fname = fname.substring(0, fname.length() - "Filter".length());
		}
		return fname;
	}

}
