package com.manu.controllers;

import com.manu.requests.auth.LoginRequest;
import com.manu.requests.auth.RegisterRequest;
import com.manu.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired private AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<Object> register(@RequestBody RegisterRequest request) {
    var response = authService.register(request);
    return ResponseEntity.status(response.getStatusCode())
        .body(response.getStatusCode() == 201 ? response.getContent() : response.getMessage());
  }

  @PostMapping("/login")
  public ResponseEntity<Object> login(@RequestBody LoginRequest request) {
    var response = authService.login(request);
    return ResponseEntity.status(response.getStatusCode())
        .body(response.getStatusCode() == 200 ? response.getContent() : response.getMessage());
  }
}
