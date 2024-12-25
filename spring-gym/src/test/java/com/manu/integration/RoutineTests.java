package com.manu.integration;

import com.manu.repositories.UserRepository;
import com.manu.requests.auth.LoginRequest;
import com.manu.requests.auth.RegisterRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RoutineTests {

  @Autowired private WebTestClient webTestClient;

  @Autowired private UserRepository userRepository;

  private Long userId;

  private String userToken;

  @BeforeAll
  void register() {
    var newUser = new RegisterRequest("John Doe", "johndoe3@gmail.com", "1234", 40, 70, 65, 5);
    webTestClient
        .post()
        .uri("/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(newUser)
        .exchange()
        .expectStatus()
        .isCreated();
    var userToLog = new LoginRequest("johndoe3@gmail.com", "1234");
    var response =
        webTestClient
            .post()
            .uri("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(userToLog)
            .exchange()
            .expectStatus()
            .isOk()
            .returnResult(String.class);
    userToken = response.getResponseBody().blockFirst();
    userId = userRepository.findByEmail("johndoe3@gmail.com").get().getId();
  }

  @DisplayName("Getting a routine when registration is completed works properly")
  @Test
  void getRoutineTest() {
    webTestClient
        .get()
        .uri("/routine/" + userId)
        .header("Authorization", userToken)
        .exchange()
        .expectStatus()
        .isOk();
  }

  @DisplayName("When a user tries to access a routine that is not its returns forbidden")
  @Test
  void forbiddenRoutineTest() {
    webTestClient
        .get()
        .uri("/routine/" + 10)
        .header("Authorization", userToken)
        .exchange()
        .expectStatus()
        .isUnauthorized()
        .expectBody(String.class)
        .isEqualTo("You cannot access a routine that is not yours");
  }
}
