package com.futureh.drone.feeder.exception;

/**
 * InputNameException class.
 */
public class InputNameException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public InputNameException() {
    super("The param isn't video (Param must be video).");
  }

}
