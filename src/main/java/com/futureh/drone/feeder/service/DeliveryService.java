package com.futureh.drone.feeder.service;

import com.futureh.drone.feeder.dto.DeliveryDto;
import com.futureh.drone.feeder.exception.InputNotFoundException;
import com.futureh.drone.feeder.exception.IntServerErrorInVideoFinding;
import com.futureh.drone.feeder.exception.WrongInputDataException;
import com.futureh.drone.feeder.model.Delivery;
import com.futureh.drone.feeder.model.Drone;
import com.futureh.drone.feeder.model.Video;
import com.futureh.drone.feeder.repository.DeliveryRepository;
import com.futureh.drone.feeder.repository.VideoRepository;
import com.futureh.drone.feeder.util.DeliveryStatus;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * DeliveryService class.
 */
@Service
public class DeliveryService {

  String paramWithoutVideo = "The param don't have a video (Param must have a video).";
  String videoIdNotFound = "Video id not found.";
  String deliveryIdNotFound = "Delivery id not found.";

  @Autowired
  private DeliveryRepository deliveryRepository;

  @Autowired
  private DroneService droneService;

  @Autowired
  private VideoRepository videoRepository;

  /** addDelivery method.*/
  public Delivery addDelivery(DeliveryDto delivery) {
    String receiverName = delivery.getReceiverName();
    String address = delivery.getAddress();
    String zipCode = delivery.getZipCode();
    String latitude = delivery.getLatitude();
    String longitude = delivery.getLongitude();
    Float weightInKg = delivery.getWeightInKg();
    Delivery newDelivery = deliveryRepository.save(
        new Delivery(receiverName, address, zipCode, latitude, longitude, weightInKg));
    return newDelivery;
  }

  /** getVideoByName method.*/
  public Video getVideoByName(String videoName) {
    List<Video> videos = videoRepository.findAll();
    Video video = videos.stream()
        .filter(vdo -> vdo.getFileName().equals(videoName))
        .findAny().orElse(null);
    return video;
  }

  /** saveFile method.*/
  public void saveFile(String videoName, MultipartFile multipartFile) throws IOException {
    Path uploadDirectory = Paths.get("videos-uploads");

    try {
      InputStream inputStream = multipartFile.getInputStream();
      Path videoPath = uploadDirectory.resolve(videoName);
      Files.copy(inputStream, videoPath, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException err) {
      throw new WrongInputDataException(paramWithoutVideo);
    }
  }

  /** addVideo method.*/
  public Delivery addVideo(Long id, Video video) throws IOException {
    String videoName = video.getFileName();
    String droneName = videoName.substring(0, 4);
    Drone drone = droneService.getDroneByName(droneName);
    video.setDrone(drone);

    Delivery delivery = this.getDeliveryById(id);

    delivery.setVideo(video);
    delivery.setStatus(DeliveryStatus.DELIVERED);
    Delivery deliveryUpdate = deliveryRepository.save(delivery);

    return deliveryUpdate;
  }

  /** getAllVideos method.*/
  public List<Video> getAllVideos() {
    return videoRepository.findAll();
  }

  /** getVideoById method.*/
  public Video getVideoById(Long id) {
    Video video = videoRepository.findById(id).orElse(null);
    if (video != null) {
      return video;      
    } else {
      throw new InputNotFoundException(videoIdNotFound);
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
      throw new InputNotFoundException(deliveryIdNotFound);
    }
  }

  /** removeDelivery method.*/
  public Long removeDelivery(Long id) {
    Delivery delivery = deliveryRepository.findById(id).orElse(null);
    if (delivery != null) {
      deliveryRepository.delete(delivery);
      return id;
    } else {
      throw new InputNotFoundException(deliveryIdNotFound);
    }
  }

  /** updateDelivery method.*/
  public Delivery updateDelivery(Long id, DeliveryDto delivery) {
    Delivery deliveryUpdate = deliveryRepository.findById(id).orElse(null);
    if (deliveryUpdate != null) {
      deliveryUpdate.setReceiverName(delivery.getReceiverName());
      deliveryUpdate.setAddress(delivery.getAddress());
      deliveryUpdate.setZipCode(delivery.getZipCode());
      deliveryUpdate.setLongitude(delivery.getLongitude());
      deliveryUpdate.setLatitude(delivery.getLatitude());
      deliveryUpdate.setWeightInKg(delivery.getWeightInKg());
      return deliveryRepository.save(deliveryUpdate);
    } else {
      throw new InputNotFoundException(deliveryIdNotFound);
    }
  }

  /** getVideoAsResource method.*/
  public Resource getVideoAsResource(String videoName) throws IOException {
    Path uploadDirectory = Paths.get("videos-uploads");
    Stream<Path> files = Files.list(uploadDirectory);
    Path foundVideo = files.filter(video -> video.getFileName().toString().equals(videoName))
        .findAny().orElse(null);

    files.close();

    if (foundVideo != null) {
      return new UrlResource(foundVideo.toUri());
    } else {
      return null;
    }
  }

  /** deleteVideo method.*/
  public Delivery deleteVideo(Long id, String videoName) throws IOException {
    Path uploadDirectory = Paths.get("videos-uploads");
    Stream<Path> files = Files.list(uploadDirectory);
    Path videoPath = files.filter(video -> video.getFileName().toString().equals(videoName))
        .findAny().orElse(null);
    File myObj = new File(videoPath.toUri());
    files.close();

    if (myObj.delete()) {
      Delivery deliveryUpdate = deliveryRepository.findById(id).orElse(null);
      if (deliveryUpdate != null) {
        deliveryUpdate.setVideo(null);
        deliveryUpdate.setStatus(DeliveryStatus.TO_DELIVER);
        return deliveryRepository.save(deliveryUpdate);
      } else {
        throw new InputNotFoundException(deliveryIdNotFound);
      }
    } else {
      throw new IntServerErrorInVideoFinding();
    }
  }

}
