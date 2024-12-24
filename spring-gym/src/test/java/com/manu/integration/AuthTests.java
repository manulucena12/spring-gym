package com.manu.integration;

import com.manu.repositories.UserRepository;
import com.manu.requests.auth.LoginRequest;
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

  @Autowired private UserRepository userRepository;

  @DisplayName("Creating a user for the first time works properly")
  @Test
  void registerTest() {
    var newUser = new RegisterRequest("John Doe", "johndoe@gmail.com", "1234", 40, 70, 65, 5);
    webTestClient
        .post()
        .uri("/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(newUser)
        .exchange()
        .expectStatus()
        .isCreated();
  }

  @DisplayName("Registering with an existing email is forbidden")
  @Test
  void registerForbiddenTest() {
    var newUser = new RegisterRequest("John Doe", "johndoe@gmail.com", "1234", 40, 70, 65, 5);
    System.out.println(userRepository.findByEmail("johndoe@gmail.com").get().getPassword());
    webTestClient
        .post()
        .uri("/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(newUser)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody(String.class)
        .isEqualTo("This email has already been registered, use another one");
  }

  @DisplayName("Creating a user without enough is not permitted")
  @Test
  void insufficientRegisterTest() {
    var newUser = new RegisterRequest(null, null, "1234", 40, 70, 65, 5);
    ;
    webTestClient
        .post()
        .uri("/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(newUser)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody(String.class)
        .isEqualTo("Insufficient data to register");
  }

  @DisplayName("Login once a user has been created works properly")
  @Test
  void loginTest() {
    var newUser = new RegisterRequest("John Doe2", "johndoe2@gmail.com", "1234", 40, 70, 65, 5);
    webTestClient
        .post()
        .uri("/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(newUser)
        .exchange()
        .expectStatus()
        .isCreated();
    var userToLog = new LoginRequest("johndoe2@gmail.com", "1234");
    webTestClient
        .post()
        .uri("/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(userToLog)
        .exchange()
        .expectStatus()
        .isOk();
  }

  @DisplayName("Login with bad credentials causes error")
  @Test
  void badLoginTest() {
    var userToLog = new LoginRequest("johndoe2@gmail.com", "12345");
    webTestClient
        .post()
        .uri("/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(userToLog)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody(String.class)
        .isEqualTo("Bad credentials");
  }
}
