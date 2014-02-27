package com.google.gwt.angular.tipcalc.client;

import com.google.gwt.angular.client.AngularController;
import com.google.gwt.angular.client.NgInject;
import com.google.gwt.angular.client.NgWatch;
import com.google.gwt.angular.client.Util;
import com.google.gwt.core.client.GWT;

/**
 * Controller used for TipCalc
 */
@NgInject(name = "TipCtrl")
public class TipCalcController extends AngularController<TipCalcScope> {

	@NgWatch(value = "tipModel", objEq = true)
	public void $watchTipModel(TipModel tipModel) {
		tipModel.setTip(tipModel.getBillAmount() * (tipModel.getPercentage()/100));
		tipModel.setFinalBill(tipModel.getBillAmount() + tipModel.getTip());
	}

	@Override
	protected void initialize(TipCalcScope scope) {
		TipModel tipModel = Util.make(GWT.create(TipModel.class));
		scope.setTipModel(tipModel);
		tipModel.setBillAmount(1100);
	}
}
