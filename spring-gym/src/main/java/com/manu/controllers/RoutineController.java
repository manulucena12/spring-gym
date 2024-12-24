package com.manu.controllers;

import com.manu.services.RoutineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/routine")
public class RoutineController {

  @Autowired private RoutineService routineService;

  @GetMapping("/{id}")
  public ResponseEntity<Object> getPersonalRoutine(@PathVariable Long id) {
    var response = routineService.getUserRoutine(id);
    return ResponseEntity.status(response.getStatusCode())
        .body(response.getMessage() == null ? response.getContent() : response.getMessage());
  }
}
