package com.manu.integration;

import com.manu.entities.RoutineEntity;
import com.manu.repositories.UserRepository;
import com.manu.requests.auth.LoginRequest;
import com.manu.requests.auth.RegisterRequest;
import com.manu.requests.routine.UpdateRoutineRequest;
import java.util.Optional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RoutineTests {

  @Autowired private WebTestClient webTestClient;

  @Autowired private UserRepository userRepository;

  private Long userId;

  private String userToken;

  private Long dayId;

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
  @Order(1)
  void getRoutineTest() {
    var response =
        webTestClient
            .get()
            .uri("/routine/" + userId)
            .header("Authorization", userToken)
            .exchange()
            .expectStatus()
            .isOk()
            .returnResult(RoutineEntity.class);
    var days = response.getResponseBody().blockFirst();
    dayId = days.getPlan().get(1).getId();
  }

  @DisplayName("When a user tries to access a routine that is not its returns forbidden")
  @Test
  @Order(2)
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

  @DisplayName("Adding more days to a routine works properly")
  @Test
  @Order(3)
  void addDaysToRoutineTest() {
    var addDaysRequest = new UpdateRoutineRequest(2, null);
    webTestClient
        .put()
        .uri("/routine/" + userId)
        .header("Authorization", userToken)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(addDaysRequest)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(String.class)
        .isEqualTo("Days added successfully to routine");
  }

  @DisplayName("Adding more days than seven causes error")
  @Test
  @Order(4)
  void addForbiddenDaysToRoutineTest() {
    var addDaysRequest = new UpdateRoutineRequest(6, null);
    webTestClient
        .put()
        .uri("/routine/" + userId)
        .header("Authorization", userToken)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(addDaysRequest)
        .exchange()
        .expectStatus()
        .isBadRequest();
  }

  @DisplayName("Deleting a day that does not exist causes error")
  @Test
  @Order(5)
  void deleteNonExistingDayTest() {
    var deleteDayRequest = new UpdateRoutineRequest(7, Optional.of(90L));
    webTestClient
        .put()
        .uri("/routine/" + userId)
        .header("Authorization", userToken)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(deleteDayRequest)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody(String.class)
        .isEqualTo("Day not found");
  }

  @DisplayName("Deleting a day works properly")
  @Test
  @Order(6)
  void deleteDayTest() {
    var deleteDayRequest = new UpdateRoutineRequest(7, Optional.of(dayId));
    webTestClient
        .put()
        .uri("/routine/" + userId)
        .header("Authorization", userToken)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(deleteDayRequest)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(String.class)
        .isEqualTo("Day eliminated successfully from your routine");
  }

  @DisplayName("Deleting a day that is not owner's causes error")
  @Test
  @Order(7)
  void deleteNonOwnedDayTest() {
    var deleteDayRequest = new UpdateRoutineRequest(7, Optional.of(6L));
    webTestClient
        .put()
        .uri("/routine/" + userId)
        .header("Authorization", userToken)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(deleteDayRequest)
        .exchange()
        .expectStatus()
        .isBadRequest();
  }
}
