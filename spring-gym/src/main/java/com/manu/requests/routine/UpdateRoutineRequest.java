package com.manu.requests.routine;

import java.util.Optional;

public class UpdateRoutineRequest {
  private int days;
  private Optional<Long> id;

  public int getDays() {
    return days;
  }

  public void setDays(int days) {
    this.days = days;
  }

  public Optional<Long> getId() {
    return id;
  }

  public void setId(Optional<Long> id) {
    this.id = id;
  }

  public UpdateRoutineRequest(int days, Optional<Long> id) {
    this.days = days;
    this.id = id;
  }
}
