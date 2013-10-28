package com.google.gwt.angular.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Base class for parser functions.
 */
public class JsNgParserFunction<T> extends JavaScriptObject {
  protected JsNgParserFunction() {}
  public static native <S> JsNgParserFunction<S> parserFunc(NgParserFunction<S> func) /*-{
     return function(newValue) {
        return func.@com.google.gwt.angular.client.NgParserFunction::parse(Ljava/lang/Object;)(newValue);
     };
  }-*/;
}
