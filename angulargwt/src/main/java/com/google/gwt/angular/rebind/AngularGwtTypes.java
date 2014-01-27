package com.google.gwt.angular.rebind;

import com.google.gwt.angular.client.AngularController;
import com.google.gwt.angular.client.AngularModule;
import com.google.gwt.angular.client.Factory;
import com.google.gwt.angular.client.Model;
import com.google.gwt.angular.client.Scope;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;

import elemental.util.ArrayOf;

public class AngularGwtTypes {
	private static AngularGwtTypes instance;
	
	public JClassType scopeType;
	public JClassType elementType;
	public JClassType gwtElementType;
	public JClassType stringType;
	public JClassType controllerType;
	public JClassType modelType;
	public JClassType moduleType;
	public JClassType factoryType;
	public JClassType arrayOfType;

	private AngularGwtTypes() {}
	
	public static AngularGwtTypes createFor(GeneratorContext context) {
		AngularGwtTypes types = new AngularGwtTypes();
		TypeOracle typeOracle = context.getTypeOracle();
		types.scopeType = typeOracle.findType(Scope.class.getName());
		types.elementType = typeOracle
				.findType(elemental.dom.Element.class.getName());
		types.gwtElementType = typeOracle
				.findType(com.google.gwt.dom.client.Element.class.getName());
		types.stringType = typeOracle.findType(String.class.getName());
		types.controllerType = typeOracle.findType(AngularController.class.getName());
		types.modelType = typeOracle.findType(Model.class.getName());
		types.moduleType = typeOracle.findType(AngularModule.class.getName());
		types.factoryType = typeOracle.findType(Factory.class.getName());
		types.arrayOfType = typeOracle.findType(ArrayOf.class.getName());
		instance=types;
		return types;
	}

	public static AngularGwtTypes getInstanceFor(GeneratorContext context) {
		if(instance!=null) // && check if same context?
			return instance;
		else 
			return createFor(context);
			
	}
}