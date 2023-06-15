package com.futureh.drone.feeder.middleware;

import com.futureh.drone.feeder.exception.InputFileException;
import com.futureh.drone.feeder.exception.InputVideoNameException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * VideoNameMiddleware class.
 */
public class VideoNameMiddleware {

  /**
   * isValidName class.
   */
  public static void isValidName(String fileName) {
    if (fileName.isEmpty()) {
      throw new InputFileException();
    }

    if (fileName.length() != 26) {
      throw new InputVideoNameException("The video name length isn't 26 characters."
          + " Must be used the standard name (DRON-yyyy-MM-dd-HHmmss.mp4).");
    }

    String droneName = fileName.substring(0, 5);
    if (!droneName.matches("^(?:\\w{4})-$")) {
      throw new InputVideoNameException("The default drone name isn't correct."
          + " Must be used the standard name (DRON-yyyy-MM-dd-HHmmss.mp4).");
    }

    String format = fileName.substring(fileName.length() - 4, fileName.length());
    if (!format.equals(".mp4")) {
      throw new InputVideoNameException("The default drone name isn't correct."
          + " Must be used the standard name (DRON-yyyy-MM-dd-HHmmss.mp4).");
    }

    String date = fileName.substring(5, fileName.length() - 4);
    System.out.println(date);
    try {
      DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss");
      LocalDate.parse(date, dateFormat);
    } catch (Exception exc) {
      throw new InputVideoNameException("The delivery date-time isn't correct."
          + " Must be used the standard name (DRON-yyyy-MM-dd-HHmmss.mp4).");
    }
  }

}
