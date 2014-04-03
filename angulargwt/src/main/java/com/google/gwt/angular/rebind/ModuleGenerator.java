package com.google.gwt.angular.rebind;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.google.common.collect.Iterables;
import com.google.gwt.angular.client.AngularController;
import com.google.gwt.angular.client.AngularModule;
import com.google.gwt.angular.client.Directive;
import com.google.gwt.angular.client.NgDepends;
import com.google.gwt.angular.client.NgInject;
import com.google.gwt.angular.client.NgName;
import com.google.gwt.angular.client.NgWatch;
import com.google.gwt.angular.client.Util;
import com.google.gwt.angular.client.impl.AngularModuleBase;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.thirdparty.guava.common.base.Predicate;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class ModuleGenerator extends Generator {

	private static ModuleGenerator instance;

		
	
	public String generate(TreeLogger logger, GeneratorContext context, String typeName) {
		JClassType type = context.getTypeOracle().findType(typeName);
		
		NgDepends deps = type.getAnnotation(NgDepends.class);
		String simpleName = type.getName() + AngularConventions.MODULEIMPL;
		ClassSourceFileComposerFactory fac = new ClassSourceFileComposerFactory(
				type.getPackage().getName(), simpleName);
		
		fac.addImport(GWT.class.getName());
		fac.addImport(Util.class.getName());
		fac.addImplementedInterface(AngularModule.class.getName());
		fac.setSuperclass(AngularModuleBase.class.getName());
		
		PrintWriter pw = context.tryCreate(logger, type.getPackage().getName(),
				simpleName);
		
		SourceWriter sw = null;
		String implTypeName = type.getQualifiedSourceName() + AngularConventions.MODULEIMPL;
	
		if (pw != null) {
			sw = fac.createSourceWriter(context, pw);
		}
		if (sw == null) {
			return implTypeName;
		}
		sw.indent();
		
		// constructor
		sw.println("public " + simpleName + "() {");
		sw.indent();
		// init(GWT.create(dep1), GWT.create(dep2))
		sw.print("init(");
		if (deps != null) {
			boolean first = true;
			for (Class<?> clazz : deps.value()) {
				if (clazz.getAnnotation(NgInject.class) != null) {
					if (!first) {
						sw.print(",");
					} else {
						first = false;
					}
					sw.print("GWT.create(" + clazz.getName() + ".class)");
				}
			}
		}
		sw.println(");");
	
		sw.outdent();
		sw.println("}");
		sw.println();
		
		// native init
		sw.println("public native void init(Object... args) /*-{");
		String modName = type.getSimpleSourceName();
		NgName ngName = type.getAnnotation(NgName.class);
		if (ngName != null) {
			modName = ngName.value();
		}
		sw.println("var module = $wnd.angular.module('" + modName + "', []);");
		if (deps != null) {
			int i = 0;
			for (Class<?> clazz : deps.value()) {
				NgInject ngInject = clazz.getAnnotation(NgInject.class);
				if (ngInject != null) {
					// is an injectible
					if (AngularController.class.isAssignableFrom(clazz)) {
						// is a controller
						sw.println("args["
								+ i
								+ "].@"
								+ AngularController.class.getName()
								+ "::register"
								+ "(Lcom/google/gwt/core/client/JavaScriptObject;)(module);");
					} else {
						// is a service
						sw.println("module.factory('" + ngInject.name()
								+ "', function() {");
						sw.indent();
						JClassType serviceType = context.getTypeOracle().findType(clazz.getName()); 
						generateService(sw, i,serviceType);
						sw.outdent();
						sw.println("});");
					}
				} else if (Directive.class.isAssignableFrom(clazz)) {
					DirectiveGenerator.generateDirectives(context, sw, clazz);
				}
				i++;
			}
		}
		sw.println("}-*/;");
		
		// getter for module name
		sw.println("public String moduleName() {");
		sw.indent();
		sw.println("return \"" + modName + "\";");
		sw.outdent();
		sw.println("}");
		sw.outdent();

		sw.commit(logger);
		return implTypeName;
	}

	private void generateService(SourceWriter sw, int argNumber, JClassType serviceType) {
		final String instance = "args[" + argNumber + "]";
		boolean first=true;
		sw.println("return {");
		sw.indent();
		for(JMethod m : ClassHelper.publicMethods(serviceType)) {
			if(!first) 
				sw.print(",");
			else
				first=false;
			
			String argString = ClassHelper.declareArgs(m);
			sw.println(m.getName() + " : $entry(function("
					+ argString + ") {");
			sw.indent();
			sw.print(ClassHelper.isVoidMethod(m) ? "" : "return ");
			sw.print(instance + "." + m.getJsniSignature());
			sw.println("(" + argString + ");");
			sw.outdent();
			sw.println("})");	
		}
		sw.outdent();
		sw.println("};");
		//sw.println(" return " + instance + ";");
	}
	
	public static ModuleGenerator get() {
		if(instance==null) instance=new ModuleGenerator();
		return instance;
	}
	
}