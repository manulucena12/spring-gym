package com.manu.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "exercises")
public class ExerciseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "day_id", nullable = false, updatable = false)
  @JsonIgnore
  private DayEntity day;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "weight", nullable = false)
  private int weight;

  @Column(name = "repetitions", nullable = false)
  private int repetitions;

  public ExerciseEntity(DayEntity day, String name, int weight, int repetitions) {
    this.day = day;
    this.name = name;
    this.weight = weight;
    this.repetitions = repetitions;
  }

  public ExerciseEntity() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public DayEntity getDay() {
    return day;
  }

  public void setDay(DayEntity day) {
    this.day = day;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getWeight() {
    return weight;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }

  public int getRepetitions() {
    return repetitions;
  }

  public void setRepetitions(int repetitions) {
    this.repetitions = repetitions;
  }
}
