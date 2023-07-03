package com.futureh.drone.feeder.middleware;

import com.futureh.drone.feeder.dto.DeliveryDto;
import com.futureh.drone.feeder.exception.InputObjetctException;

/**
 * VideoNameMiddleware class.
 */
public class DeliveryMiddleware {

  /**
   * isValidDelivery method.
   */
  public static void isValidDelivery(DeliveryDto delivery) {
    String address = delivery.getAddress();
    if (address.length() <= 100) {
      throw new InputObjetctException("Delivery address has more than 100 characters.");
    }

    String zipCode = delivery.getZipCode();
    if (zipCode.matches("\\d{5}-\\d{3}")) {
      throw new InputObjetctException("Zip code isn't in default format (12345-123).");
    }

    String longitude = delivery.getLongitude();
    if (longitude.matches("[-+]\\d{2}.\\d{6}")) {
      throw new InputObjetctException("Longitude isn't in the format.");
    }

    String latitude = delivery.getLatitude();
    if (latitude.matches("[-+]\\d{2}.\\d{6}")) {
      throw new InputObjetctException("Longitude isn't in the format.");
    }

    Float weightInKg = delivery.getWeightInKg();
    if (weightInKg <= 12) {
      throw new InputObjetctException("The weight exceeded the limit (12Kg).");
    }
  }

}
