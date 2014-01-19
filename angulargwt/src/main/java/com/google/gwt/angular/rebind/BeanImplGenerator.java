package com.google.gwt.angular.rebind;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.user.rebind.SourceWriter;

class BeanImplGenerator {

	public static void  generateBeanImpl(TreeLogger logger, GeneratorContext context, AngularGwtTypes types,
			JClassType scopeClass, String simpleName, SourceWriter sw)
			throws UnableToCompleteException {
		// constructor
		sw.println("protected " + simpleName + "() {}");
	
		// getters and setters
		for (JMethod method : scopeClass.getMethods()) {
			if (isGetter(method)) {
				JPrimitiveType pType = method.getReturnType().isPrimitive();
				if (pType != null) {
					if (pType != JPrimitiveType.BOOLEAN) {
						sw.println("final public "
								+ pType.getSimpleSourceName() + " "
								+ method.getName() + "" + "() {");
						sw.indent();
						sw.println("return (" + pType.getSimpleSourceName()
								+ ") json().getNumber("
								+ quotedFieldName(method) + ");");
						sw.outdent();
						sw.println("}");
					} else if (pType == JPrimitiveType.BOOLEAN) {
						sw.println("final public boolean " + method.getName()
								+ "() {");
						sw.indent();
						sw.println("return json().getBoolean("
								+ quotedFieldName(method) + ");");
						sw.outdent();
						sw.println("}");
					} else {
						// shouldn't reach here (only void left)
						throw new UnableToCompleteException();
					}
				} else {
					JClassType cType = method.getReturnType()
							.isClassOrInterface();
					if (cType.isAssignableTo(types.stringType)) {
						sw.println("final public String " + method.getName()
								+ "() {");
						sw.indent();
						sw.println("return json().getString("
								+ quotedFieldName(method) + ");");
						sw.outdent();
						sw.println("}");
					} else {
						sw.println("final public "
								+ method.getReturnType()
										.getParameterizedQualifiedSourceName()
								+ " " + method.getName() + "" + "() {");
						sw.indent();
						generateDependentType(logger, context, types, method
								.getReturnType().isClassOrInterface());
						sw.println("return Util.reinterpret_cast(json().get("
								+ quotedFieldName(method) + "))" + ";");
						sw.outdent();
						sw.println("}");
						// handle arrays
						// handle nested models
					}
				}
			} else if (isSetter(method)) {
				JPrimitiveType pType = method.getParameters()[0].getType()
						.isPrimitive();
				if (pType != null) {
					if (pType != JPrimitiveType.BOOLEAN) {
						sw.println("final public " + fluentOrVoid(method) + " "
								+ method.getName() + "("
								+ pType.getSimpleSourceName() + " arg) {");
						sw.indent();
						sw.println("json().put(" + quotedFieldName(method)
								+ ", arg);");
						maybeFluentReturn(sw, method);
						sw.outdent();
						sw.println("}");
					} else if (pType == JPrimitiveType.BOOLEAN) {
						sw.println("final public " + fluentOrVoid(method) + " "
								+ method.getName() + "" + "(boolean "
								+ " arg) {");
						sw.indent();
						sw.println("json().put(" + quotedFieldName(method)
								+ ", arg);");
						maybeFluentReturn(sw, method);
						sw.outdent();
						sw.println("}");
					} else {
						// shouldn't reach here (only void left)
						throw new UnableToCompleteException();
					}
				} else {
					JClassType cType = method.getParameters()[0].getType()
							.isClassOrInterface();
					if (cType.isAssignableTo(types.stringType)) {
						sw.println("final public " + fluentOrVoid(method) + " "
								+ method.getName() + "(String arg) {");
						sw.indent();
						sw.println("json().put(" + quotedFieldName(method)
								+ ", arg);");
						maybeFluentReturn(sw, method);
						sw.outdent();
						sw.println("}");
					} else {
						String paramType = method.getParameters()[0].getType()
								.getParameterizedQualifiedSourceName();
						sw.println("final public " + fluentOrVoid(method) + " "
								+ method.getName() + "(" + paramType + " arg)"
								+ " {");
						sw.indent();
						generateDependentType(logger, context,types, method
								.getReturnType().isClassOrInterface());
	
						String arg = "Util.<JsonObject>reinterpret_cast(arg)";
						sw.println("json().put(" + quotedFieldName(method)
								+ ", " + arg + ");");
						maybeFluentReturn(sw, method);
						sw.outdent();
						sw.println("}");
						// handle arrays
						// handle nested models
					}
				}
			}
		}
	}

	private static void generateDependentType(TreeLogger logger,
			GeneratorContext context,AngularGwtTypes types, JClassType classOrInterface)
			throws UnableToCompleteException {
		if (classOrInterface != null) {
			if (classOrInterface.isAssignableTo(types.scopeType)) {
				ScopeGenerator.generateScope(logger, context, types, classOrInterface);
			} else if (classOrInterface.isAssignableTo(types.modelType)) {
				ModelGenerator.generateModelType(logger, context, types, classOrInterface);
			} else if (classOrInterface.isAssignableTo(types.arrayOfType)
					&& classOrInterface.isParameterized() != null) {
				generateDependentType(logger, context, types ,classOrInterface
						.isParameterized().getTypeArgs()[0]);
			}
		}
	}

	private static void maybeFluentReturn(SourceWriter sw, JMethod method) {
		if (method.getReturnType() != JPrimitiveType.VOID) {
			sw.println("return this;");
		}
	}

	private static String fluentOrVoid(JMethod method) {
		return method.getReturnType() != JPrimitiveType.VOID ? method
				.getEnclosingType().getSimpleSourceName() : "void";
	}

	private static String quotedFieldName(JMethod method) {
		String name = method.getName().substring(isBeanStyle(method) ? 3 : 0);
		return "\"" + Character.toLowerCase(name.charAt(0)) + name.substring(1)
				+ "\"";
	}

	private static boolean isSetter(JMethod method) {
		String name = method.getName();
		// traditional setFooField(type) JavaBean setter
		if (name.startsWith("set") && Character.isUpperCase(name.charAt(3))) {
			return method.getParameters().length == 1
					&& (method.getReturnType() == JPrimitiveType.VOID || method
							.getReturnType() == method.getEnclosingType());
			// non-traditional fluent-only EnclosingType fooField(type) setter
		} else if (method.getReturnType() == method.getEnclosingType()
				&& method.getParameters().length == 1) {
			return true;
		}
		return false;
	}

	private static boolean isGetter(JMethod method) {
		String name = method.getName();
		// traditional Type getFooField() JavaBean getter
		if (name.startsWith("get") && Character.isUpperCase(name.charAt(3))) {
			return method.getParameters().length == 0;
			// non traditional Type foo() getter, needs to have paired setter to
			// be detected correctly
		} else if (method.getParameters().length == 0) {
			// TODO: should enforce that a setter exists to disambiguate this
			// style of getter
			return true;
		}
		return false;
	}

	private static boolean isBeanStyle(JMethod method) {
		return method.getName().startsWith("get")
				|| method.getName().startsWith("set");
	}
	
}