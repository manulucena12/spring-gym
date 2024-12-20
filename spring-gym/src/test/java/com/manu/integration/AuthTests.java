package com.manu.integration;

import com.manu.requests.auth.RegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthTests {

  @Autowired private WebTestClient webTestClient;

  @DisplayName("Creating a user for the first time works properly")
  @Test
  void registerTest() {
    var newUser =
        RegisterRequest.builder()
            .name("John Doe")
            .email("johndoe@gmail.com")
            .password("JD1234")
            .age(40)
            .weight(67)
            .goal(60)
            .build();
    webTestClient
        .post()
        .uri("/register")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(newUser)
        .exchange()
        .expectStatus()
        .isCreated();
  }

  @DisplayName("Registering with an existing email is forbidden")
  @Test
  void registerForbiddenTest() {
    var newUser =
        RegisterRequest.builder()
            .name("John Doe")
            .email("johndoe@gmail.com")
            .password("JD1234")
            .age(40)
            .weight(67)
            .goal(60)
            .build();
    webTestClient
        .post()
        .uri("/register")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(newUser)
        .exchange()
        .expectStatus()
        .isBadRequest();
  }
}
