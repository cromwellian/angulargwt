package com.google.gwt.angular.rebind;

import java.io.PrintWriter;

import com.google.gwt.angular.client.Util;
import com.google.gwt.angular.client.impl.JsScopeBase;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

import elemental.js.json.JsJsonObject;
import elemental.json.JsonObject;
import elemental.util.ArrayOfString;

class ScopeGenerator extends Generator {	
	private static ScopeGenerator instance;

	public String generate(TreeLogger logger,
			GeneratorContext context, String typeName)
			throws UnableToCompleteException {
		
		AngularGwtTypes types = AngularGwtTypes.getInstanceFor(context);
		JClassType type = context.getTypeOracle().findType(typeName);
		
		ClassSourceFileComposerFactory fac = new ClassSourceFileComposerFactory(
				type.getPackage().getName(), type.getName() + AngularConventions.FACTORY);
		fac.addImplementedInterface(types.factoryType.getQualifiedSourceName());
		fac.addImport(JsJsonObject.class.getName());
		fac.addImport(Util.class.getName());
		PrintWriter pw = context.tryCreate(logger, type.getPackage().getName(),
				type.getName() + AngularConventions.FACTORY);
		SourceWriter sw = null;
		String factoryTypeName = type.getQualifiedSourceName() + AngularConventions.FACTORY;
		if (pw != null) {
			sw = fac.createSourceWriter(context, pw);
		}
		if (sw == null) {
			return factoryTypeName;
		}

		sw.indent();
		sw.println("public native " + generateScope(logger, context, types, type)
				+ " create() /*-{");
		sw.indent();

		sw.println("return {};");
		sw.outdent();
		sw.println("}-*/;");
		sw.outdent();
		sw.commit(logger);
		logger.log(TreeLogger.Type.DEBUG, "Generated " + factoryTypeName);
		return factoryTypeName;
	}

	static String generateScope(TreeLogger logger, GeneratorContext context, AngularGwtTypes types,
			JClassType scopeClass) throws UnableToCompleteException {
		String simpleName = scopeClass.getName() + AngularConventions.SCOPEIMPL;
		ClassSourceFileComposerFactory fac = new ClassSourceFileComposerFactory(
				scopeClass.getPackage().getName(), simpleName);
		fac.setSuperclass(JsScopeBase.class.getName() + "<"
				+ scopeClass.getName() + ">");
		fac.addImport(JsonObject.class.getName());
		fac.addImport(ArrayOfString.class.getName());
		fac.addImport(JsScopeBase.class.getName());
		fac.addImport(Util.class.getName());
		fac.addImplementedInterface(scopeClass.getQualifiedSourceName());
		PrintWriter pw = context.tryCreate(logger, scopeClass.getPackage()
				.getName(), simpleName);
		SourceWriter sw = null;
		String typeName = scopeClass.getQualifiedSourceName() + AngularConventions.SCOPEIMPL;

		if (pw != null) {
			sw = fac.createSourceWriter(context, pw);
		}
		if (sw == null) {
			return typeName;
		}
		BeanImplGenerator.generateBeanImpl(logger, context, types, scopeClass, simpleName, sw);

		sw.commit(logger);
		return typeName;
	}

	public static ScopeGenerator get() {
		if(instance==null) instance = new ScopeGenerator();
		return instance;
	}
}