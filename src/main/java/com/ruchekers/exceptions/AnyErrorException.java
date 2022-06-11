package com.ruchekers.exceptions;

public class AnyErrorException extends Exception {
  public AnyErrorException(String message) {
    super(message);
  }

  public AnyErrorException(String message, Throwable cause) {
    super(message, cause);
  }
}
