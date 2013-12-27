package com.google.gwt.angular.client;

/**
 * A scope object is essentially a Model object that can be injected and comes with some
 * methods builtin. The {@link NgInject} annotation here says that all subtypes of Scope will
 * still be named as '$scope'.
 */
@NgInject(name = "$scope",
    basetype = Scope.class)
public interface Scope<T extends Scope<T>> extends Model<T> {
  <S> void $watch(String expr, WatchFunction<S> func);
  <S> void $watch(String expr, WatchFunction<S> func, boolean objEq);

  void $apply(String expr);
}
