package com.manu.integration;

import com.manu.repositories.UserRepository;
import com.manu.repositories.WeightRepository;
import com.manu.requests.auth.LoginRequest;
import com.manu.requests.auth.RegisterRequest;
import com.manu.requests.weights.NewWeightRequest;
import com.manu.requests.weights.UpdateWeightRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WeightTests {

  @Autowired private WebTestClient webTestClient;

  @Autowired private UserRepository userRepository;

  @Autowired private WeightRepository weightRepository;

  private String userToken;

  private Long userId;

  private Long weightId;

  private int secondWeightId;

  @BeforeAll
  void register() {
    var newUser = new RegisterRequest("John Doe", "johndoe6@gmail.com", "1234", 40, 70, 65, 5);
    webTestClient
        .post()
        .uri("/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(newUser)
        .exchange()
        .expectStatus()
        .isCreated();
    var userToLog = new LoginRequest("johndoe6@gmail.com", "1234");
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
    userId = userRepository.findByEmail("johndoe6@gmail.com").get().getId();
    weightId = weightRepository.findByOwner(userId).get().get(0).getId();
  }

  @DisplayName("When a user is registered its current weight is added to his weights list")
  @Test
  @Order(1)
  void firstWeightTrackedTest() {
    webTestClient
        .get()
        .uri("/weights/" + userId)
        .header("Authorization", userToken)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.weights.length()")
        .isEqualTo(1)
        .jsonPath("$.weights[0].id")
        .isEqualTo(weightId);
  }

  @DisplayName("Getting a single weight works properly")
  @Test
  @Order(2)
  void getSingleWeightTest() {
    webTestClient
        .get()
        .uri("/weights/single/" + weightId)
        .header("Authorization", userToken)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.weight")
        .isEqualTo(70);
  }

  @DisplayName("Deleting the only weight a user has is not possible")
  @Test
  @Order(3)
  void deleteUniqueWeightTest() {
    webTestClient
        .delete()
        .uri("/weights/single/" + weightId)
        .header("Authorization", userToken)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody(String.class)
        .isEqualTo("You cannot delete your first weight, delete the others or update this");
  }

  @DisplayName("Updating a weight works properly")
  @Test
  @Order(4)
  void updateWeightTest() {
    var request = new UpdateWeightRequest(71.5);
    webTestClient
        .put()
        .uri("/weights/single/" + weightId)
        .header("Authorization", userToken)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus()
        .isOk();
  }

  @DisplayName("Creating a weight works properly")
  @Test
  @Order(5)
  void createWeightTest() {
    var request = new NewWeightRequest(72, userId);
    webTestClient
        .post()
        .uri("/weights")
        .header("Authorization", userToken)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody()
        .jsonPath("$.id")
        .value(
            value -> {
              secondWeightId = (int) value;
            });
  }

  @DisplayName("Deleting a weight when there is more than one registered works properly")
  @Test
  @Order(6)
  void deleteWeightTest() {
    webTestClient
        .delete()
        .uri("/weights/single/" + secondWeightId)
        .header("Authorization", userToken)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(String.class)
        .isEqualTo("Your weight has been deleted successfully");
  }

  @DisplayName("Getting/Putting/Deleting a non-existing weight causes error")
  @Test
  @Order(7)
  void forbiddenEndpointsTest() {
    webTestClient
        .get()
        .uri("/weights/single/" + 90L)
        .header("Authorization", userToken)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody(String.class)
        .isEqualTo("Weight not found");
    var updateWeight = new UpdateWeightRequest(80);
    webTestClient
        .put()
        .uri("/weights/single/" + 90L)
        .header("Authorization", userToken)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(updateWeight)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody(String.class)
        .isEqualTo("Weight not found");
    webTestClient
        .delete()
        .uri("/weights/single/" + 90L)
        .header("Authorization", userToken)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody(String.class)
        .isEqualTo("Weight not found");
  }

  @DisplayName("Getting/Putting/Deleting without authorization is forbidden")
  @Test
  @Order(8)
  void unauthenticatedTest() {
    webTestClient.get().uri("/weights/single/" + 90L).exchange().expectStatus().isForbidden();
    var updateWeight = new UpdateWeightRequest(80);
    webTestClient
        .put()
        .uri("/weights/single/" + 90L)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(updateWeight)
        .exchange()
        .expectStatus()
        .isForbidden();
    webTestClient.delete().uri("/weights/single/" + 90L).exchange().expectStatus().isForbidden();
  }
}
