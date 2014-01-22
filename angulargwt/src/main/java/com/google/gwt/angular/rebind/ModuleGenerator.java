package com.google.gwt.angular.rebind;

import java.io.PrintWriter;

import com.google.gwt.angular.client.AngularController;
import com.google.gwt.angular.client.AngularModule;
import com.google.gwt.angular.client.Directive;
import com.google.gwt.angular.client.NgDepends;
import com.google.gwt.angular.client.NgInject;
import com.google.gwt.angular.client.NgName;
import com.google.gwt.angular.client.Util;
import com.google.gwt.angular.client.impl.AngularModuleBase;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

class ModuleGenerator {

	public static String generateModule(TreeLogger logger, GeneratorContext context, AngularGwtTypes types,
			JClassType type) {
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
		String typeName = type.getQualifiedSourceName() + AngularConventions.MODULEIMPL;
	
		if (pw != null) {
			sw = fac.createSourceWriter(context, pw);
		}
		if (sw == null) {
			return typeName;
		}
		sw.indent();
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
						sw.println(" return args[" + i + "];");
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
	
		sw.println("public String moduleName() {");
		sw.indent();
		sw.println("return \"" + modName + "\";");
		sw.outdent();
		sw.println("}");
		sw.outdent();
		sw.commit(logger);
		return typeName;
	}
	
}