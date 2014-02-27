package com.google.gwt.angular.rebind;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gwt.angular.client.NgInject;
import com.google.gwt.angular.client.NgInjected;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.user.rebind.SourceWriter;

public final class Injection {
	public String localName;
	public String ngName;
	public JClassType type;
	
	public static Injection create(JClassType type,String localName,String ngName) {
		Injection inj = new Injection();
		inj.type=type;
		inj.localName = localName;
		inj.ngName=ngName;
		return inj;
	}
	
	public static final Function<Injection, String> toDeclaration = new Function<Injection, String>() {
		@Override
		public String apply(Injection i) {
			return i.type.getQualifiedSourceName() + " " + i.localName;
		}
	};
	
	public static final Function<Injection, String> toAssignment = new Function<Injection, String>() {
		@Override
		public String apply(Injection i) {
			return String.format("this.%s=%s;",i.localName,i.localName);
		}
	};
	
	public static final Function<Injection, String> toName = new Function<Injection, String>() {
		@Override
		public String apply(Injection i) {
			return i.ngName;
		}
	};

	public static void generateOnInject(SourceWriter sw, List<Injection> injects) {
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
	}
	
	public static List<Injection> getInjectedFields(JClassType dType) {
		List<Injection> injects = new ArrayList<Injection>();
		for (JField f : dType.getFields()) {
			if(f.isAnnotationPresent(NgInjected.class)) {
				JClassType pType = f.getType().isClassOrInterface();
				if (pType != null) {
					NgInject pInject = pType.getAnnotation(NgInject.class);
					
					if (pInject != null) {
						injects.add(Injection.create(pType, f.getName(),pInject.name()));
					}
				}
			}
		}
		return injects;
	}
}