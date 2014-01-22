package com.google.gwt.angular.rebind;

import java.util.ArrayList;

import com.google.common.base.Joiner;
import com.google.gwt.angular.client.NgDirective;
import com.google.gwt.angular.client.NgInject;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.user.rebind.SourceWriter;

public class DirectiveGenerator {
	public static void generateDirectives(GeneratorContext context, SourceWriter sw,
			Class<?> clazz) {
		// class containing directives
		JClassType dType = context.getTypeOracle().findType(clazz.getName());

		for (JMethod method : dType.getMethods()) {
			NgDirective ngDirective = method.getAnnotation(NgDirective.class);

			// a method annotated as diredctive
			if (ngDirective != null) {
				sw.print("module.directive('" + ngDirective.value() + "', "
						+ "function " + method.getName() + "(");
				generateSingleDirective(sw, dType, method);
				sw.println(");");
			}
		}
	}

	private static void generateSingleDirective(SourceWriter sw, JClassType dType,	JMethod method) {

		//find initmethod for class
		JMethod initMethod = null;
		for (JMethod m : dType.getMethods()) {
			if (m == method) {
				continue;
			}
			if (m.getName().equals(
					"on" + Character.toUpperCase(method.getName().charAt(0))
							+ method.getName().substring(1))) {
				initMethod = m;
				break;
			}
		}
		
		ArrayList<String> params = new ArrayList<String>();
		//gather params for initmethod
		if (initMethod != null) {
			for (JParameter param : initMethod.getParameters()) {
				JClassType pType = param.getType().isClassOrInterface();
				if (pType != null) {
					NgInject pInject = pType.getAnnotation(NgInject.class);

					if (pInject != null) {
						params.add(pInject.name());
					}
				}
			}
		}

		String paramString = Joiner.on(",").join(params);
		sw.println(paramString + ") {");
		sw.indent();
		sw.println("return { ");
		sw.indent();

		ArrayList<String> linkPassedParams = new ArrayList<String>();
		linkPassedParams.add("scope");
		linkPassedParams.add("element");
		linkPassedParams.add("attrs");
		
		ArrayList<String> requires = new ArrayList<String>();
		int requireCount = 0;
		
		for (JParameter p : method.getParameters()) {
			JClassType lpType = p.getType().isClassOrInterface();
			if (lpType != null) {
				NgInject requiresInject = lpType.getAnnotation(NgInject.class);
				if (requiresInject != null) {
					if ("$scope".equals(requiresInject.name())
							|| "$element".equals(requiresInject.name())) {
						continue;
					}
					linkPassedParams.add("requires[" + requireCount + "]");
					requireCount++;
					requires.add(requiresInject.name());
				}
			}
		}
		
		String linkArgs = "(scope,element,attrs"
				+ (requireCount > 0 ? ", " + "requires" : "") + ")";
		if (!requires.isEmpty()) {
			sw.println("require: ['" + Joiner.on("', '").join(requires) + "'],");
		}
		
		sw.println("link: function" + linkArgs + " {");
		sw.indent();
		
		sw.println("var directive = @" + dType.getQualifiedSourceName()
				+ "::new()();");
		if (initMethod != null) {
			sw.println("directive." + initMethod.getJsniSignature() + "("
					+ paramString + ");");
		}
		sw.println("directive." + method.getJsniSignature() + "("
				+ Joiner.on("," + "").join(linkPassedParams) + ");");
		sw.println("}");
		sw.outdent();
		sw.outdent();
		sw.println("};");
		sw.outdent();
		sw.println("}");
		
	}

}
