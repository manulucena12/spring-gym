package com.manu.requests.days;

public class UpdateDayRequest {

  private String weekday;
  private String name;

  public UpdateDayRequest(String weekday, String name) {
    this.weekday = weekday;
    this.name = name;
  }

  public String getWeekday() {
    return weekday;
  }

  public void setWeekday(String weekday) {
    this.weekday = weekday;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
