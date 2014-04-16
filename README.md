# AngularGWT
Write AngularJS Controllers, Filters, Directives in GWT

## What? (is it)

This is a library that enables Authors to write components or complete apps for [AngularJS][1] in Java. This is mostly done using compiler castings for [Google Web Toolkit][2] to "compile" Java Code into Javascript.

## Why? (GWT to write Angular modules)

AngularJS provides a MVVM framework for web apps, offering modularisation, IOC and allows to create reusable components while still allowing to create apps quickly. As every contemporary web app, AngularJS is built on Javascript.

GWT offers a way to write, test and distibute code in Java and compile it to Javascript. This library offers the possiblity to use GWT to create modules and apps for AngularJS using GWT.

Javascript is a very flexible and widespread language, however for mature languages like Java, a richer set of tooling regarding development, testing, versioning and distribution support is availiable. Also a large base of developers and libraries are availiable that can be migrated more easily.

## How? (to use)

Best way to start is to take a look at the archetypes.

### Archetypes

There are two archetypes:
* ```angulargwt-module-archetype``` for a **module** project, where the GWT code is used by a AngularJS app in javascript 
* ```angulargwt-app-archetype``` for an **app** project, where the top-level-module is written in GWT

As usual ``mvn archetype:generate`` or an IDE such as m2e or NetBeans can be used for the archetypes.

### Other ways to set up AngularGWT projects

#### Maven
You can build AngularGWT libraries or apps is using a maven plugin, such as the [codehaus gwt-maven-plugin][3] or the [gwt-maven-plugin from Thomas Broyer][4]

Integrate the following dependency into your pom.xml (adjust version number accordingly). It is on sonatype oss and releases are mirrored into maven central.

```xml
<dependency>
   <groupId>com.github.h0ru5.gwt</groupId>
   <artifactId>angulargwt</artifactId>
   <version>1.1.3</version>
</dependency>
```

