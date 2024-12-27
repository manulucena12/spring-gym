package com.manu.services;

import com.manu.exceptions.DayNotFoundException;
import com.manu.repositories.DayRepository;
import com.manu.requests.days.UpdateDayRequest;
import com.manu.responses.HttpServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DayService {

  @Autowired private DayRepository dayRepository;

  @Autowired private JwtService jwtService;

  public HttpServiceResponse<Object> getSingleDay(Long id) {
    try {
      var day =
          dayRepository.findById(id).orElseThrow(() -> new DayNotFoundException("Day not found"));
      var owner =
          dayRepository
              .findOwnerById(day.getId())
              .orElseThrow(() -> new DayNotFoundException("Day not found"));
      if (!jwtService.isOwner(owner)) {
        return new HttpServiceResponse<>(401, null, "You cannot access a day that is not yours");
      }
      return new HttpServiceResponse<>(200, day, null);
    } catch (Exception e) {
      if (e instanceof DayNotFoundException) {
        return new HttpServiceResponse<>(400, null, "Day not found");
      }
      return new HttpServiceResponse<>(500, null, "Internal server error");
    }
  }

  public HttpServiceResponse<Object> updateDay(Long id, UpdateDayRequest body) {
    var day =
        dayRepository.findById(id).orElseThrow(() -> new DayNotFoundException("Day not found"));
    var owner =
        dayRepository
            .findOwnerById(day.getId())
            .orElseThrow(() -> new DayNotFoundException("Day not found"));
    if (!jwtService.isOwner(owner)) {
      return new HttpServiceResponse<>(401, null, "You cannot access a day that is not yours");
    }
    day.setWeekDay(body.getWeekday());
    day.setName(body.getName());
    var updatedDay = dayRepository.save(day);
    return new HttpServiceResponse<>(200, updatedDay, null);
  }
}
