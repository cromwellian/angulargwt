package com.google.gwt.angular.rebind;

import java.io.PrintWriter;

import com.google.gwt.angular.client.Util;
import com.google.gwt.angular.client.impl.JsModelBase;
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

public class ModelGenerator extends Generator{
	private static ModelGenerator instance;

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
		sw.println("public native " + ModelGenerator.generateModelType(logger, context, types, type)
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

	static String generateModelType(TreeLogger logger,
			GeneratorContext context, AngularGwtTypes types, JClassType modelType)
			throws UnableToCompleteException {
		String simpleName = modelType.getName() + AngularConventions.MODELIMPL;
		ClassSourceFileComposerFactory fac = new ClassSourceFileComposerFactory(
				modelType.getPackage().getName(), simpleName);
		fac.setSuperclass(JsModelBase.class.getName() + "<"
				+ modelType.getName() + ">");
		fac.addImport(JsonObject.class.getName());
		fac.addImport(ArrayOfString.class.getName());
		fac.addImport(JsModelBase.class.getName());
		fac.addImport(Util.class.getName());
		fac.addImplementedInterface(modelType.getQualifiedSourceName());
		PrintWriter pw = context.tryCreate(logger, modelType.getPackage()
				.getName(), simpleName);
		SourceWriter sw = null;
		String typeName = modelType.getQualifiedSourceName() + AngularConventions.MODELIMPL;
	
		if (pw != null) {
			sw = fac.createSourceWriter(context, pw);
		}
		if (sw == null) {
			return typeName;
		}
		BeanImplGenerator.generateBeanImpl(logger, context, types, modelType, simpleName, sw);
	
		sw.commit(logger);
		return typeName;
	}

	public static ModelGenerator get() {
		if(instance == null) instance = new ModelGenerator();
		return instance;
	}
}
