package com.manu.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

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

  public DayEntity(RoutineEntity routine, String weekDay, String name) {
    this.routine = routine;
    this.weekDay = weekDay;
    this.name = name;
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
