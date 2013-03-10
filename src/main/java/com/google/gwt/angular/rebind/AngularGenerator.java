package com.google.gwt.angular.rebind;

import com.google.common.base.Joiner;
import com.google.gwt.angular.client.*;
import com.google.gwt.angular.client.impl.JsModelBase;
import com.google.gwt.angular.client.impl.JsScopeBase;
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
import elemental.json.JsonObject;
import elemental.util.ArrayOf;
import elemental.util.ArrayOfString;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AngularGenerator extends Generator {
  public static final String IMPL = "Impl";
  private static final String SCOPEIMPL = "Jso";
  private static final String FACTORY = "Factory";
  private static final String MODELIMPL = "Jso";
  private static final String MODULEIMPL = "Impl";
  private JClassType scopeType;
  private JClassType elementType;
  private JClassType gwtElementType;
  private JClassType stringType;
  private JClassType controllerType;
  private JClassType modelType;
  private JClassType moduleType;
  private JClassType factoryType;
  private JClassType arrayOfType;

  @Override
  public String generate(TreeLogger logger, GeneratorContext context, String typeName)
      throws UnableToCompleteException {
    TypeOracle typeOracle = context.getTypeOracle();
    scopeType = typeOracle.findType(Scope.class.getName());
    elementType = typeOracle.findType(elemental.dom.Element.class.getName());
    gwtElementType = typeOracle.findType(com.google.gwt.dom.client.Element.class.getName());
    stringType = typeOracle.findType(String.class.getName());
    controllerType = typeOracle.findType(AngularController.class.getName());
    modelType = typeOracle.findType(Model.class.getName());
    moduleType = typeOracle.findType(AngularModule.class.getName());
    factoryType = typeOracle.findType(Factory.class.getName());
    arrayOfType = typeOracle.findType(ArrayOf.class.getName());
    logger.log(TreeLogger.Type.DEBUG, "Generating " + typeName);
    JClassType type = typeOracle.findType(typeName);
    if (type.isAssignableTo(controllerType)) {
      return generateController(logger, context, typeName, type);
    } else if (type.isAssignableTo(scopeType)) {
      return generateScopeFactory(logger, context, type);
    } else if (type.isAssignableTo(modelType)) {
      return generateModelFactory(logger, context, type);
    } else if (type.isAssignableTo(moduleType)) {
      return generateModule(logger, context, type);
    }
    logger.log(TreeLogger.Type.ERROR, "Don't know how to create " + typeName);
    throw new UnableToCompleteException();
  }

  private String generateScopeFactory(TreeLogger logger, GeneratorContext context,
                                      JClassType type) throws UnableToCompleteException {
    ClassSourceFileComposerFactory fac =
        new ClassSourceFileComposerFactory(type.getPackage().getName(),
            type.getName() + FACTORY);
    fac.addImplementedInterface(factoryType.getQualifiedSourceName());
    fac.addImport(JsJsonObject.class.getName());
    fac.addImport(Util.class.getName());
    PrintWriter pw = context.tryCreate(logger, type.getPackage().getName(),
        type.getName() + FACTORY);
    SourceWriter sw = null;
    String typeName = type.getQualifiedSourceName() + FACTORY;
    if (pw != null) {
      sw = fac.createSourceWriter(context, pw);
    }
    if (sw == null) {
      return typeName;
    }

    sw.indent();
    sw.println("public native " + generateScope(logger, context, type) + " create() /*-{");
    sw.indent();


    sw.println("return {};");
    sw.outdent();
    sw.println("}-*/;");
    sw.outdent();
    sw.commit(logger);
    logger.log(TreeLogger.Type.DEBUG, "Generated " + typeName);
    return typeName;
  }

  private String generateModelFactory(TreeLogger logger, GeneratorContext context,
                                      JClassType type) throws UnableToCompleteException {
    ClassSourceFileComposerFactory fac =
        new ClassSourceFileComposerFactory(type.getPackage().getName(),
            type.getName() + FACTORY);
    fac.addImplementedInterface(factoryType.getQualifiedSourceName());
    fac.addImport(JsJsonObject.class.getName());
    fac.addImport(Util.class.getName());
    PrintWriter pw = context.tryCreate(logger, type.getPackage().getName(),
        type.getName() + FACTORY);
    SourceWriter sw = null;
    String typeName = type.getQualifiedSourceName() + FACTORY;
    if (pw != null) {
      sw = fac.createSourceWriter(context, pw);
    }
    if (sw == null) {
      return typeName;
    }

    sw.indent();
    sw.println("public native " + generateModelType(logger, context, type) + " create() /*-{");
    sw.indent();


    sw.println("return {};");
    sw.outdent();
    sw.println("}-*/;");
    sw.outdent();
    sw.commit(logger);
    logger.log(TreeLogger.Type.DEBUG, "Generated " + typeName);
    return typeName;
  }

  private String generateModule(TreeLogger logger, GeneratorContext context, JClassType type) {
    NgDepends deps = type.getAnnotation(NgDepends.class);
    String simpleName = type.getName() + MODULEIMPL;
    ClassSourceFileComposerFactory fac =
        new ClassSourceFileComposerFactory(type.getPackage().getName(),
            simpleName);
    fac.addImport(GWT.class.getName());
    fac.addImport(Util.class.getName());
    fac.addImplementedInterface(AngularModule.class.getName());
    PrintWriter pw = context.tryCreate(logger, type.getPackage().getName(),
        simpleName);
    SourceWriter sw = null;
    String typeName = type.getQualifiedSourceName() + MODULEIMPL;

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
          if (AngularController.class.isAssignableFrom(clazz)) {
            sw.println("args["+i+"].@" + AngularController.class.getName()+"::register" +
                "(Lcom/google/gwt/core/client/JavaScriptObject;)(module);");
          } else {
            sw.println("module.factory('" + ngInject.name() + "', function() {");
            sw.indent();
            sw.println(" return args[" + i + "];");
            sw.outdent();
            sw.println("});");
          }
        }
        i++;
      }
    }
    sw.println("}-*/;");
    sw.outdent();
    sw.commit(logger);
    return typeName;
  }

  private String generateModelType(TreeLogger logger, GeneratorContext context,
                                   JClassType modelType) throws UnableToCompleteException {
    String simpleName = modelType.getName() + MODELIMPL;
    ClassSourceFileComposerFactory fac =
        new ClassSourceFileComposerFactory(modelType.getPackage().getName(),
            simpleName);
    fac.setSuperclass(JsModelBase.class.getName() + "<" + modelType.getName() + ">");
    fac.addImport(JsonObject.class.getName());
    fac.addImport(ArrayOfString.class.getName());
    fac.addImport(JsModelBase.class.getName());
    fac.addImport(Util.class.getName());
    fac.addImplementedInterface(modelType.getQualifiedSourceName());
    PrintWriter pw = context.tryCreate(logger, modelType.getPackage().getName(),
        simpleName);
    SourceWriter sw = null;
    String typeName = modelType.getQualifiedSourceName() + MODELIMPL;

    if (pw != null) {
      sw = fac.createSourceWriter(context, pw);
    }
    if (sw == null) {
      return typeName;
    }
    generateBeanImpl(logger, context, modelType, simpleName, sw);

    sw.commit(logger);
    return typeName;
  }

  private String generateController(TreeLogger logger, GeneratorContext context, String typeName,
                                    JClassType type) throws UnableToCompleteException {
    ClassSourceFileComposerFactory fac =
        new ClassSourceFileComposerFactory(type.getPackage().getName(),
            type.getName() + IMPL);
    fac.setSuperclass(typeName);
    fac.addImport(JavaScriptObject.class.getName());
    PrintWriter pw = context.tryCreate(logger, type.getPackage().getName(), type.getName() + IMPL);
    SourceWriter sw = null;
    if (pw != null) {
      sw = fac.createSourceWriter(context, pw);
    }
    if (sw == null) {
      return typeName + IMPL;
    }
    String controllerName = type.getSimpleSourceName();
    NgInject ngInject = type.getAnnotation(NgInject.class);
    if (ngInject != null) {
      controllerName = ngInject.name();
    }
        /*
         * Generates code that looks similar to this:
         *  var self=this;
         * $wnd.TodoController = $entry(function ($scope, $element) {
         * var wrapScope = @com.google.gwt.angular.client.todomvc.BodyScopeAdapter::new(Lelemental/json/JsonObject;)($scope);
         * self.@com.google.gwt.angular.client.AngularController::setScope(Lcom/google/gwt/angular/client/Scope;)(wrapScope);
         * self.@com.google.gwt.angular.client.todomvc.TodoController::onInit(Lcom/google/gwt/angular/client/TodoScope;Lcom/google/gwt/dom/client/Element;)(wrapScope, $element);
         * $scope.doSomething = $entry(function() {
         *   return self.@com.google.gwt.angular.client.todomvc.TodoController::doSomething()();
         * });
         * });
         * $wnd.TodoController.$inject = ['$scope', '$element'];
         */
    JMethod onInitMethod = findInitMethod(type, logger);
    JClassType scopeClass = findScopeClass(onInitMethod);

    String scopeAdapter = null;
    if (scopeClass != null) {
      scopeAdapter = generateScope(logger, context, scopeClass);
    }
    // this override teaches the compiler that TodoScope has been instantiated so it is not pruned
    sw.indent();
    sw.println("protected void setScope(" + scopeAdapter + " jsoScope) { super.setScope" +
        "(jsoScope); " +
        "}");
    sw.outdent();

    sw.indent();
    sw.println("protected native void register(JavaScriptObject module) /*-{");
    sw.indent();
    sw.println("var self = this;");
    sw.print("var ctrlFunc =  $entry(function(");
    List<String> params = declareControllerParams(onInitMethod.getParameters());
    sw.print(Joiner.on(", ").join(params));
    sw.println(") {");
    sw.indent();
    sw.println(
        "self.@" + typeName + IMPL + "::setScope" +
            "(*)($scope);");
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
      sw.println("$scope.$watch('" + watchParams.value() + "', $entry(function("
          + argString + ") {");
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
    sw.println(
        "ctrlFunc.$inject = [\"" + Joiner.on("\", " +
            "\"").join(params) + "\"];");
    sw.println("module.controller('" + controllerName + "', ctrlFunc);");
    sw.outdent();
    sw.println("}-*/;");

    sw.commit(logger);
    return typeName + IMPL;
  }

  private String declareArgs(JMethod action) {
    StringBuilder args = new StringBuilder();
    for (int i = 0; i < action.getParameters().length; i++) {
      if (i > 0) {
        args.append(", ");
      }
      args.append("arg" + i);
    }
    return args.toString();
  }

  private boolean isVoidMethod(JMethod method) {
    return method.getReturnType() == JPrimitiveType.VOID;
  }

  private Collection<JMethod> publicActionMethods(JClassType type, JMethod onInitMethod) {
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

  private boolean isSetter(JMethod method) {
    String name = method.getName();
    // traditional setFooField(type) JavaBean setter
    if (name.startsWith("set") && Character.isUpperCase(name.charAt(3))) {
      return method.getParameters().length == 1 &&
          (method.getReturnType() == JPrimitiveType.VOID || method.getReturnType() == method
              .getEnclosingType());
      // non-traditional fluent-only EnclosingType fooField(type) setter
    } else if (method.getReturnType() == method.getEnclosingType() &&
        method.getParameters().length == 1) {
      return true;
    }
    return false;
  }

  private boolean isGetter(JMethod method) {
    String name = method.getName();
    // traditional Type getFooField() JavaBean getter
    if (name.startsWith("get") && Character.isUpperCase(name.charAt(3))) {
      return method.getParameters().length == 0;
      // non traditional Type foo() getter, needs to have paired setter to be detected correctly
    } else if (method.getParameters().length == 0) {
      // TODO: should enforce that a setter exists  to disambiguate this style of getter
      return true;
    }
    return false;
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

  private String generateScope(TreeLogger logger, GeneratorContext context,
                               JClassType scopeClass)
      throws UnableToCompleteException {
    String simpleName = scopeClass.getName() + SCOPEIMPL;
    ClassSourceFileComposerFactory fac =
        new ClassSourceFileComposerFactory(scopeClass.getPackage().getName(),
            simpleName);
    fac.setSuperclass(JsScopeBase.class.getName() + "<" + scopeClass.getName() + ">");
    fac.addImport(JsonObject.class.getName());
    fac.addImport(ArrayOfString.class.getName());
    fac.addImport(JsScopeBase.class.getName());
    fac.addImport(Util.class.getName());
    fac.addImplementedInterface(scopeClass.getQualifiedSourceName());
    PrintWriter pw = context.tryCreate(logger, scopeClass.getPackage().getName(),
        simpleName);
    SourceWriter sw = null;
    String typeName = scopeClass.getQualifiedSourceName() + SCOPEIMPL;

    if (pw != null) {
      sw = fac.createSourceWriter(context, pw);
    }
    if (sw == null) {
      return typeName;
    }
    generateBeanImpl(logger, context, scopeClass, simpleName, sw);

    sw.commit(logger);
    return typeName;
  }

  private void generateBeanImpl(TreeLogger logger, GeneratorContext context, JClassType scopeClass,
                                String simpleName, SourceWriter sw)
      throws UnableToCompleteException {
    // constructor
    sw.println("protected " + simpleName + "() {}");

    // getters and setters
    for (JMethod method : scopeClass.getMethods()) {
      if (isGetter(method)) {
        JPrimitiveType pType = method.getReturnType().isPrimitive();
        if (pType != null) {
          if (pType != JPrimitiveType.BOOLEAN) {
            sw.println("final public " + pType.getSimpleSourceName() + " " + method.getName() + "" +
                "() {");
            sw.indent();
            sw.println("return (" + pType.getSimpleSourceName() + ") json().getNumber(" +
                quotedFieldName(method) + ");");
            sw.outdent();
            sw.println("}");
          } else if (pType == JPrimitiveType.BOOLEAN) {
            sw.println("final public boolean " + method.getName() + "() {");
            sw.indent();
            sw.println("return json().getBoolean(" + quotedFieldName(method) + ");");
            sw.outdent();
            sw.println("}");
          } else {
            // shouldn't reach here (only void left)
            throw new UnableToCompleteException();
          }
        } else {
          JClassType cType = method.getReturnType().isClassOrInterface();
          if (cType.isAssignableTo(stringType)) {
            sw.println("final public String " + method.getName() + "() {");
            sw.indent();
            sw.println("return json().getString(" + quotedFieldName(method) + ");");
            sw.outdent();
            sw.println("}");
          } else {
            sw.println(
                "final public " + method.getReturnType().getParameterizedQualifiedSourceName() +
                    " " +
                    method.getName() + "" +
                    "() {");
            sw.indent();
            generateDependentType(logger, context, method.getReturnType().isClassOrInterface());
            sw.println("return Util.reinterpret_cast(json().get(" + quotedFieldName(method) + "))" +
                ";");
            sw.outdent();
            sw.println("}");
            // handle arrays
            // handle nested models
          }
        }
      } else if (isSetter(method)) {
        JPrimitiveType pType = method.getParameters()[0].getType().isPrimitive();
        if (pType != null) {
          if (pType != JPrimitiveType.BOOLEAN) {
            sw.println(
                "final public " + fluentOrVoid(method) + " " + method.getName() + "(" + pType
                    .getSimpleSourceName() +
                    " arg) {");
            sw.indent();
            sw.println("json().put(" + quotedFieldName(method) + ", arg);");
            maybeFluentReturn(sw, method);
            sw.outdent();
            sw.println("}");
          } else if (pType == JPrimitiveType.BOOLEAN) {
            sw.println("final public " + fluentOrVoid(method) + " " + method.getName() + "" +
                "(boolean "
                + " arg) {");
            sw.indent();
            sw.println("json().put(" + quotedFieldName(method) + ", arg);");
            maybeFluentReturn(sw, method);
            sw.outdent();
            sw.println("}");
          } else {
            // shouldn't reach here (only void left)
            throw new UnableToCompleteException();
          }
        } else {
          JClassType cType = method.getParameters()[0].getType().isClassOrInterface();
          if (cType.isAssignableTo(stringType)) {
            sw.println(
                "final public " + fluentOrVoid(method) + " " + method.getName() + "(String arg) {");
            sw.indent();
            sw.println("json().put(" + quotedFieldName(method) + ", arg);");
            maybeFluentReturn(sw, method);
            sw.outdent();
            sw.println("}");
          } else {
            String paramType =
                method.getParameters()[0].getType().getParameterizedQualifiedSourceName();
            sw.println("final public " + fluentOrVoid(method) + " " + method.getName() + "(" +
                paramType +
                " arg)" +
                " {");
            sw.indent();
            generateDependentType(logger, context, method.getReturnType().isClassOrInterface());

            String arg = "Util.<JsonObject>reinterpret_cast(arg)";
            sw.println("json().put(" + quotedFieldName(method) + ", " + arg + ");");
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

  private void generateDependentType(TreeLogger logger, GeneratorContext context,
                                     JClassType classOrInterface) throws UnableToCompleteException {
    if (classOrInterface != null) {
      if (classOrInterface.isAssignableTo(scopeType)) {
        generateScope(logger, context, classOrInterface);
      } else if (classOrInterface.isAssignableTo(modelType)) {
        generateModelType(logger, context, classOrInterface);
      } else if (classOrInterface.isAssignableTo(arrayOfType) &&
          classOrInterface.isParameterized() != null) {
        generateDependentType(logger, context, classOrInterface.isParameterized().getTypeArgs()[0]);
      }
    }
  }

  private void maybeFluentReturn(SourceWriter sw, JMethod method) {
    if (method.getReturnType() != JPrimitiveType.VOID) {
      sw.println("return this;");
    }
  }

  private String fluentOrVoid(JMethod method) {
    return method.getReturnType() != JPrimitiveType.VOID ? method.getEnclosingType()
        .getSimpleSourceName() : "void";
  }

  private String quotedFieldName(JMethod method) {
    String name = method.getName().substring(isBeanStyle(method) ? 3 : 0);
    return "\"" + Character.toLowerCase(name.charAt(0)) + name.substring(1) + "\"";
  }

  private boolean isBeanStyle(JMethod method) {
    return method.getName().startsWith("get") || method.getName().startsWith("set");
  }

  private JMethod findInitMethod(JClassType type, TreeLogger logger)
      throws UnableToCompleteException {
    JMethod onInit = null;
    for (JMethod method : type.getMethods()) {
      if (method.getName().equals("onInit")) {
        if (onInit == null) {
          onInit = method;
        } else {
          logger.log(TreeLogger.Type.ERROR, type.getName() + " has two onInit methods.");
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
    return ctype != null && (ctype.isAssignableTo(elementType)
        || ctype.isAssignableTo(gwtElementType));
  }

  private boolean isScope(JClassType ctype) {
    return ctype != null && ctype.isAssignableTo(scopeType);
  }
}
