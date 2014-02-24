package com.google.gwt.angular.rebind;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gwt.angular.client.NgInject;
import com.google.gwt.angular.client.NgInjected;
import com.google.gwt.angular.client.NgWatch;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

class ControllerGenerator extends Generator {

	private static ControllerGenerator instance;

	public static ControllerGenerator get() {
		if(instance==null) 
			instance = new ControllerGenerator();
		return instance;
	}

	static final class Injection {
		public String localName;
		public String ngName;
		public JClassType type;
		
		public static final Function<Injection, String> toDeclaration = new Function<ControllerGenerator.Injection, String>() {
			@Override
			public String apply(Injection i) {
				return i.type.getQualifiedSourceName() + " " + i.localName;
			}
		};
		
		public static final Function<Injection, String> toAssignment = new Function<ControllerGenerator.Injection, String>() {
			@Override
			public String apply(Injection i) {
				return String.format("this.%s=%s;",i.localName,i.localName);
			}
		};
		
		public static final Function<Injection, String> toName = new Function<ControllerGenerator.Injection, String>() {
			@Override
			public String apply(Injection i) {
				return i.localName;
			}
		};
	}


	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
		AngularGwtTypes types = AngularGwtTypes.getInstanceFor(context);
		JClassType type = context.getTypeOracle().findType(typeName);

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
//		JMethod onInitMethod = findInitMethod(type, logger);
		JClassType scopeClass = findScopeClass(type,types);

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

		//generates a method to pull injections local
		List<Injection> injects = getInjections(type, types);

		sw.indent();
		sw.print("protected void onInject(");

		sw.print(Joiner.on(',').join(Lists.transform(injects,Injection.toDeclaration)));
		
		sw.println(") {");
		sw.indent();

		//assignments
		sw.println(Joiner.on('\n').join(Lists.transform(injects,Injection.toAssignment))); 
		
		sw.outdent();		

		sw.println("}");
		sw.outdent();		

		//start of register function
		sw.indent();
		sw.println("protected native void register(JavaScriptObject module) /*-{");
		sw.indent();
		sw.println("var self = this;");
	
		List<String> injList = Lists.transform(injects,Injection.toName);
		List<String> params = Lists.newArrayList("$scope");
		params.addAll(injList);

		//print controller function
		sw.print("var ctrlFunc =  $entry(function(");
		sw.print(Joiner.on(", ").join(params));
		sw.println(") {");
		sw.indent();

		//set the scope
		sw.println("self.@" + typeName + AngularConventions.IMPL + "::setScope" + "(*)($scope);");

		//assign injections
		sw.print("self.@" + typeName + AngularConventions.IMPL + "::onInject" + "(*)(");
		sw.print(Joiner.on(',').join(injList));
		sw.println(");");

		
		//call onInit
//		sw.print("self." + onInitMethod.getJsniSignature() + "(");
//		String controllerParams = Joiner.on(", ").join(params);
//		sw.print(controllerParams);
//		sw.println(");");

		//call initialize
		sw.println("self.@" + typeName + AngularConventions.IMPL + "::initialize" + "(*)($scope);");

		for (JMethod action : publicActionMethods(type)) {
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

	private static  List<Injection> getInjections(JClassType ctype, AngularGwtTypes types) {
		List<Injection> injects = new ArrayList<ControllerGenerator.Injection>();
		
		for(JField field:ctype.getFields()) {
			NgInjected injected = field.getAnnotation(NgInjected.class);
			if(injected!=null) {
				JClassType type = field.getType().isClassOrInterface();
				Injection in = new Injection();
				in.type = type;
				in.localName = field.getName();
				
				if (isScope(type,types)) {
					in.ngName = "$scope";
				} else if (isElement(type,types)) {
					in.ngName = "$element";
				} else {
					NgInject ngInject = type.getAnnotation(NgInject.class);
					if (ngInject != null) {
						in.ngName = ngInject.name();
					} else {
						//Placeholder class or interface
					}
				}
				injects.add(in);
			}
			
		}
		return injects;
	}



	private static List<String> declareControllerParams(JParameter[] parameters,AngularGwtTypes types) {
		List<String> params = new ArrayList<String>();
		for (JParameter param : parameters) {
			JClassType type = param.getType().isClassOrInterface();
			if (isScope(type,types)) {
				params.add("$scope");
			} else if (isElement(type,types)) {
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



	private static boolean  isElement(JClassType ctype, AngularGwtTypes types) {
		return ctype != null
				&& (ctype.isAssignableTo(types.elementType) || ctype
						.isAssignableTo(types.gwtElementType));
	}

	private static boolean isScope(JClassType ctype, AngularGwtTypes types) {
		return ctype != null && ctype.isAssignableTo(types.scopeType);
	}

	private static String declareArgs(JMethod action) {
		StringBuilder args = new StringBuilder();
		for (int i = 0; i < action.getParameters().length; i++) {
			if (i > 0) {
				args.append(", ");
			}
			args.append("arg").append(i);
		}
		return args.toString();
	}

	private static boolean isVoidMethod(JMethod method) {
		return method.getReturnType() == JPrimitiveType.VOID;
	}

	private static Collection<JMethod> publicActionMethods(JClassType type) {
		Collection<JMethod> methods = new ArrayList<JMethod>();
		for (JMethod method : type.getMethods()) {
			if (method.getAnnotation(NgWatch.class) != null) {
				continue;
			}
			if (method.isPublic() && !method.isAbstract() && !method.isStatic()) {
				methods.add(method);
			}
		}
		return methods;
	}

	private static Collection<JMethod> watchMethods(JClassType type) {
		Collection<JMethod> methods = new ArrayList<JMethod>();
		for (JMethod method : type.getMethods()) {

			if (method.getAnnotation(NgWatch.class) == null) {
				continue;
			}

			methods.add(method);

		}
		return methods;
	}

	private static JClassType findScopeClass(JClassType type,AngularGwtTypes types) {
		for (JClassType cType : type.getSuperclass().isParameterized().getTypeArgs()) {
			if (isScope(cType, types)) {
				return cType;
			}
		}
		return null;
	}

	private static JMethod findInitMethod(JClassType type, TreeLogger logger)
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

}