package com.manu.requests.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {

  private String name;
  private String email;
  private String password;
  private int age;
  private int weight;
  private int goal;
}
