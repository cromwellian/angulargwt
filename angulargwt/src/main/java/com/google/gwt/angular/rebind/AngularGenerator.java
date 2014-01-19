package com.google.gwt.angular.rebind;

import com.google.common.base.Joiner;
import com.google.gwt.angular.client.*;
import com.google.gwt.angular.client.impl.AngularModuleBase;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.*;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

import elemental.js.json.JsJsonObject;
import elemental.util.ArrayOf;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.validator.xml.BeanType;

public class AngularGenerator extends Generator {
	
	private AngularGwtTypes types;

	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
		
		types=AngularGwtTypes.createFor(context);
		
		logger.log(TreeLogger.Type.DEBUG, "Generating " + typeName);
		JClassType type = context.getTypeOracle().findType(typeName);
		if (type.isAssignableTo(types.controllerType)) {
			return generateController(logger, context, typeName, type);
		} else if (type.isAssignableTo(types.scopeType)) {
			return ScopeGenerator.generateScopeFactory(logger, context, types, type);
		} else if (type.isAssignableTo(types.modelType)) {
			return generateModelFactory(logger, context, type);
		} else if (type.isAssignableTo(types.moduleType)) {
			return generateModule(logger, context, type);
		}
		logger.log(TreeLogger.Type.ERROR, "Don't know how to create "
				+ typeName);
		throw new UnableToCompleteException();
	}

	
	private String generateModelFactory(TreeLogger logger,
			GeneratorContext context, JClassType type)
			throws UnableToCompleteException {
		ClassSourceFileComposerFactory fac = new ClassSourceFileComposerFactory(
				type.getPackage().getName(), type.getName() + AngularConventions.FACTORY);
		fac.addImplementedInterface(types.factoryType.getQualifiedSourceName());
		fac.addImport(JsJsonObject.class.getName());
		fac.addImport(Util.class.getName());
		PrintWriter pw = context.tryCreate(logger, type.getPackage().getName(),
				type.getName() + AngularConventions.FACTORY);
		SourceWriter sw = null;
		String typeName = type.getQualifiedSourceName() + AngularConventions.FACTORY;
		if (pw != null) {
			sw = fac.createSourceWriter(context, pw);
		}
		if (sw == null) {
			return typeName;
		}

		sw.indent();
		sw.println("public native " + BeanImplGenerator.generateModelType(logger, context, types, type)
				+ " create() /*-{");
		sw.indent();

		sw.println("return {};");
		sw.outdent();
		sw.println("}-*/;");
		sw.outdent();
		sw.commit(logger);
		logger.log(TreeLogger.Type.DEBUG, "Generated " + typeName);
		return typeName;
	}

	private String generateModule(TreeLogger logger, GeneratorContext context,
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
					generateDirectives(context, sw, clazz);
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

	private void generateDirectives(GeneratorContext context, SourceWriter sw,
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

	private void generateSingleDirective(SourceWriter sw, JClassType dType,	JMethod method) {

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

	private String generateController(TreeLogger logger,
			GeneratorContext context, String typeName, JClassType type)
			throws UnableToCompleteException {
		ClassSourceFileComposerFactory fac = new ClassSourceFileComposerFactory(
				type.getPackage().getName(), type.getName() + AngularConventions.IMPL);
		fac.setSuperclass(typeName);
		fac.addImport(JavaScriptObject.class.getName());
		PrintWriter pw = context.tryCreate(logger, type.getPackage().getName(),
				type.getName() + AngularConventions.IMPL);
		SourceWriter sw = null;
		if (pw != null) {
			sw = fac.createSourceWriter(context, pw);
		}
		if (sw == null) {
			return typeName + AngularConventions.IMPL;
		}
		String controllerName = type.getSimpleSourceName();
		NgInject ngInject = type.getAnnotation(NgInject.class);
		if (ngInject != null) {
			controllerName = ngInject.name();
		}
		/*
		 * Generates code that looks similar to this: var self=this;
		 * $wnd.TodoController = $entry(function ($scope, $element) { var
		 * wrapScope =
		 * @com.google.gwt.angular.client.todomvc.BodyScopeAdapter::new
		 * (Lelemental/json/JsonObject;)($scope);
		 * self.@com.google.gwt.angular.client
		 * .AngularController::setScope(Lcom/google
		 * /gwt/angular/client/Scope;)(wrapScope);
		 * self.@com.google.gwt.angular.client
		 * .todomvc.TodoController::onInit(Lcom
		 * /google/gwt/angular/client/TodoScope
		 * ;Lcom/google/gwt/dom/client/Element;)(wrapScope, $element);
		 * $scope.doSomething = $entry(function() { return
		 * self.@com.google.gwt.angular
		 * .client.todomvc.TodoController::doSomething()(); }); });
		 * $wnd.TodoController.$inject = ['$scope', '$element'];
		 */
		JMethod onInitMethod = findInitMethod(type, logger);
		JClassType scopeClass = findScopeClass(onInitMethod);

		String scopeAdapter = null;
		if (scopeClass != null) {
			scopeAdapter = ScopeGenerator.generateScope(logger, context, types, scopeClass);
		}
		// this override teaches the compiler that TodoScope has been
		// instantiated so it is not pruned
		sw.indent();
		sw.println("protected void setScope(" + scopeAdapter
				+ " jsoScope) { super.setScope" + "(jsoScope); " + "}");
		sw.outdent();

		sw.indent();
		sw.println("protected native void register(JavaScriptObject module) /*-{");
		sw.indent();
		sw.println("var self = this;");
		sw.print("var ctrlFunc =  $entry(function(");
		List<String> params = declareControllerParams(onInitMethod
				.getParameters());
		sw.print(Joiner.on(", ").join(params));
		sw.println(") {");
		sw.indent();
		sw.println("self.@" + typeName + AngularConventions.IMPL + "::setScope" + "(*)($scope);");
		sw.print("self." + onInitMethod.getJsniSignature() + "(");
		String controllerParams = Joiner.on(", ").join(params);
		sw.print(controllerParams);
		sw.println(");");

		for (JMethod action : publicActionMethods(type, onInitMethod)) {
			String argString = declareArgs(action);
			sw.println("$scope." + action.getName() + " = $entry(function("
					+ argString + ") {");
			sw.indent();
			sw.print(isVoidMethod(action) ? "" : "return ");
			sw.print("self." + action.getJsniSignature());
			sw.println("(" + argString + ");");
			sw.outdent();
			sw.println("});");
		}

		for (JMethod action : watchMethods(type)) {
			NgWatch watchParams = action.getAnnotation(NgWatch.class);
			String argString = declareArgs(action);
			sw.println("$scope.$watch('" + watchParams.value()
					+ "', $entry(function(" + argString + ") {");
			sw.indent();
			sw.print(isVoidMethod(action) ? "" : "return ");
			sw.print("self." + action.getJsniSignature());
			sw.println("(" + argString + ");");
			sw.outdent();
			sw.println("}), " + watchParams.objEq() + ");");
		}

		sw.outdent();
		sw.println("});");
		// assign controller injections
		sw.println("ctrlFunc.$inject = [\""
				+ Joiner.on("\", " + "\"").join(params) + "\"];");
		sw.println("module.controller('" + controllerName + "', ctrlFunc);");
		sw.outdent();
		sw.println("}-*/;");

		sw.commit(logger);
		return typeName + AngularConventions.IMPL;
	}

	private String declareArgs(JMethod action) {
		StringBuilder args = new StringBuilder();
		for (int i = 0; i < action.getParameters().length; i++) {
			if (i > 0) {
				args.append(", ");
			}
			args.append("arg").append(i);
		}
		return args.toString();
	}

	private boolean isVoidMethod(JMethod method) {
		return method.getReturnType() == JPrimitiveType.VOID;
	}

	private Collection<JMethod> publicActionMethods(JClassType type,
			JMethod onInitMethod) {
		Collection<JMethod> methods = new ArrayList<JMethod>();
		for (JMethod method : type.getMethods()) {
			if (method == onInitMethod) {
				continue;
			}
			if (method.getAnnotation(NgWatch.class) != null) {
				continue;
			}
			if (method.isPublic() && !method.isAbstract() && !method.isStatic()) {
				methods.add(method);
			}
		}
		return methods;
	}

	private Collection<JMethod> watchMethods(JClassType type) {
		Collection<JMethod> methods = new ArrayList<JMethod>();
		for (JMethod method : type.getMethods()) {

			if (method.getAnnotation(NgWatch.class) == null) {
				continue;
			}

			methods.add(method);

		}
		return methods;
	}

	private JClassType findScopeClass(JMethod onInitMethod) {
		for (JParameter param : onInitMethod.getParameters()) {
			JClassType cType = param.getType().isClassOrInterface();
			if (isScope(cType)) {
				return cType;
			}
		}
		return null;
	}

	private JMethod findInitMethod(JClassType type, TreeLogger logger)
			throws UnableToCompleteException {
		JMethod onInit = null;
		for (JMethod method : type.getMethods()) {
			if (method.getName().equals("onInit")) {
				if (onInit == null) {
					onInit = method;
				} else {
					logger.log(TreeLogger.Type.ERROR, type.getName()
							+ " has two onInit methods.");
					throw new UnableToCompleteException();
				}
			}
		}
		return onInit;
	}

	private List<String> declareControllerParams(JParameter[] parameters) {
		List<String> params = new ArrayList<String>();
		for (JParameter param : parameters) {
			JClassType type = param.getType().isClassOrInterface();
			if (isScope(type)) {
				params.add("$scope");
			} else if (isElement(type)) {
				params.add("$element");
			} else {
				NgInject ngInject = type.getAnnotation(NgInject.class);
				if (ngInject != null) {
					params.add(ngInject.name());
				}
			}
		}
		return params;
	}

	private boolean isElement(JClassType ctype) {
		return ctype != null
				&& (ctype.isAssignableTo(types.elementType) || ctype
						.isAssignableTo(types.gwtElementType));
	}

	private boolean isScope(JClassType ctype) {
		return ctype != null && ctype.isAssignableTo(types.scopeType);
	}
}
