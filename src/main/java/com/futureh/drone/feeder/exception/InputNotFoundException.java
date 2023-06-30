package com.futureh.drone.feeder.exception;

/**
 * InputNotFound class.
 */
public class InputNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public InputNotFoundException(String message) {
    super(message);
  }

}
