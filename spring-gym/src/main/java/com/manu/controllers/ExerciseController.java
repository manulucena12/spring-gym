package com.manu.controllers;

import com.manu.entities.ExerciseEntity;
import com.manu.requests.exercises.NewExerciseRequest;
import com.manu.requests.exercises.UpdateExerciseRequest;
import com.manu.services.ExerciseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exercises")
@Tag(name = "Exercises", description = "Endpoints for exercises")
public class ExerciseController {

  @Autowired private ExerciseService exerciseService;

  @Operation(
      summary = "Get a single exercise",
      description = "Retrieves a single exercise by its ID. The user must own the exercise.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Exercise retrieved successfully",
            content = @Content(schema = @Schema(implementation = ExerciseEntity.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Exercise not found or unauthorized access"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  @GetMapping("/{id}")
  public ResponseEntity<Object> getSingleExercise(@PathVariable Long id) {
    var response = exerciseService.getSingleExercise(id);
    return ResponseEntity.status(response.getStatusCode())
        .body(response.getStatusCode() == 200 ? response.getContent() : response.getMessage());
  }

  @Operation(
      summary = "Update a single exercise",
      description = "Updates the details of a single exercise. The user must own the exercise.",
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The updated exercise details",
              required = true,
              content = @Content(schema = @Schema(implementation = UpdateExerciseRequest.class))),
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Exercise updated successfully",
            content = @Content(schema = @Schema(implementation = ExerciseEntity.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Exercise not found or unauthorized access"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  @PutMapping("/{id}")
  public ResponseEntity<Object> updateSingleExercise(
      @PathVariable Long id, @RequestBody UpdateExerciseRequest request) {
    var response = exerciseService.updateExercise(id, request);
    return ResponseEntity.status(response.getStatusCode())
        .body(response.getStatusCode() == 200 ? response.getContent() : response.getMessage());
  }

  @Operation(
      summary = "Delete a single exercise",
      description = "Deletes a single exercise by its ID. The user must own the exercise.",
      responses = {
        @ApiResponse(responseCode = "200", description = "Exercise deleted successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Exercise not found or unauthorized access"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteSingleExercise(@PathVariable Long id) {
    var response = exerciseService.deleteExercise(id);
    return ResponseEntity.status(response.getStatusCode()).body(response.getMessage());
  }

  @Operation(
      summary = "Create a new exercise",
      description = "Creates a new exercise for a specific day. The user must own the day.",
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The details of the new exercise",
              required = true,
              content = @Content(schema = @Schema(implementation = NewExerciseRequest.class))),
      responses = {
        @ApiResponse(
            responseCode = "201",
            description = "Exercise created successfully",
            content = @Content(schema = @Schema(implementation = ExerciseEntity.class))),
        @ApiResponse(responseCode = "400", description = "Day not found or unauthorized access"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  @PostMapping
  public ResponseEntity<Object> createExercise(@RequestBody NewExerciseRequest request) {
    var response = exerciseService.createExercise(request);
    return ResponseEntity.status(response.getStatusCode())
        .body(response.getStatusCode() == 201 ? response.getContent() : response.getMessage());
  }
}
