package com.futureh.drone.feeder.service;

import com.futureh.drone.feeder.dto.DeliveryDto;
import com.futureh.drone.feeder.dto.VideoDto;
import com.futureh.drone.feeder.exception.InputNotFoundException;
import com.futureh.drone.feeder.model.Delivery;
import com.futureh.drone.feeder.model.Drone;
import com.futureh.drone.feeder.model.Video;
import com.futureh.drone.feeder.repository.DeliveryRepository;
import com.futureh.drone.feeder.repository.VideoRepository;
import com.futureh.drone.feeder.util.DeliveryStatus;
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
    String videoName = video.getVideoName();
    Resource resource = videoDownloadService.getVideoAsResource(videoName);
    Long size = resource.contentLength();
    Video newVideo = new Video(videoName, size);

    String droneName = videoName.substring(0, 4);
    Drone drone = droneService.getDroneByName(droneName);
    newVideo.setDrone(drone);

    Long deliveryId = video.getDeliveryId();
    Delivery delivery = deliveryRepository.findById(deliveryId).orElse(null);
    delivery.setVideo(newVideo);
    delivery.setStatus(DeliveryStatus.DELIVERED);
    Delivery deliveryUpdate = deliveryRepository.save(delivery);
    return deliveryUpdate;
  }

  /** getAllVideos method. */
  public List<Video> getAllVideos() {
    return videoRepository.findAll();
  }

  /** getVideoById method. */
  public Video getVideoById(Long id) {
    Video video = videoRepository.findById(id).orElse(null);
    if (video != null) {
      return video;      
    } else {
      throw new InputNotFoundException("Video id not found.");
    }
  }

  /** getAllDeliveries method.*/
  public List<Delivery> getAllDeliveries() {
    return deliveryRepository.findAll();
  }

  /** getDeliveryById method.*/
  public Delivery getDeliveryById(Long id) {
    Delivery delivery = deliveryRepository.findById(id).orElse(null);
    if (delivery != null) {
      return delivery;      
    } else {
      throw new InputNotFoundException("Delivery id not found.");
    }
  }

  /** removeDelivery method.*/
  public Long removeDelivery(Long id) {
    Delivery delivery = deliveryRepository.findById(id).orElse(null);
    if (delivery != null) {
      deliveryRepository.delete(delivery);
      return id;
    } else {
      return null;
    }
  }

  /** updateDelivery method.*/
  public Delivery updateDelivery(Long id, DeliveryDto delivery) {
    Delivery deliveryUpdate = deliveryRepository.findById(id).orElse(null);
    if (deliveryUpdate != null) {
      deliveryUpdate.setAddress(delivery.getAddress());
      deliveryUpdate.setZipCode(delivery.getZipCode());
      deliveryUpdate.setLongitude(delivery.getLongitude());
      deliveryUpdate.setLatitude(delivery.getLatitude());
      deliveryUpdate.setWeightInKg(delivery.getWeightInKg());
      return deliveryRepository.save(deliveryUpdate);
    } else {
      return null;
    }
  }
  
}
