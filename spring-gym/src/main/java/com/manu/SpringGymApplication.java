package com.manu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringGymApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringGymApplication.class, args);
    System.out.println("Backend running properly");
    System.out.println("Documentation available in /swagger-ui/index.html");
  }
}
