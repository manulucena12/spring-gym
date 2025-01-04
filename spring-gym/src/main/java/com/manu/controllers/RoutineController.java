package com.manu.controllers;

import com.manu.entities.RoutineEntity;
import com.manu.requests.routine.UpdateRoutineRequest;
import com.manu.services.RoutineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/routine")
@Tag(name = "Routines", description = "Endpoints for managing user routines")
public class RoutineController {

  @Autowired private RoutineService routineService;

  @Operation(
      summary = "Retrieve personal routine",
      description =
          "This endpoint retrieves a user's personal routine, including the associated plan and days.",
      parameters = {
        @Parameter(
            name = "id",
            description = "ID of the user whose routine is being requested",
            required = true,
            schema = @Schema(type = "long"))
      },
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Routine retrieved successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RoutineEntity.class))),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized access to another user's routine",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(example = "You cannot access a routine that is not yours"))),
        @ApiResponse(
            responseCode = "400",
            description = "Routine or days not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(example = "Routine or days not found"))),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(example = "Internal server error")))
      })
  @GetMapping("/{id}")
  public ResponseEntity<Object> getPersonalRoutine(@PathVariable Long id) {
    var response = routineService.getUserRoutine(id);
    return ResponseEntity.status(response.getStatusCode())
        .body(response.getMessage() == null ? response.getContent() : response.getMessage());
  }

  @Operation(
      summary = "Update personal routine",
      description =
          "This endpoint updates a user's personal routine, allowing modification of days or removal of specific days.",
      parameters = {
        @Parameter(
            name = "id",
            description = "ID of the user whose routine is being updated",
            required = true,
            schema = @Schema(type = "long"))
      },
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Payload containing routine update details.",
              required = true,
              content = @Content(schema = @Schema(implementation = UpdateRoutineRequest.class))),
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Routine updated successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema =
                        @Schema(
                            example =
                                "Days added successfully to routine or Day eliminated successfully from your routine"))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid update request",
            content =
                @Content(
                    mediaType = "application/json",
                    schema =
                        @Schema(
                            example =
                                "You cannot add more than 7 days to a routine or Routine not found"))),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized access to another user's routine",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(example = "You cannot access a routine that is not yours"))),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(example = "Internal server error")))
      })
  @PutMapping("/{id}")
  public ResponseEntity<Object> updatePersonalRoutine(
      @PathVariable Long id, @RequestBody UpdateRoutineRequest request) {
    var response = routineService.updateUserRoutine(id, request);
    return ResponseEntity.status(response.getStatusCode()).body(response.getMessage());
  }
}
