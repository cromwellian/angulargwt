package com.google.gwt.angular.client.tipcalc;

import com.google.gwt.angular.client.Scope;

/**
 * TipCalcScope.
 */
public interface TipCalcScope extends Scope<TipCalcScope> {
  TipModel getTipModel();
  void setTipModel(TipModel model);
}
