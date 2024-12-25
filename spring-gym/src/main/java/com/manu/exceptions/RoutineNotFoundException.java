package com.manu.exceptions;

public class RoutineNotFoundException extends RuntimeException {
  public RoutineNotFoundException(String message) {
    super(message);
  }
}
