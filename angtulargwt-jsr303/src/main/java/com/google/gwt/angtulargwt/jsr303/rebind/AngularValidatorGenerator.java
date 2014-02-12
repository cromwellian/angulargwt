package com.google.gwt.angtulargwt.jsr303.rebind;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.validation.Validator;

import com.google.gwt.angtulargwt.jsr303.client.NgValidate;
import com.google.gwt.angtulargwt.jsr303.client.NgValidator;
import com.google.gwt.angular.client.AngularModule;
import com.google.gwt.angular.client.Util;
import com.google.gwt.angular.client.impl.AngularModuleBase;
import com.google.gwt.angular.rebind.AngularConventions;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.thirdparty.guava.common.base.Joiner;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.google.gwt.validation.client.GwtValidation;

public class AngularValidatorGenerator extends Generator {
	
	private static final String TARGET_NAME = "AngularValidatorImpl";
	private static final String TARGET_PKG = "com.google.gwt.angtulargwt.jsr303";
	private static final String implTypeName = TARGET_PKG + TARGET_NAME;

	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
		
		JClassType type = context.getTypeOracle().findType(typeName);
		logger.log(TreeLogger.Type.DEBUG, "Generating " + typeName);

		// verify input is NgValidator
		JClassType ngValidatorType = context.getTypeOracle().findType(NgValidator.class.getName());;
		if(!type.isAssignableTo(ngValidatorType)) {
			logger.log(TreeLogger.Type.ERROR, "Don't know how to create " + typeName + " it's not assignable to NgValidator");
			throw new UnableToCompleteException();
		}
		
		// check for NgValidate annotation and collect class literals
		NgValidate annotation= ngValidatorType.getAnnotation(NgValidate.class);
		List<Class<?>> beanlist;
		
		if(annotation != null) {
			beanlist = Arrays.asList(annotation.value());
//			for(Class<?> clazz : beanlist) {
				// TODO sanity-check if its bean-ish
//			}
		} else {
			logger.log(TreeLogger.Type.ERROR, "Input must be annotated with NgValidate");
			throw new UnableToCompleteException();
		}
		
		// TODO Generate AngularValidator and return classname
		PrintWriter pw = context.tryCreate(logger,TARGET_PKG,TARGET_NAME);
		if(pw!=null) {
			SourceWriter sw = writeImpl(context, pw, beanlist);
			sw.commit(logger);
		}
		
		return implTypeName;
	}

	SourceWriter writeImpl(GeneratorContext context, PrintWriter pw, List<Class<?>> beanlist) {
		SourceWriter sw = null;

		ClassSourceFileComposerFactory fac = new ClassSourceFileComposerFactory(TARGET_PKG,TARGET_NAME);
		fac.addImport(Validator.class.getName());
		fac.addImport(GwtValidation.class.getName());
		
		for(Class<?> clazz: beanlist) {
			fac.addImport(clazz.getName());
		}
		
		fac.makeInterface();
		fac.addImplementedInterface(Validator.class.getName());
		
		fac.addAnnotationDeclaration(createAnnotation(beanlist));

		if (pw != null) {
			sw = fac.createSourceWriter(context, pw);
		}
		
		// TODO actually generate code
		sw.outdent();
		sw.print("}");
		
		return sw;
	}

	String createAnnotation(List<Class<?>> beanlist) {
		StringBuilder sb = new StringBuilder();
		sb.append("@GwtValidation({");
		
//		Joiner.on(',').appendTo(sb , beanlist);
		if(!beanlist.isEmpty()) {
		Iterator<Class<?>> it = beanlist.iterator();
		sb.append(it.next().getSimpleName());
		sb.append(".class");
		while(it.hasNext()) {
			sb.append(',');
			sb.append(it.next().getSimpleName());
			sb.append(".class");
		}
		}
		
		sb.append("})");
		return sb.toString();
	}

}
