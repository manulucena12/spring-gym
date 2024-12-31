package com.manu.requests.weights;

public class NewWeightRequest {

  private double weight;
  private Long owner;

  public NewWeightRequest(double weight, Long owner) {
    this.weight = weight;
    this.owner = owner;
  }

  public double getWeight() {
    return weight;
  }

  public void setWeight(double weight) {
    this.weight = weight;
  }

  public Long getOwner() {
    return owner;
  }

  public void setOwner(Long owner) {
    this.owner = owner;
  }
}
