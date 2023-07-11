package com.futureh.drone.feeder.middleware;

import com.futureh.drone.feeder.dto.DroneDto;
import com.futureh.drone.feeder.exception.WrongInputDataException;

/**
 * VideoNameMiddleware class.
 */
public class DroneMiddleware {

  /**
   * isValidDelivery method.
   */
  public static void isValidDrone(DroneDto drone) {
    String name = drone.getName(); 
    if (name.length() != 4) {
      throw new WrongInputDataException("Drone name must be 4 characters.");
    }
    if (!name.matches("[A-Z_0-9]{4}")) {
      throw new WrongInputDataException("Drone name must have uppercase characters and numbers.");
    }

    String model = drone.getModel(); 
    if (model.length() > 32) {
      throw new WrongInputDataException("Drone name has more than 32 characters.");
    }

    Float capacity = drone.getCapacityWeightInKg();
    if (capacity == null) {
      throw new WrongInputDataException("Drone must have weight capacity in kg.");
    }
  }

}
