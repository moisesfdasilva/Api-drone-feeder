package com.futureh.drone.feeder.exception;

/**
 * InputObjetctException class.
 */
public class WrongInputDataException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public WrongInputDataException(String message) {
    super(message);
  }

}
