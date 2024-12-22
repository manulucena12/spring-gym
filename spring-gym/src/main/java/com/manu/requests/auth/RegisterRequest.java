package com.manu.requests.auth;

public class RegisterRequest {

  private String name;
  private String email;
  private String password;
  private int age;
  private int weight;
  private int goal;

  public RegisterRequest(
      String name, String email, String password, int age, int weight, int goal) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.age = age;
    this.weight = weight;
    this.goal = goal;
  }

  public String getName() {
    return name;
  }

  public int getGoal() {
    return goal;
  }

  public int getWeight() {
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
}
