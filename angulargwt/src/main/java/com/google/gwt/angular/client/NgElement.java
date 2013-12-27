package com.google.gwt.angular.client;

import elemental.dom.Element;

/**
 * Extension of Elemental's Element with Angular convenience functions.
 */
@NgInject(name = "$element")
public interface NgElement extends Element {
  void bind(String evt, Runnable run);

  <T> T inheritedData(String dataName);
}
