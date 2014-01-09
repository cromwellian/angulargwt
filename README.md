# AngularGWT
Write AngularJS Controllers, Filters, Directives in GWT

## What? (is it)

This is a library that enables Authors to write components or complete apps for [AngularJS][1] in Java. This is mostly done using compiler castings for [Google Web Toolkit][2] to "compile" Java Code into Javascript.

## Why? (GWT to write Angular modules)

AngularJS provides a MVVM framework for web apps, offering modularisation, IOC and allows to create reusable components while still allowing to create apps quickly. As every contemporary web app, AngularJS is built on Javascript.

GWT offers a way to write, test and distibute code in Java and compile it to Javascript. This library offers the possiblity to use GWT to create modules and apps for AngularJS using GWT.

Javascript is a very flexible and widespread language, however for mature languages like Java, a richer set of tooling regarding development, testing, versioning and distribution support is availiable. Also a large base of developers and libraries are availiable that can be migrated more easily.

## How? (to use)

Best way to start is to take a look at the examples.
However, a brief getting started here:
### Setting up a GWT project and add the library

#### Maven
The easiest way to build ibraries or apps is probably using a maven plugin, such as the [codehaus gwt-maven-plugin][3] or the [gwt-maven-plugin from Thomas Broyer][4]

Integrate the following dependency into your pom.xml (adjust version number accordingly). It is on sonatype oss and releases are mirrored into maven central.

```xml
<dependency>
   <groupId>com.github.h0ru5.gwt</groupId>
   <artifactId>angulargwt</artifactId>
   <version>1.1.1</version>
</dependency>
```

#### manually add the lib to your project
tbd

#### Ivy/Gradle/...
tbd

### Implementing a module and/or an application
Every project you write will provide an **AngularJS Module**. Just extend ``AngularModule`` and provide the module name and the dependencies with the ``@NgName`` and the ``@NgDepends`` annotations. Also put all provided components  (controllers, directives, services etc.) into the ``@NgDepends``.

For example:

```java
@NgName("mymodule")
@NgDepends({MyService.class, MyFilter.class, MyController.class, MyDirective.class})
public class MyModule implements AngularModule {
}
```

If you write not only a library but an **application**, you also need to provide a entry-point class. Just extend ``AngularApp`` to do so. You to override the abstract method ``main()``, which should return all referenced modules as an array.

```java
public class MyApp extends AngularApp {
   @Override
   protected AngularModule[] main() {
     return new AngularModule[] {(AngularModule) GWT.create(MyModule.class)};
   }
}
```

The template(s) are then provided in the same way as usual for AngularJS and need to include the GWT-generated script. You do not need to include angularjs from the cloud, it will be lazy-loaded if it is not found.

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

Each controller must implement the function ``onInit(Scope scope,...)``, in which it specifies the components to be injected.

Watches are defined by annotating a Method with the ``@Watch`` annotation. The first parameter is the expression to be watched, the second parameter is a boolean that enforces deep object equality. The Method will be called when the watch expression changes with new and old value as parameters.
Refer to [Angular's documentation on $watch][7] for background information.

```java
@NgInject(name = "MyCtrl")
public class MyController extends AngularController<MyScope> {
    public void onInit(MyScope scope, MyService service, Location Location, MyFilter filter) {...}

    @NgWatch(value = "persons", objEq = true)
    public void $watchPersons() {...}
    
    @NgWatch("location.path()")
    public void $watchPath(String path) {...}
    
    //arbitrary functions to be called via template
    public void clearPersons() {...}
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

Directives are defined as methods in classes that implement the tagging-interface ``Directive`` 
(it does not provide any abstract methods)
The  method is annotated as ``@NgDirective("name")``.

```java
public class MyDirective implements Directive {
    
    @NgDirective("displayPerson")
    public void display(final MyScope scope, final NgElement element, final JsonObject attrs) {
        //...
    }
    
}
```

## Supporting the project / to be done

 - Maven Archetype to auto-generate starter project.
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