package com.futureh.drone.feeder.middleware;

import com.futureh.drone.feeder.dto.DroneDto;
import com.futureh.drone.feeder.exception.InputObjetctException;

/**
 * VideoNameMiddleware class.
 */
public class DroneMiddleware {

  /**
   * isValidDelivery method.
   */
  public static void isValidDrone(DroneDto drone) {
    String name = drone.getName(); 
    if (name.length() <= 4) {
      throw new InputObjetctException("Drone name has more than 4 characters.");
    }
    if (name.matches("[A-Z_0-9]{4}")) {
      throw new InputObjetctException("Drone name must have uppercase characters and numbers.");
    }
    String model = drone.getModel(); 
    if (model.length() <= 32) {
      throw new InputObjetctException("Drone name has more than 32 characters.");
    }
  }

}
