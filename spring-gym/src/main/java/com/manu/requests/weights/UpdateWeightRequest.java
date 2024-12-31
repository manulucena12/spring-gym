package com.manu.requests.weights;

public class UpdateWeightRequest {

  private double weight;

  public UpdateWeightRequest(double weight) {
    this.weight = weight;
  }

  public double getWeight() {
    return weight;
  }

  public void setWeight(double weight) {
    this.weight = weight;
  }
}
