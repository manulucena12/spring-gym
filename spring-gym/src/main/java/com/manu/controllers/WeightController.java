package com.manu.controllers;

import com.manu.requests.weights.NewWeightRequest;
import com.manu.requests.weights.UpdateWeightRequest;
import com.manu.services.WeightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weights")
public class WeightController {

  @Autowired private WeightService weightService;

  @GetMapping("/{id}")
  public ResponseEntity<Object> getUserWeights(@PathVariable Long id) {
    var response = weightService.getAllWeights(id);
    return ResponseEntity.status(response.getStatusCode())
        .body(response.getStatusCode() == 200 ? response.getContent() : response.getMessage());
  }

  @GetMapping("/single/{id}")
  public ResponseEntity<Object> getSingleWeight(@PathVariable Long id) {
    var response = weightService.getSingleWeight(id);
    return ResponseEntity.status(response.getStatusCode())
        .body(response.getStatusCode() == 200 ? response.getContent() : response.getMessage());
  }

  @PutMapping("/single/{id}")
  public ResponseEntity<Object> updateWeight(
      @PathVariable Long id, @RequestBody UpdateWeightRequest request) {
    var response = weightService.updateWeight(id, request);
    return ResponseEntity.status(response.getStatusCode())
        .body(response.getStatusCode() == 200 ? response.getContent() : response.getMessage());
  }

  @DeleteMapping("/single/{id}")
  public ResponseEntity<Object> deleteWeight(@PathVariable Long id) {
    var response = weightService.deleteWeight(id);
    return ResponseEntity.status(response.getStatusCode()).body(response.getMessage());
  }

  @PostMapping
  public ResponseEntity<Object> createWeight(@RequestBody NewWeightRequest request) {
    var response = weightService.createWeight(request);
    return ResponseEntity.status(response.getStatusCode())
        .body(response.getStatusCode() == 201 ? response.getContent() : response.getMessage());
  }
}
