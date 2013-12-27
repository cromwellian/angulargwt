package com.google.gwt.angular.tipcalc.client;

import com.google.gwt.angular.client.Model;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;

/**
 */
public interface TipModel extends Model<TipModel> {
  @Min(1)
  int getBillAmount();

  void setBillAmount(int billAmount);

  int getNumPeople();

  void setNumPeople(int numPeople);

  double getPercentage();

  void setPercentage(double percentage);

  double getTip();

  void setTip(double tip);

  double getFinalBill();

  void setFinalBill(double finalBill);
}
