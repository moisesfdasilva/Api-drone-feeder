package com.futureh.drone.feeder.exception;

/**
 * InputVideoNameException class.
 */
public class VideoNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public VideoNotFoundException() {
    super("Video not found."
        + " Try to use the standard video name (DRON-yyyy-MM-dd-HHmmss.mp4).");
  }

}
