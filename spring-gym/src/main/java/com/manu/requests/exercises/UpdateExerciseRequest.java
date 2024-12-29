package com.manu.requests.exercises;

public class UpdateExerciseRequest {

  private String name;
  private int weight;
  private int repetitions;

  public UpdateExerciseRequest(String name, int weight, int repetitions) {
    this.name = name;
    this.weight = weight;
    this.repetitions = repetitions;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getWeight() {
    return weight;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }

  public int getRepetitions() {
    return repetitions;
  }

  public void setRepetitions(int repetitions) {
    this.repetitions = repetitions;
  }
}
