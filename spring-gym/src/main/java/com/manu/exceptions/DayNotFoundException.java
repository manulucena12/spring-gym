package com.manu.exceptions;

public class DayNotFoundException extends RuntimeException {
  public DayNotFoundException(String message) {
    super(message);
  }
}
