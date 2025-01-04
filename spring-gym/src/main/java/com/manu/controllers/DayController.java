package com.manu.controllers;

import com.manu.entities.DayEntity;
import com.manu.requests.days.UpdateDayRequest;
import com.manu.services.DayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/days")
@Tag(name = "Days", description = "Endpoints for days")
public class DayController {

  @Autowired private DayService dayService;

  @Operation(
      summary = "Get a single day",
      description = "Retrieve a specific day by its ID. The user must own the day.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Day retrieved successfully",
            content = @Content(schema = @Schema(implementation = DayEntity.class))),
        @ApiResponse(responseCode = "400", description = "Day not found or unauthorized access"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access to the day"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  @GetMapping("/{id}")
  public ResponseEntity<Object> getSingleDay(@PathVariable Long id) {
    var response = dayService.getSingleDay(id);
    return ResponseEntity.status(response.getStatusCode())
        .body(response.getStatusCode() == 200 ? response.getContent() : response.getMessage());
  }

  @Operation(
      summary = "Update a single day",
      description = "Update the details of a specific day by its ID. The user must own the day.",
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Details for updating the day",
              required = true,
              content = @Content(schema = @Schema(implementation = UpdateDayRequest.class))),
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Day updated successfully",
            content = @Content(schema = @Schema(implementation = DayEntity.class))),
        @ApiResponse(responseCode = "400", description = "Day not found or invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access to the day"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  @PutMapping("/{id}")
  public ResponseEntity<Object> updateSingleDay(
      @PathVariable Long id, @RequestBody UpdateDayRequest request) {
    var response = dayService.updateDay(id, request);
    return ResponseEntity.status(response.getStatusCode())
        .body(response.getStatusCode() == 200 ? response.getContent() : response.getMessage());
  }
}
