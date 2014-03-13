package com.google.gwt.angular.rebind;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;

public class AngularGenerator extends Generator {

	private AngularGwtTypes types;

	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {

		types=AngularGwtTypes.createFor(context);

		JClassType type = context.getTypeOracle().findType(typeName);
		logger.log(TreeLogger.Type.DEBUG, "Generating " + typeName);
		
		if (type.isAssignableTo(types.controllerType)) {
			return ControllerGenerator.get().generate(logger, context, typeName);
		} else if (type.isAssignableTo(types.scopeType)) {
			return ScopeGenerator.get().generate(logger, context, typeName);
		} else if (type.isAssignableTo(types.modelType)) {
			return ModelGenerator.get().generate(logger, context, typeName);
		} else if (type.isAssignableTo(types.moduleType)) {
			return ModuleGenerator.get().generate(logger, context, typeName);
		}
		logger.log(TreeLogger.Type.ERROR, "Don't know how to create "
				+ typeName);
		throw new UnableToCompleteException();
	}
}
