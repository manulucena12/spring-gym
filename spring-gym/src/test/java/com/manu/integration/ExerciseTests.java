package com.manu.integration;

import com.manu.entities.DayEntity;
import com.manu.entities.ExerciseEntity;
import com.manu.entities.RoutineEntity;
import com.manu.repositories.UserRepository;
import com.manu.requests.auth.LoginRequest;
import com.manu.requests.auth.RegisterRequest;
import com.manu.requests.exercises.NewExerciseRequest;
import com.manu.requests.exercises.UpdateExerciseRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExerciseTests {

  @Autowired private WebTestClient webTestClient;

  @Autowired private UserRepository userRepository;

  private String userToken;

  private Long userId;

  private DayEntity day;

  private Long exerciseId;

  @BeforeAll
  void register() {
    var newUser = new RegisterRequest("John Doe", "johndoe5@gmail.com", "1234", 40, 70, 65, 5);
    webTestClient
        .post()
        .uri("/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(newUser)
        .exchange()
        .expectStatus()
        .isCreated();
    var userToLog = new LoginRequest("johndoe5@gmail.com", "1234");
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
    userId = userRepository.findByEmail("johndoe5@gmail.com").get().getId();
    var response2 =
        webTestClient
            .get()
            .uri("/routine/" + userId)
            .header("Authorization", userToken)
            .exchange()
            .expectStatus()
            .isOk()
            .returnResult(RoutineEntity.class);
    day = response2.getResponseBody().blockFirst().getPlan().get(1);
  }

  @DisplayName("Creating a exercise works properly")
  @Test
  @Order(1)
  void createExerciseTest() {
    var newExercise = new NewExerciseRequest("Seated Row", day.getId(), 50, 8);
    var response =
        webTestClient
            .post()
            .uri("/exercises")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(newExercise)
            .header("Authorization", userToken)
            .exchange()
            .expectStatus()
            .isCreated()
            .returnResult(ExerciseEntity.class);
    exerciseId = response.getResponseBody().blockFirst().getId();
  }

  @DisplayName("Creating a exercise works properly")
  @Test
  @Order(2)
  void createBadExerciseTest() {
    var newExercise = new NewExerciseRequest("Seated Row", 90L, 50, 8);
    webTestClient
        .post()
        .uri("/exercises")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(newExercise)
        .header("Authorization", userToken)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody(String.class)
        .isEqualTo("Day not found");
  }

  @DisplayName("Getting an exercise when created works properly")
  @Test
  @Order(3)
  void getExerciseTest() {
    webTestClient
        .get()
        .uri("/exercises/" + exerciseId)
        .header("Authorization", userToken)
        .exchange()
        .expectStatus()
        .isOk();
  }

  @DisplayName("Updating an exercise works properly")
  @Test
  @Order(4)
  void updateExerciseTest() {
    var updateExercise = new UpdateExerciseRequest("Unilateral Row", 55, 10);
    webTestClient
        .put()
        .uri("/exercises/" + exerciseId)
        .header("Authorization", userToken)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(updateExercise)
        .exchange()
        .expectStatus()
        .isOk();
  }

  @DisplayName("Deleting an exericise works properly")
  @Test
  @Order(5)
  void deleteExerciseTest() {
    webTestClient
        .delete()
        .uri("/exercises/" + exerciseId)
        .header("Authorization", userToken)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(String.class)
        .isEqualTo("Exercise deleted successfully");
  }

  @DisplayName("Getting/Putting/Deleting a non-existing exercise causes error")
  @Test
  @Order(6)
  void failedTest() {
    webTestClient
        .get()
        .uri("/exercises/" + 90L)
        .header("Authorization", userToken)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody(String.class)
        .isEqualTo("Exercise not found");
    var updateExercise = new UpdateExerciseRequest("Unilateral Row", 55, 10);
    webTestClient
        .put()
        .uri("/exercises/" + 90L)
        .header("Authorization", userToken)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(updateExercise)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody(String.class)
        .isEqualTo("Exercise not found");
    webTestClient
        .delete()
        .uri("/exercises/" + 90L)
        .header("Authorization", userToken)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody(String.class)
        .isEqualTo("Exercise not found");
  }
}
