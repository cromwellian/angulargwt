package com.google.gwt.angtulargwt.jsr303.rebind.test;

import com.google.gwt.angular.client.Model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 */
public interface TipModel extends Model<TipModel> {
  @Min(1)
  int getBillAmount();
  void setBillAmount(int billAmount);

  @Min(1)
  int getNumPeople();
  void setNumPeople(int numPeople);

  @Min(0)
  double getPercentage();
  void setPercentage(double percentage);

  double getTip();
  void setTip(double tip);

  double getFinalBill();
  void setFinalBill(double finalBill);
}