Current SNAPSHOT state: [![Build Status](https://buildhive.cloudbees.com/job/h0ru5/job/angulargwt/badge/icon)](https://buildhive.cloudbees.com/job/h0ru5/job/angulargwt/)

#### manually add the lib to your project
tbd

#### Ivy/Gradle/...
tbd

### Inheriting the GWT module
This inherits declaration is needed in your &lt;yourmodule&gt;.gwt.xml 
(note the com.google namespace)

```xml
<inherits name="com.google.gwt.angular.angulargwt" />
```

### Creating an AngularJS module
Every project you write will provide at least one **AngularJS Module**. Just extend ``AngularModule`` and provide the module name and the dependencies with the ``@NgName`` and the ``@NgDepends`` annotations. Also put all provided components  (controllers, directives, services etc.) into the ``@NgDepends``.

For example:

```java
@NgName("mymodule")
@NgDepends({MyService.class, MyFilter.class, MyController.class, MyDirective.class})
public class MyModule implements AngularModule {
}
```

### Choosing a module or an application as project type

There are three ways a project is used:

1. As Java library in another AngularGWT project
2. As javascript containing AngularGWT modules used by an AngularJS app
3. As javascript containing AngularGWT modules incl. the top-module

For compillation to javascript, a GWT  entry point is needed. 

#### App project

If you write not only a library module but an **application** (use case 3), you need to provide a entry-point class by extending ``AngularApp``. You need to override the abstract method ``main()``, which should return all referenced AngularGWT modules as an array.

```java
public class MyApp extends AngularApp {
   @Override
   protected AngularModule[] main() {
     return new AngularModule[] {(AngularModule) GWT.create(MyModule.class)};
   }
}
```

The template(s) are then provided in the same way as usual for AngularJS and need to include the GWT-generated script. You do not need to include angularjs in the head, it will be lazy-loaded from the CDN if it is not detected.

#### A library of AngularGWT modules

For all other projects (use cases 1 or 2), you write a **module** and extend an entry-point in the same way from ``AngularEntryPoint``

```java
public class MyLib extends AngularEntryPoint {
   @Override
   protected AngularModule[] main() {
     return new AngularModule[] {(AngularModule) GWT.create(MyModule.class)};
   }
}
```

If you use this lib in Java (use case 1), you just add the classes and sources to the classpath (or let Maven do it).

If you use the compiled library as javascript in a regular AngularJS app (use case 2) then you have to delay the bootstrapping until the async loading of GWT has finished, this is done via a callback called ``angularGwtModuleLoaded``.

The script ``angulargwt.js`` provides a very basic handler for this purpose.

So your AngularJS-app **must not** use ``<body ng-app="MyApp">``, but instead include this:

```html
<script type="text/javascript" src="angulargwt.js"></script>
<script type="text/javascript">	
		// This must be called instead of ng-app!
		angulargwt('myApp');
</script>
```

Refer to [bootstrap process documentation](http://docs.angularjs.org/guide/bootstrap) of AngularJS for background.

### Implementing the different types of AngularJS components

Refer to [AngularJS concepts][5] for a basic intro of the different types of components

#### Model
A model is defined by its interface, which must extend from ``Model<>`` and use the own type as type parameter for the superinterface.

The Model is generated during compilation and in code it is invoked via [deferred binding][6], which means you instatiate a Model via  ``GWT.create()``.

```java
public interface Person extends Model<Person> {
   String getName();
   Person setName(String name);
   
   //or
   
   int age();
   Person age(int age); 
}
```

#### Scope
Similar to the Model, a Scope is defined by contract (by its interface) and generated in the compilation process.
Note that getters and setters can be implicit or explicit and use "fluent" or "void-setter" style.

```java
public interface MyScope extends Scope<MyScope> {
   void setPersons(ArrayOf<Person> persons);
   ArrayOf<Person> getPersons();
   
   //or
   
   int maxPersons();
   MyScope maxPersons(int max);
}
```

#### Controller

Controllers extend the class ``ÀngularController<>`` and refer to their scope as type argument. The name to be used in the template is specified via ```@NgInject(name="ControllerName")```.

Each controller must implement the abstract function ``initialize(Scope scope)``, in which it initializes the scope.

To specify a component to be injected, it must be declared as a public field and annotated with ``@NgInjected``

Watches are defined by annotating a Method with the ``@Watch`` annotation. The first parameter is the expression to be watched, the second parameter is a boolean that enforces deep object equality. The Method will be called when the watch expression changes with new and old value as parameters.
Refer to [Angular's documentation on $watch][7] for background information.

```java
@NgInject(name = "MyCtrl")
public class MyController extends AngularController<MyScope> {
    
    @NgInjected
    public MyService service;
    
    @Override
    public void initialize(MyScope scope) {...}

    @NgWatch(value = "persons", objEq = true)
    public void $watchPersons() {...}
    
    @NgWatch("location.path()")
    public void $watchPath(String path) {...}
    
    //arbitrary functions to be called from the template
    public void clearPersons() {...}
}
```

#### Filter
A Filter is implementing the Filter interface with a type parameter for the Model it is filtering. It is injectable by annotating the class with ```@NgInject(name="filterName")```.

```java
@NgInject(name = "todoFilter")
public class TodoFilter implements Filter<Todo> {
          
        public ArrayOf<Todo> filter(ArrayOf<Todo> todos, Todo todoFilterArg) {
            ArrayOf<Todo> result = JsArrayOf.create();
            
            //Filtering, e.g. by compairing with the Argument
            
            return result;
        }
}
```

#### Service
A service can provide Util methods and is just implemented as POJO and annotated with the name used for injection into the controllers.

```java
@NgInject(name = "serverProxy")
public class ServerProxy {
    public ArrayOf<Todo> get() {...}
    public void put(ArrayOf<Todo> todos) {...}
}
```

#### Directive

Directives are defined as classes that implement the interface ``Directive``. 
The class must also be annotated as ``@NgDirective("name")``.

Other components can be injected similar to the controller.

```java
@NgDirective("displayPerson")
public class MyDirective implements Directive {

    @Override
    public void init() {}

    @Override
    public void link(final MyScope scope, final ArrayOf<NgElement> element, final JsonObject attrs) {
        //...
    }
}
```

## Supporting the project / to be done

 - More JavaDoc
 - Unit tests.
 - Transitive dependencies, modules that depend on modules.
 - JSO wrappers for the rest of the AngularJs API. (so far only what was needed for TodoMVC is implemented)

## License

Since GWT is using Apache Licence and AngularJS is using MIT Licence, the usage of this code code should conform this both licences.
I am happy for any legal advice on the proper licencing.

## Kudos

I forked this code from [nordligulv][8], which is a fork from [cromwellian][9], I merely buffed things up a bit and added this doc.

> Written with [StackEdit](https://stackedit.io/).


  [1]: http://angularjs.org/ "AngularJS"
  [2]: http://www.gwtproject.org/ "GWT"
  [3]: http://mojo.codehaus.org/gwt-maven-plugin/
  [4]: https://github.com/tbroyer/gwt-maven-plugin
  [5]: http://docs.angularjs.org/guide/concepts
  [6]: http://www.gwtproject.org/doc/latest/DevGuideCodingBasicsDeferred.html
  [7]: http://docs.angularjs.org/api/ng.$rootScope.Scope#methods_$watch
  [8]: https://github.com/nordligulv/angulargwt%20nordligulv
  [9]: https://github.com/cromwellian/angulargwt%20cromweilian
