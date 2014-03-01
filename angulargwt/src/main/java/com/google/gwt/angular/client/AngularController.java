package com.google.gwt.angular.client;

import com.google.gwt.core.client.JavaScriptObject;
import elemental.util.ArrayOf;

import java.util.Iterator;

/**
 * Base class for all implementations of AngularJs conntrollers in GWT. To use,
 * subclass {@code AngularController} and define the abstract methods.
 * Each public field annotated as {@link NgInjected} is assumed to be an injectable type, either
 * a builtin type, or a custom injectable type defined by {@link NgInject}.
 * <p/>
 * <p>Any additional public methods defined will automatically be exported to the <code>$scope</code> object used by
 * the controller and callable from the view.
 * <p/>
 * <pre>
 *     public class MyController extends AngularController {
 *         public void initialize(Scope scope) {
 *             // publish key "foo" with value "bar" into $scope
 *             scope.put("foo", "bar");
 *         }
 *
 *         // automatically published as $scope.doIt
 *         public void doIt(String arg) {
 *             Window.alert(scope.getString("foo") + " " + arg);
 *         }
 *     }
 * </pre>
 * <p/>
 * See the documentation for {@link Scope} for how to define custom Scope subtypes with type-safe accessors.
 *
 * @author Ray Cromwell (cromwellian@gmail.com)
 */
public abstract class AngularController<T extends Scope> {
  // convenience variable for accessing scope in published controller methods
  protected T scope;

  protected AngularController() {
  }

  protected void setScope(T scope) {
    this.scope = scope;
  }

  protected abstract void initialize(T scope);
  
  protected void register(JavaScriptObject module) {
  }

  protected <S> Iterable<S> iterable(final ArrayOf<S> array) {
    return new Iterable<S>() {
      public Iterator<S> iterator() {
        return new Iterator<S>() {
          int next = 0;

          public boolean hasNext() {
            return next < array.length();
          }

          public S next() {
            return array.get(next++);
          }

          public void remove() {
          }
        };
      }
    };
  }
}
