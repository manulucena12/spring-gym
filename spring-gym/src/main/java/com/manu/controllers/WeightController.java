package com.manu.controllers;

import com.manu.requests.weights.NewWeightRequest;
import com.manu.requests.weights.UpdateWeightRequest;
import com.manu.services.WeightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weights")
@Tag(name = "Weights")
public class WeightController {

  @Autowired private WeightService weightService;

  @Operation(
      summary = "Retrieve all weights for a user",
      description = "This endpoint allows a user to retrieve all weights they own.",
      parameters = {
        @Parameter(
            name = "id",
            description = "The user ID whose weights are to be retrieved.",
            required = true,
            in = ParameterIn.PATH)
      },
      responses = {
        @ApiResponse(responseCode = "200", description = "All gone ok"),
        @ApiResponse(
            responseCode = "401",
            description = "When a user that is not the owner of the weights causes this error"),
        @ApiResponse(
            responseCode = "400",
            description = "When there is no weight register causes this")
      })
  @GetMapping("/{id}")
  public ResponseEntity<Object> getUserWeights(@PathVariable Long id) {
    var response = weightService.getAllWeights(id);
    return ResponseEntity.status(response.getStatusCode())
        .body(response.getStatusCode() == 200 ? response.getContent() : response.getMessage());
  }

  @Operation(
      summary = "Retrieve a single weight",
      description = "This endpoint retrieves a specific weight by its ID.",
      parameters = {
        @Parameter(
            name = "id",
            description = "The ID of the weight to retrieve",
            required = true,
            in = ParameterIn.PATH)
      },
      responses = {
        @ApiResponse(responseCode = "200", description = "All gone ok"),
        @ApiResponse(
            responseCode = "401",
            description = "When a user that is not the owner of the weight causes this error"),
        @ApiResponse(responseCode = "400", description = "When the weight is not found")
      })
  @GetMapping("/single/{id}")
  public ResponseEntity<Object> getSingleWeight(@PathVariable Long id) {
    var response = weightService.getSingleWeight(id);
    return ResponseEntity.status(response.getStatusCode())
        .body(response.getStatusCode() == 200 ? response.getContent() : response.getMessage());
  }

  @Operation(
      summary = "Update an existing weight",
      description = "This endpoint allows a user to update an existing weight.",
      parameters = {
        @Parameter(
            name = "id",
            description = "The ID of the weight to update.",
            required = true,
            in = ParameterIn.PATH)
      },
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Payload containing the updated weight information.",
              required = true,
              content = @Content(schema = @Schema(implementation = UpdateWeightRequest.class))),
      responses = {
        @ApiResponse(responseCode = "200", description = "Weight updated successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "When the weight is not found or the user is not the owner")
      })
  @PutMapping("/single/{id}")
  public ResponseEntity<Object> updateWeight(
      @PathVariable Long id, @RequestBody UpdateWeightRequest request) {
    System.out.println(request.getWeight());
    var response = weightService.updateWeight(id, request);
    return ResponseEntity.status(response.getStatusCode())
        .body(response.getStatusCode() == 200 ? response.getContent() : response.getMessage());
  }

  @Operation(
      summary = "Delete an existing weight",
      description = "This endpoint allows a user to delete an existing weight.",
      parameters = {
        @Parameter(
            name = "id",
            description = "The ID of the weight to delete.",
            required = true,
            in = ParameterIn.PATH)
      },
      responses = {
        @ApiResponse(responseCode = "200", description = "Weight deleted successfully"),
        @ApiResponse(
            responseCode = "400",
            description =
                "When the weight is not found, the user is not the owner, or it is the first weight")
      })
  @DeleteMapping("/single/{id}")
  public ResponseEntity<Object> deleteWeight(@PathVariable Long id) {
    var response = weightService.deleteWeight(id);
    return ResponseEntity.status(response.getStatusCode()).body(response.getMessage());
  }

  @Operation(
      summary = "Create a new weight",
      description = "This endpoint allows a user to create a new weight record.",
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Payload containing the weight information to create.",
              required = true,
              content = @Content(schema = @Schema(implementation = NewWeightRequest.class))),
      responses = {
        @ApiResponse(responseCode = "201", description = "Weight created successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "When the user attempts to create a weight with an invalid owner ID")
      })
  @PostMapping
  public ResponseEntity<Object> createWeight(@RequestBody NewWeightRequest request) {
    var response = weightService.createWeight(request);
    return ResponseEntity.status(response.getStatusCode())
        .body(response.getStatusCode() == 201 ? response.getContent() : response.getMessage());
  }
}
