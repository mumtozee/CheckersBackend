package com.ruchekers.exceptions;

public class InvalidMoveException extends Exception {
  public InvalidMoveException(String message) {
    super(message);
  }

  public InvalidMoveException(String message, Throwable cause) {
    super(message, cause);
  }
}
