package com.manu.services;

import com.manu.exceptions.DayNotFoundException;
import com.manu.exceptions.RoutineNotFoundException;
import com.manu.repositories.DayRepository;
import com.manu.repositories.RoutineRepository;
import com.manu.requests.routine.UpdateRoutineRequest;
import com.manu.responses.HttpServiceResponse;
import com.manu.utils.RoutineUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoutineService {

  @Autowired private JwtService jwtService;

  @Autowired private RoutineRepository routineRepository;

  @Autowired private DayRepository dayRepository;

  @Autowired private RoutineUtils routineUtils;

  public HttpServiceResponse<Object> getUserRoutine(Long id) {
    try {
      if (!jwtService.isOwner(id)) {
        return new HttpServiceResponse<>(
            401, null, "You cannot access a routine that is not yours");
      }
      var routine =
          routineRepository
              .findByOwner(id)
              .orElseThrow(() -> new RoutineNotFoundException("Routine not found"));
      var days =
          dayRepository
              .findByRoutine(routine.getId())
              .orElseThrow(() -> new DayNotFoundException("Days not found"));
      routine.setPlan(days);
      return new HttpServiceResponse<>(200, routine, null);
    } catch (Exception e) {
      if (e instanceof RoutineNotFoundException || e instanceof DayNotFoundException) {
        return new HttpServiceResponse<>(400, null, "Routine or days not found");
      }
      System.out.println(e);
      return new HttpServiceResponse<>(500, null, "Internal server error");
    }
  }

  public HttpServiceResponse<Object> updateUserRoutine(Long id, UpdateRoutineRequest body) {
    try {
      if (!jwtService.isOwner(id)) {
        return new HttpServiceResponse<>(
            401, null, "You cannot access a routine that is not yours");
      }
      var routine =
          routineRepository
              .findByOwner(id)
              .orElseThrow(() -> new RoutineNotFoundException("Routine not found"));
      if (body.getDays() > routine.getDays()) {
        return new HttpServiceResponse<>(400, null, "You cannot add more than 7 days to a routine");
      }
      if (body.getDays() < routine.getDays()) {
        if (!routineUtils.addDaysToRoutine(routine, body.getDays())) {
          return new HttpServiceResponse<>(
              400, null, "You cannot add more than 7 days to a routine");
        }
        return new HttpServiceResponse<>(200, null, "Days added successfully to routine");
      }
      if (!dayRepository.existsById(body.getId().get())) {
        return new HttpServiceResponse<>(400, null, "Day not found");
      }
      if (!routineUtils.quitDayFromRoutine(routine, body.getId().get())) {
        return new HttpServiceResponse<>(
            400, null, "You must have at least one day in your routine");
      }
      return new HttpServiceResponse<>(200, null, "Day eliminated successfully from your routine");
    } catch (Exception e) {
      if (e instanceof RoutineNotFoundException) {
        return new HttpServiceResponse<>(400, null, "Routine not found");
      }
      System.out.println(e);
      return new HttpServiceResponse<>(500, null, "Internal server error");
    }
  }
}
