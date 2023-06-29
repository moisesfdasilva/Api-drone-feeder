package com.futureh.drone.feeder.service;

import com.futureh.drone.feeder.dto.DeliveryDto;
import com.futureh.drone.feeder.model.Delivery;
import com.futureh.drone.feeder.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * DeliveryService class.
 */
@Service
public class DeliveryService {

  @Autowired
  private DeliveryRepository deliveryRepository;

  /** addDelivery method.*/
  public Delivery addDelivery(DeliveryDto delivery) {
    String address = delivery.getAddress();
    String zipCode = delivery.getZipCode();
    String longitude = delivery.getLongitude();
    String latitude = delivery.getLatitude();
    Integer weightInKg = delivery.getWeightInKg();
    Delivery newDelivery = deliveryRepository.save(
        new Delivery(address, zipCode, longitude, latitude, weightInKg));
    return newDelivery;
  }

}
