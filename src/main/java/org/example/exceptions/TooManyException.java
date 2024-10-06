package org.example.exceptions;

public class TooManyException extends Exception {
  public TooManyException(String message) {
    super(message);
  }
}