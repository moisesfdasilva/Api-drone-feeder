package com.futureh.drone.feeder.middleware;

import com.futureh.drone.feeder.exception.WrongInputDataException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * VideoNameMiddleware class.
 */
public class VideoNameMiddleware {

  /**
   * isValidName method.
   */
  public static void isValidName(String fileName) {
    if (fileName.isEmpty()) {
      throw new WrongInputDataException("The param don't have a video (Param must have a video).");
    }

    if (fileName.length() != 26) {
      throw new WrongInputDataException("The video name length isn't 26 characters."
          + " Must be used the standard name (DRON-yyyy-MM-dd-HHmmss.mp4).");
    }

    String droneName = fileName.substring(0, 5);
    if (!droneName.matches("^(?:\\w{4})-$")) {
      throw new WrongInputDataException("The default drone name isn't correct."
          + " Must be used the standard name (DRON-yyyy-MM-dd-HHmmss.mp4).");
    }

    String format = fileName.substring(fileName.length() - 4, fileName.length());
    if (!format.equals(".mp4")) {
      throw new WrongInputDataException("The video format isn't correct."
          + " Must be used the standard name (DRON-yyyy-MM-dd-HHmmss.mp4).");
    }

    String date = fileName.substring(5, fileName.length() - 4);
    try {
      DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss");
      LocalDate.parse(date, dateFormat);
    } catch (Exception exc) {
      throw new WrongInputDataException("The delivery date-time isn't correct."
          + " Must be used the standard name (DRON-yyyy-MM-dd-HHmmss.mp4).");
    }
  }

}
