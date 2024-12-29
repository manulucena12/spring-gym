package com.manu.requests.exercises;

public class NewExerciseRequest {

  private String name;
  private Long day;
  private int weight;
  private int repetitions;

  public NewExerciseRequest(String name, Long day, int weight, int repetitions) {
    this.name = name;
    this.day = day;
    this.weight = weight;
    this.repetitions = repetitions;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getRepetitions() {
    return repetitions;
  }

  public void setRepetitions(int repetitions) {
    this.repetitions = repetitions;
  }

  public int getWeight() {
    return weight;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }

  public Long getDay() {
    return day;
  }

  public void setDay(Long day) {
    this.day = day;
  }
}
