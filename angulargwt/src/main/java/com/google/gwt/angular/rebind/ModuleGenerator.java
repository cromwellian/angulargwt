package com.google.gwt.angular.rebind;

import java.io.PrintWriter;

import com.google.gwt.angular.client.AngularController;
import com.google.gwt.angular.client.AngularModule;
import com.google.gwt.angular.client.Directive;
import com.google.gwt.angular.client.Filter;
import com.google.gwt.angular.client.NgDepends;
import com.google.gwt.angular.client.NgInject;
import com.google.gwt.angular.client.NgName;
import com.google.gwt.angular.client.Util;
import com.google.gwt.angular.client.impl.AngularModuleBase;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class ModuleGenerator extends Generator {

	private static ModuleGenerator instance;
	
	public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
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
				final String compInstance = "args[" + i++ + "]";
				
				// automatically adding inject based on classname to filters that are not annotated
				if (Filter.class.isAssignableFrom(clazz)) {
					FilterGenerator.generateFilter(context,sw,clazz,ngInject,compInstance);
				} else if (ngInject != null) {
				// is an injectible
					
					if (AngularController.class.isAssignableFrom(clazz)) {
						// is a controller
						sw.println(compInstance
								+ ".@"
								+ AngularController.class.getName()
								+ "::register"
								+ "(Lcom/google/gwt/core/client/JavaScriptObject;)(module);");
					} else {
						// is a service
						sw.println("module.factory('" + ngInject.name()
								+ "', function() {");
						sw.indent();
						JClassType serviceType = context.getTypeOracle().findType(clazz.getName()); 
						generateService(sw, compInstance,serviceType);
						sw.outdent();
						sw.println("});");
					}
				} else if (Directive.class.isAssignableFrom(clazz)) {
					// is a directive
					DirectiveGenerator.generateDirectives(context, sw, clazz);
				} else {
					logger.log(TreeLogger.Type.ERROR, "Unknown type of component: "
							+ clazz.getSimpleName() + "\nDid you forget to add @NgInject?");
					throw new UnableToCompleteException();
				}
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

	private static void generateService(SourceWriter sw, final String instance, JClassType serviceType) {
		
		boolean first=true;
		sw.println("return $wnd.angular.extend(" + instance +",{");
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
		sw.println("});");
		//sw.println(" return " + instance + ";");
	}
	
	public static ModuleGenerator get() {
		if(instance==null) instance=new ModuleGenerator();
		return instance;
	}
	
}