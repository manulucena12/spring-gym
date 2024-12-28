package com.manu.integration;

import com.manu.entities.RoutineEntity;
import com.manu.repositories.UserRepository;
import com.manu.requests.auth.LoginRequest;
import com.manu.requests.auth.RegisterRequest;
import com.manu.requests.days.UpdateDayRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DayTests {

  @Autowired private WebTestClient webTestClient;

  @Autowired private UserRepository userRepository;

  private String userToken;

  private Long userId;

  private Long dayId;

  @BeforeAll
  void register() {
    var newUser = new RegisterRequest("John Doe", "johndoe4@gmail.com", "1234", 40, 70, 65, 5);
    webTestClient
        .post()
        .uri("/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(newUser)
        .exchange()
        .expectStatus()
        .isCreated();
    var userToLog = new LoginRequest("johndoe4@gmail.com", "1234");
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
    userId = userRepository.findByEmail("johndoe4@gmail.com").get().getId();
    var response2 =
        webTestClient
            .get()
            .uri("/routine/" + userId)
            .header("Authorization", userToken)
            .exchange()
            .expectStatus()
            .isOk()
            .returnResult(RoutineEntity.class);
    dayId = response2.getResponseBody().blockFirst().getPlan().get(1).getId();
  }

  @DisplayName("Getting a day works properly")
  @Test
  void getDayTest() {
    webTestClient
        .get()
        .uri("/days/" + dayId)
        .header("Authorization", userToken)
        .exchange()
        .expectStatus()
        .isOk();
  }

  @DisplayName("Getting a day that is not owner's causes error")
  @Test
  void getNonOwnedDayTest() {
    webTestClient
        .get()
        .uri("/days/" + (40L))
        .header("Authorization", userToken)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody(String.class)
        .isEqualTo("Day not found");
  }

  @DisplayName("Updating a day works properly")
  @Test
  void updateDayTest() {
    var request = new UpdateDayRequest("Monday", "Back Workout");
    webTestClient
        .put()
        .uri("/days/" + dayId)
        .header("Authorization", userToken)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus()
        .isOk();
  }
}
