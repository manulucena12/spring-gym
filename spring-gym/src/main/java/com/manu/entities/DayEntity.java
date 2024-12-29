package com.manu.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "days")
public class DayEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "routine", nullable = false, updatable = false)
  @JsonIgnore
  private RoutineEntity routine;

  @Column(name = "weekday", nullable = false)
  private String weekDay;

  @Column(name = "name", nullable = false)
  private String name;

  public List<ExerciseEntity> getExercises() {
    return exercises;
  }

  public void setExercises(List<ExerciseEntity> exercises) {
    this.exercises = exercises;
  }

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "day")
  private List<ExerciseEntity> exercises;

  public DayEntity(
      RoutineEntity routine, String weekDay, String name, List<ExerciseEntity> exercises) {
    this.routine = routine;
    this.weekDay = weekDay;
    this.name = name;
    this.exercises = exercises;
  }

  public DayEntity() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getWeekDay() {
    return weekDay;
  }

  public void setWeekDay(String weekDay) {
    this.weekDay = weekDay;
  }

  public RoutineEntity getRoutine() {
    return routine;
  }

  public void setRoutine(RoutineEntity routine) {
    this.routine = routine;
  }
}
