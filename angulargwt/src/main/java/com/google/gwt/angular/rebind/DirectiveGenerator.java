package com.google.gwt.angular.rebind;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gwt.angular.client.NgDirective;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.user.rebind.SourceWriter;

public class DirectiveGenerator {

	public static void generateDirectives(GeneratorContext context, SourceWriter sw,
			Class<?> clazz) {
		// class of a directive
		JClassType dType = context.getTypeOracle().findType(clazz.getName());
		NgDirective ngDirective = dType.getAnnotation(NgDirective.class);
			// a method annotated as directive
			if (ngDirective != null ) {
				sw.print("module.directive('" + ngDirective.value() + "',[ ");
				generateSingleDirective(sw, dType,ngDirective);
				sw.println("]);");
			}
	}

	private static void generateSingleDirective(SourceWriter sw, JClassType dType, NgDirective ngDirective ) {

		//find initmethod
		JMethod initMethod = dType.findMethod("init", new JType[0]);
		
		//find injected components
		List<Injection> injects = Injection.getInjectedFields(dType);
		
		List<String> params = Lists.transform(injects,Injection.toName);
		
		if(!params.isEmpty()) {
			sw.print("\'" +
					Joiner.on("\', " + "\'").join(params)
					+  "\', ");			
		}
		
		sw.print("function (");
		String paramString = Joiner.on(",").join(params);
		sw.println(paramString + ") {");
		sw.indent();
		sw.println("return { ");
		sw.indent();
		
		if (!ngDirective.templateUrl().isEmpty()) {
			
			if(!ngDirective.inlineTemplate()){
				sw.println("templateUrl : '"+ ngDirective.templateUrl() +"',");
			} else {
				
				try {
					String template = new Scanner(new File(ngDirective.templateUrl())).useDelimiter("\\Z").next();
					template = template.replace("\n", "").replace("\r", "");
					sw.println("template : '"+ template +"',");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
		
//		if (!ngDirective.template().isEmpty()){
//			sw.println("template : \"" + ngDirective.template() +"\",");
//		}
		
		if (!ngDirective.restrict().isEmpty()) {
			sw.println("restrict : \""+ ngDirective.restrict() +"\",");
		}
		
		if (ngDirective.transclude()) {
			sw.println("transclude : true,");
		}
		
		ArrayList<String> linkPassedParams = new ArrayList<String>();
		linkPassedParams.add("scope");
		linkPassedParams.add("element");
		linkPassedParams.add("attrs");
		
		List<String> requires = getRequires(dType);
		
		//angularJS configurations
		for(int requireCount = 0;requireCount < requires.size();requireCount++)
			linkPassedParams.add("requires[" + requireCount + "]");

		if (!requires.isEmpty()) {
			sw.println("require: ['" + Joiner.on("', '").join(requires) + "'],");
		}
		
		//link function 
		String linkArgs = "(scope,element,attrs"
				+ (!requires.isEmpty() ? ", " + "requires" : "") + ")";
		
		sw.println("link: function" + linkArgs + " {");
		sw.indent();
		
		//construct directive
		sw.println("var directive = @" + dType.getQualifiedSourceName()
				+ "::new()();");
		
		//inject
		for(Injection inj : injects) {
			sw.println("directive.%s=%s;", inj.localName,inj.ngName);
		}
		
		//initialize	
		if (initMethod != null) {
			sw.println("directive." + initMethod.getJsniSignature() + "()");
		}
		
		//call the compiled link function
		sw.println("directive.@" + dType.getQualifiedSourceName() + 
		"::link(Lcom/google/gwt/angular/client/Scope;Lelemental/util/ArrayOf;Lelemental/json/JsonObject;)(scope,element,attrs)");
		
		
		//close link function
		sw.println("}");
		sw.outdent();
		sw.outdent();
		
		//close returned object
		sw.println("};");
		sw.outdent();
		
		//close directive function
		sw.println("}");
		
	}
	
	public static List<String> getRequires(JClassType dType) {
		List<String> requires = new ArrayList<String>();
		return requires;
		//Dead code: inter-directive communication with require and controllers
//		for (JField rt : dType.getFields()) {
//			JClassType lpType = rt.getType().isClassOrInterface();
//			if (lpType != null) {
//				NgInject requiresInject = lpType.getAnnotation(NgInject.class);
//				if (requiresInject != null) {
//					if ("$scope".equals(requiresInject.name())
//							|| "$element".equals(requiresInject.name())) {
//						continue;
//					}
//					
//					requires.add(requiresInject.name());
//				}
//			}
//		}
//		return requires;
	}

}
