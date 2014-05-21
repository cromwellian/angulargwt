package com.google.gwt.angular.client;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to teach the code generators that this field is to be injected.
 * So for example, if you are trying to inject type T, declare your property:
 * <code>
 * 
 * {@code @NgInjected}
 * public T myfield;
 * 
 * </code>
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NgInjected {
}
