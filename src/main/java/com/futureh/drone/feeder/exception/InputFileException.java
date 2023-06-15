package com.futureh.drone.feeder.exception;

/**
 * InputFileException class.
 */
public class InputFileException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public InputFileException() {
    super("The param don't have a video (Param must have a video).");
  }

}
