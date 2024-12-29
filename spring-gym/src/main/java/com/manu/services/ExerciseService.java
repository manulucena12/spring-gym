package com.manu.services;

import com.manu.entities.ExerciseEntity;
import com.manu.exceptions.DayNotFoundException;
import com.manu.exceptions.ExerciseNotFoundException;
import com.manu.repositories.DayRepository;
import com.manu.repositories.ExerciseRepository;
import com.manu.requests.exercises.NewExerciseRequest;
import com.manu.requests.exercises.UpdateExerciseRequest;
import com.manu.responses.HttpServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExerciseService {

  @Autowired private ExerciseRepository exerciseRepository;

  @Autowired private JwtService jwtService;

  @Autowired private DayRepository dayRepository;

  public HttpServiceResponse<Object> getSingleExercise(Long id) {
    try {
      var exercise =
          exerciseRepository
              .findById(id)
              .orElseThrow(() -> new ExerciseNotFoundException("Exercise not found"));
      var owner =
          exerciseRepository
              .findOwnerById(id)
              .orElseThrow(() -> new ExerciseNotFoundException("Exercise not found"));
      if (!jwtService.isOwner(owner)) {
        return new HttpServiceResponse<>(
            400, null, "You cannot access an exercise that is not yours");
      }
      return new HttpServiceResponse<>(200, exercise, null);
    } catch (Exception e) {
      if (e instanceof ExerciseNotFoundException) {
        return new HttpServiceResponse<>(400, null, "Exercise not found");
      }
      System.out.println(e);
      return new HttpServiceResponse<>(500, null, "Internal server error");
    }
  }

  public HttpServiceResponse<Object> updateExercise(Long id, UpdateExerciseRequest body) {
    try {
      var exercise =
          exerciseRepository
              .findById(id)
              .orElseThrow(() -> new ExerciseNotFoundException("Exercise not found"));
      var owner =
          exerciseRepository
              .findOwnerById(id)
              .orElseThrow(() -> new ExerciseNotFoundException("Exercise not found"));
      if (!jwtService.isOwner(owner)) {
        return new HttpServiceResponse<>(
            400, null, "You cannot access an exercise that is not yours");
      }
      exercise.setName(body.getName());
      exercise.setWeight(body.getWeight());
      exercise.setRepetitions(body.getRepetitions());
      exerciseRepository.save(exercise);
      return new HttpServiceResponse<>(200, exercise, null);
    } catch (Exception e) {
      if (e instanceof ExerciseNotFoundException) {
        return new HttpServiceResponse<>(400, null, "Exercise not found");
      }
      System.out.println(e);
      return new HttpServiceResponse<>(500, null, "Internal server error");
    }
  }

  public HttpServiceResponse<Object> deleteExercise(Long id) {
    try {
      var exercise =
          exerciseRepository
              .findById(id)
              .orElseThrow(() -> new ExerciseNotFoundException("Exercise not found"));
      var owner =
          exerciseRepository
              .findOwnerById(id)
              .orElseThrow(() -> new ExerciseNotFoundException("Exercise not found"));
      if (!jwtService.isOwner(owner)) {
        return new HttpServiceResponse<>(
            400, null, "You cannot access an exercise that is not yours");
      }
      exerciseRepository.delete(exercise);
      return new HttpServiceResponse<>(200, null, "Exercise deleted successfully");
    } catch (Exception e) {
      if (e instanceof ExerciseNotFoundException) {
        return new HttpServiceResponse<>(400, null, "Exercise not found");
      }
      System.out.println(e);
      return new HttpServiceResponse<>(500, null, "Internal server error");
    }
  }

  public HttpServiceResponse<Object> createExercise(NewExerciseRequest body) {
    try {
      var day =
          dayRepository
              .findById(body.getDay())
              .orElseThrow(() -> new DayNotFoundException("Day not found"));
      var owner =
          dayRepository
              .findOwnerById(day.getId())
              .orElseThrow(() -> new DayNotFoundException("Day not found"));
      if (!jwtService.isOwner(owner)) {
        return new HttpServiceResponse<>(400, null, "You cannot access a day that is not yours");
      }
      var newExercise =
          new ExerciseEntity(day, body.getName(), body.getWeight(), body.getRepetitions());
      var createdExercise = exerciseRepository.save(newExercise);
      return new HttpServiceResponse<>(201, createdExercise, null);
    } catch (Exception e) {
      if (e instanceof DayNotFoundException) {
        return new HttpServiceResponse<>(400, null, "Day not found");
      }
      System.out.println(e);
      return new HttpServiceResponse<>(500, null, "Internal server error");
    }
  }
}
