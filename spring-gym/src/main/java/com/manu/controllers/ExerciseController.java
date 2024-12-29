package com.manu.controllers;

import com.manu.requests.exercises.NewExerciseRequest;
import com.manu.requests.exercises.UpdateExerciseRequest;
import com.manu.services.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exercises")
public class ExerciseController {

  @Autowired private ExerciseService exerciseService;

  @GetMapping("/{id}")
  public ResponseEntity<Object> getSingleExercise(@PathVariable Long id) {
    var response = exerciseService.getSingleExercise(id);
    return ResponseEntity.status(response.getStatusCode())
        .body(response.getStatusCode() == 200 ? response.getContent() : response.getMessage());
  }

  @PutMapping("/{id}")
  public ResponseEntity<Object> updateSingleExercise(
      @PathVariable Long id, @RequestBody UpdateExerciseRequest request) {
    var response = exerciseService.updateExercise(id, request);
    return ResponseEntity.status(response.getStatusCode())
        .body(response.getStatusCode() == 200 ? response.getContent() : response.getMessage());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteSingleExercise(@PathVariable Long id) {
    var response = exerciseService.deleteExercise(id);
    return ResponseEntity.status(response.getStatusCode()).body(response.getMessage());
  }

  @PostMapping
  public ResponseEntity<Object> createExercise(@RequestBody NewExerciseRequest request) {
    var response = exerciseService.createExercise(request);
    return ResponseEntity.status(response.getStatusCode())
        .body(response.getStatusCode() == 201 ? response.getContent() : response.getMessage());
  }
}
