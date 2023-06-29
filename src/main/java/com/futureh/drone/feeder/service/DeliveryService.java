package com.futureh.drone.feeder.service;

import com.futureh.drone.feeder.dto.DeliveryDto;
import com.futureh.drone.feeder.dto.VideoDto;
import com.futureh.drone.feeder.model.Delivery;
import com.futureh.drone.feeder.model.Drone;
import com.futureh.drone.feeder.model.Video;
import com.futureh.drone.feeder.repository.DeliveryRepository;
import com.futureh.drone.feeder.repository.VideoRepository;
import java.io.IOException;
import java.util.List;
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

  @Autowired
  private VideoRepository videoRepository;

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
  public Delivery addVideo(VideoDto video) throws IOException {
    // verificar a existencia do upload do video
    String videoName = video.getVideoName();
    Resource resource = videoDownloadService.getVideoAsResource(videoName);
    Long size = resource.contentLength();
    Video newVideo = new Video(videoName, size);

    // verificar a existencia do drone
    String droneName = videoName.substring(0, 4);
    Drone drone = droneService.getDroneByName(droneName);
    newVideo.setDrone(drone);

    // verificar a existencia da entrega
    Long deliveryId = video.getDeliveryId();
    Delivery delivery = deliveryRepository.findById(deliveryId).orElse(null);
    delivery.setVideo(newVideo);
    Delivery deliveryUpdate = deliveryRepository.save(delivery);
    return deliveryUpdate;
  }

  /** addVideo method. */
  public List<Video> getAllVideos() {
    return videoRepository.findAll();
  }

}
