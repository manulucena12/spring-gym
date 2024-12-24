package com.manu.services;

import com.manu.repositories.RoutineRepository;
import com.manu.responses.HttpServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoutineService {

  @Autowired private JwtService jwtService;

  @Autowired private RoutineRepository routineRepository;

  public HttpServiceResponse<Object> getUserRoutine(Long id) {
    if (!jwtService.isOwner(id)) {
      return new HttpServiceResponse<>(401, null, "You cannot access a routine that is not yours");
    }
    var routine = routineRepository.findByOwner(id);
    if (routine.isEmpty()) {
      return new HttpServiceResponse<>(400, null, "Routine not found");
    }
    return new HttpServiceResponse<>(200, routine, null);
  }
}
