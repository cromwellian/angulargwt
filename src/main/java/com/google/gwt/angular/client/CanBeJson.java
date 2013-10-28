package com.google.gwt.angular.client;

import elemental.json.JsonObject;

/**
 * Implemented by all interfaces which can act like Json objects.
 */
public interface CanBeJson {
  JsonObject json();
}
