package com.ruchekers.exceptions;

public class WhiteCellException extends Exception {
  public WhiteCellException(String message) {
    super(message);
  }

  public WhiteCellException(String message, Throwable cause) {
    super(message, cause);
  }
}
