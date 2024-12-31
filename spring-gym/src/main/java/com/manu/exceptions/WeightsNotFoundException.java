package com.manu.exceptions;

public class WeightsNotFoundException extends RuntimeException {
  public WeightsNotFoundException(String message) {
    super(message);
  }
}
