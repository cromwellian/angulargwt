package com.google.gwt.angular.client;

/**
 * Represents Angular's $timeout service.
 */
@NgInject(name = "$timeout")
public interface NgTimeout {
  void schedule(Runnable runnable, int millis, boolean flag);
}
