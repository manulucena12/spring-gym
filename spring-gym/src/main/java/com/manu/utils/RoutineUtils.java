package com.manu.utils;

import com.manu.entities.DayEntity;
import com.manu.entities.RoutineEntity;
import com.manu.exceptions.DayNotFoundException;
import com.manu.repositories.DayRepository;
import com.manu.repositories.RoutineRepository;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoutineUtils {

  @Autowired private RoutineRepository routineRepository;

  @Autowired private DayRepository dayRepository;

  public boolean addDaysToRoutine(RoutineEntity routine, int days) {
    if (routine.getDays() + days > 7) {
      return false;
    }
    for (int i = 0; i < days; i++) {
      dayRepository.save(new DayEntity(routine, "Undefined", "Unnamed Workout", List.of()));
    }
    int newDays = routine.getDays() + days;
    routineRepository.updateById(newDays, new Date().toString(), routine.getId());
    return true;
  }

  public boolean quitDayFromRoutine(RoutineEntity routine, Long dayId, Long userId) {
    if (routine.getDays() == 1) {
      return false;
    }
    if (!dayRepository.existsById(dayId)) {
      return false;
    }
    var owner =
        dayRepository.findOwnerById(dayId).orElseThrow(() -> new DayNotFoundException("Not found"));
    if (owner != userId) {
      return false;
    }
    dayRepository.deleteById(dayId);
    routineRepository.updateById(routine.getDays() - 1, new Date().toString(), routine.getId());
    return true;
  }
}
