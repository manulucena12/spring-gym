package com.manu.requests.auth;

public class RegisterRequest {

  private String name;
  private String email;
  private String password;
  private int age;
  private double weight;
  private int goal;
  private int days;

  public RegisterRequest(
      String name, String email, String password, int age, double weight, int goal, int days) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.age = age;
    this.weight = weight;
    this.goal = goal;
    this.days = days;
  }

  public String getName() {
    return name;
  }

  public int getGoal() {
    return goal;
  }

  public double getWeight() {
    return weight;
  }

  public int getAge() {
    return age;
  }

  public String getPassword() {
    return password;
  }

  public String getEmail() {
    return email;
  }

  public int getDays() {
    return days;
  }
}
