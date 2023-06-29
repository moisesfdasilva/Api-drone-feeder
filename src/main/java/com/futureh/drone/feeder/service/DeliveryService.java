package com.futureh.drone.feeder.service;

import com.futureh.drone.feeder.dto.DeliveryDto;
import com.futureh.drone.feeder.dto.VideoDto;
import com.futureh.drone.feeder.model.Delivery;
import com.futureh.drone.feeder.model.Drone;
import com.futureh.drone.feeder.model.Video;
import com.futureh.drone.feeder.repository.DeliveryRepository;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

/**
 * DeliveryService class.
 */
@Service
public class DeliveryService {

  @Autowired
  private DeliveryRepository deliveryRepository;

  @Autowired
  private VideoDownloadService videoDownloadService;

  @Autowired
  private DroneService droneService;

  /** addDelivery method.*/
  public Delivery addDelivery(DeliveryDto delivery) {
    String address = delivery.getAddress();
    String zipCode = delivery.getZipCode();
    String longitude = delivery.getLongitude();
    String latitude = delivery.getLatitude();
    Float weightInKg = delivery.getWeightInKg();
    Delivery newDelivery = deliveryRepository.save(
        new Delivery(address, zipCode, longitude, latitude, weightInKg));
    return newDelivery;
  }

  /** addVideo method. */
  public String addVideo(VideoDto video) throws IOException {
    // verificar a existencia do upload do video
    String videoName = video.getVideoName();
    Resource resource = videoDownloadService.getVideoAsResource(videoName);
    Long size = resource.contentLength();
    Video xvideo = new Video(videoName, size);
    // verificar a existencia do drone
    String droneName = videoName.substring(0, 4);
    Drone drone = droneService.getDroneByName(droneName);
    xvideo.setDrone(drone);
    // verificar a existencia da entrega
    Long deliveryId = video.getDeliveryId();
    System.out.println("XXXXXXXXXXXXX3");
    Delivery delivery = deliveryRepository.findById(deliveryId).orElse(null);
    System.out.println("XXXXXXXXXXXXX4");
    delivery.setVideo(xvideo);
    System.out.println("XXXXXXXXXXXXX5");
    Delivery deliveryA = deliveryRepository.save(delivery);
    System.out.println("XXXXXXXXXXXXX6");
    return "deliveryA";
  }

}
