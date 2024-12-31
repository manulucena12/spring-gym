package com.manu.responses.weights;

import com.manu.entities.WeightEntity;
import java.util.List;

public class UserWeightContent<T> {

  private int goal;
  private String objective;
  private String kgLeft;
  private String started;
  private String last;
  private List<WeightEntity> weights;

  public String getObjective() {
    return objective;
  }

  public void setObjective(String objective) {
    this.objective = objective;
  }

  public String getKgLeft() {
    return kgLeft;
  }

  public void setKgLeft(String kgLeft) {
    this.kgLeft = kgLeft;
  }

  public UserWeightContent(
      int goal,
      String last,
      String started,
      List<WeightEntity> weights,
      String kgLeft,
      String objective) {
    this.goal = goal;
    this.last = last;
    this.started = started;
    this.weights = weights;
    this.kgLeft = kgLeft;
    this.objective = objective;
  }

  public int getGoal() {
    return goal;
  }

  public void setGoal(int goal) {
    this.goal = goal;
  }

  public List<WeightEntity> getWeights() {
    return weights;
  }

  public void setWeights(List<WeightEntity> weights) {
    this.weights = weights;
  }

  public String getLast() {
    return last;
  }

  public void setLast(String last) {
    this.last = last;
  }

  public String getStarted() {
    return started;
  }

  public void setStarted(String started) {
    this.started = started;
  }
}
