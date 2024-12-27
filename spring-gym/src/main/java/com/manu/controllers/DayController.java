package com.manu.controllers;

import com.manu.requests.days.UpdateDayRequest;
import com.manu.services.DayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/days")
public class DayController {

  @Autowired private DayService dayService;

  @GetMapping("/{id}")
  public ResponseEntity<Object> getSingleDay(@PathVariable Long id) {
    var response = dayService.getSingleDay(id);
    return ResponseEntity.status(response.getStatusCode())
        .body(response.getStatusCode() == 200 ? response.getContent() : response.getMessage());
  }

  @PutMapping("/{id}")
  public ResponseEntity<Object> updateSingleDay(
      @PathVariable Long id, @RequestBody UpdateDayRequest request) {
    var response = dayService.updateDay(id, request);
    return ResponseEntity.status(response.getStatusCode())
        .body(response.getStatusCode() == 200 ? response.getContent() : response.getMessage());
  }
}
