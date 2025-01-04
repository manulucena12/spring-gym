package com.manu.controllers;

import com.manu.requests.auth.LoginRequest;
import com.manu.requests.auth.RegisterRequest;
import com.manu.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for user registration and login")
public class AuthController {

  @Autowired private AuthService authService;

  @Operation(
      summary = "Register a new user",
      description =
          "This endpoint allows a new user to register by providing the necessary details.",
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Payload containing user registration details.",
              required = true,
              content = @Content(schema = @Schema(implementation = RegisterRequest.class))),
      responses = {
        @ApiResponse(
            responseCode = "201",
            description = "User registered successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = String.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request due to invalid data",
            content =
                @Content(
                    mediaType = "application/json",
                    schema =
                        @Schema(
                            example = "This email has already been registered, use another one"))),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(example = "Internal server error")))
      })
  @PostMapping("/register")
  public ResponseEntity<Object> register(@RequestBody RegisterRequest request) {
    var response = authService.register(request);
    return ResponseEntity.status(response.getStatusCode())
        .body(response.getStatusCode() == 201 ? response.getContent() : response.getMessage());
  }

  @Operation(
      summary = "Authenticate a user",
      description = "This endpoint allows a user to log in by providing their email and password.",
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Payload containing user login details.",
              required = true,
              content = @Content(schema = @Schema(implementation = LoginRequest.class))),
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "User authenticated successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = String.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Bad credentials provided",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(example = "Bad credentials"))),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(example = "Internal server error")))
      })
  @PostMapping("/login")
  public ResponseEntity<Object> login(@RequestBody LoginRequest request) {
    var response = authService.login(request);
    return ResponseEntity.status(response.getStatusCode())
        .body(response.getStatusCode() == 200 ? response.getContent() : response.getMessage());
  }
}
