package com.futureh.drone.feeder.exception;

/**
 * InputVideoNameException class.
 */
public class IntServerErrorInVideoFinding extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public IntServerErrorInVideoFinding() {
    super("Internal server error in video finding.");
  }

}
