package com.ruchekers.exceptions;

public class BusyCellException extends Exception {
  public BusyCellException(String message) {
    super(message);
  }

  BusyCellException(String message, Throwable cause) {
    super(message, cause);
  }
}
