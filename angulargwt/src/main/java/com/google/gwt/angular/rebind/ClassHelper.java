package com.google.gwt.angular.rebind;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;

/**
 * @author h0ru5
 *
 */
public class ClassHelper {

	public static String declareArgs(JMethod action) {
		StringBuilder args = new StringBuilder();
		for (int i = 0; i < action.getParameters().length; i++) {
			if (i > 0) {
				args.append(", ");
			}
			args.append("arg").append(i);
		}
		return args.toString();
	}

	public static boolean isVoidMethod(JMethod method) {
		return method.getReturnType() == JPrimitiveType.VOID;
	}

	public static Collection<JMethod> publicMethods(JClassType type) {
		Collection<JMethod> methods = new ArrayList<JMethod>();
		for (JMethod method : type.getMethods()) {
			if (method.isPublic() && !method.isAbstract() && !method.isStatic()) {
				methods.add(method);
			}
		}
		return methods;
	}

	public static JMethod methodByName(JClassType type, String methodName) {
		for (JMethod method : type.getMethods()) {
			if (method.getName().equals(methodName)) {
				return method;
			}
		}
		return null;
	}

}
