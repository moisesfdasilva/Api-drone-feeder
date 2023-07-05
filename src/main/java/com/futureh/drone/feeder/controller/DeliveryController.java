package com.futureh.drone.feeder.controller;

import com.futureh.drone.feeder.dto.DeliveryDto;
import com.futureh.drone.feeder.exception.ConflictWithInputDataException;
import com.futureh.drone.feeder.exception.InputNotFoundException;
import com.futureh.drone.feeder.exception.IntServerErrorInVideoFinding;
import com.futureh.drone.feeder.middleware.DeliveryMiddleware;
import com.futureh.drone.feeder.middleware.VideoNameMiddleware;
import com.futureh.drone.feeder.model.Delivery;
import com.futureh.drone.feeder.model.Video;
import com.futureh.drone.feeder.response.DeliveryResponse;
import com.futureh.drone.feeder.response.VideoDetailsResponse;
import com.futureh.drone.feeder.response.VideoResponse;
import com.futureh.drone.feeder.service.DeliveryService;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * DeliveryController class.
 */
@RestController
@RequestMapping("/delivery")
public class DeliveryController {

  String videoAlreadyExists = "The video already exists.";
  String deliveryHasntVideo = "The delivery hasn't video";

  @Autowired
  private DeliveryService deliveryService;

  /** addDelivery method.*/
  @PostMapping("/new")
  public ResponseEntity<DeliveryResponse> addDelivery(@RequestBody DeliveryDto delivery) {
    DeliveryMiddleware.isValidDelivery(delivery);

    Delivery newDelivery = deliveryService.addDelivery(delivery);

    DeliveryResponse newDeliveryResponse = new DeliveryResponse();
    newDeliveryResponse.createResponseByDeliveryEntity(newDelivery);

    return ResponseEntity.ok(newDeliveryResponse);
  }

  /** addVideo method.*/
  @PostMapping("/{id}/uploadVideo")
  public ResponseEntity<DeliveryResponse> addVideo(@PathVariable("id") Long id,
      @RequestParam("video") MultipartFile multipartFile) throws IOException {
    String videoName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
    VideoNameMiddleware.isValidName(videoName);

    Video video = deliveryService.getVideoByName(videoName);
    if (video != null) {
      throw new ConflictWithInputDataException(videoAlreadyExists);
    }

    deliveryService.saveFile(videoName, multipartFile);

    Long size = multipartFile.getSize();
    Video newVideo = new Video(videoName, size);

    Delivery deliveryUpdated = deliveryService.addVideo(id, newVideo);
    DeliveryResponse deliveryUpdatedResponse = new DeliveryResponse();
    deliveryUpdatedResponse.createResponseByDeliveryEntity(deliveryUpdated);

    return ResponseEntity.ok(deliveryUpdatedResponse);
  }

  /** getAllVideos method.*/
  @GetMapping("/allVideos")
  public ResponseEntity<List<VideoResponse>> getAllVideos() {
    List<Video> videos = deliveryService.getAllVideos();

    List<VideoResponse> videosResponse = videos.stream().map(vdo -> {
      VideoResponse videoResponse = new VideoResponse();
      videoResponse.createResponseByVideoEntity(vdo);
      return videoResponse;
    }).collect(Collectors.toList());

    return ResponseEntity.ok(videosResponse);
  }

  /** getVideoDetails method.*/
  @GetMapping("/{id}/video")
  public ResponseEntity<VideoDetailsResponse> getVideoDetails(@PathVariable("id") Long id) {
    Video video = deliveryService.getVideoById(id);

    VideoDetailsResponse videoDetailsResponse = new VideoDetailsResponse();
    videoDetailsResponse.createResponseByVideoEntity(video);

    return ResponseEntity.ok(videoDetailsResponse);
  }

  /** getAllDeliveries method.*/
  @GetMapping("/all")
  public ResponseEntity<List<DeliveryResponse>> getAllDeliveries() {
    List<Delivery> deliveries = deliveryService.getAllDeliveries();

    List<DeliveryResponse> deliveriesResponse = deliveries.stream().map(dlv -> {
      DeliveryResponse deliveryResponse = new DeliveryResponse();
      deliveryResponse.createResponseByDeliveryEntity(dlv);
      return deliveryResponse;
    }).collect(Collectors.toList());

    return ResponseEntity.ok(deliveriesResponse);
  }

  /** getDeliveryById method.*/
  @GetMapping("/{id}")
  public ResponseEntity<Delivery> getDeliveryById(@PathVariable("id") Long id) {
    Delivery delivery = deliveryService.getDeliveryById(id);

    return ResponseEntity.ok(delivery);
  }

  /** removeDelivery method.*/
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> removeDelivery(@PathVariable("id") Long id) {
    Long idRemoved = deliveryService.removeDelivery(id);
    String response = "Id " + idRemoved + " has been removed.";
    return ResponseEntity.ok(response);
  }

  /** updateDelivery method.*/
  @PutMapping("/update/{id}")
  public ResponseEntity<DeliveryResponse> updateDelivery(@PathVariable("id") Long id,
      @RequestBody DeliveryDto delivery) {
    DeliveryMiddleware.isValidDelivery(delivery);

    Delivery deliveryUpdated = deliveryService.updateDelivery(id, delivery);

    DeliveryResponse deliveryUpdatedResponse = new DeliveryResponse();
    deliveryUpdatedResponse.createResponseByDeliveryEntity(deliveryUpdated);

    return ResponseEntity.ok(deliveryUpdatedResponse);
  }

  /** downloadVideo method.*/
  @GetMapping("/{id}/downloadVideo")
  public ResponseEntity<?> downloadVideo(@PathVariable("id") Long id) {
    Delivery delivery = deliveryService.getDeliveryById(id);
    Video video = delivery.getVideo();
    if (video == null) {
      throw new InputNotFoundException(deliveryHasntVideo);
    }

    String videoName = video.getFileName();
    Resource resource = null;
    try {
      resource = deliveryService.getVideoAsResource(videoName);
      if (resource == null) {
        throw new IOException();
      }
    } catch (IOException e) {
      throw new IntServerErrorInVideoFinding();
    }

    String contentType = "application/octet-stream";
    String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(contentType))
        .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
        .body(resource);
  }

  /** deleteVideo method.*/
  @DeleteMapping("/{id}/deleteVideo")
  public ResponseEntity<DeliveryResponse> deleteVideo(@PathVariable("id") Long id)
      throws IOException {
    Delivery delivery = deliveryService.getDeliveryById(id);
    Video video = delivery.getVideo();
    if (video == null) {
      throw new InputNotFoundException(deliveryHasntVideo);
    }

    String videoName = video.getFileName();
    Delivery deliveryUpdated = deliveryService.deleteVideo(id, videoName);

    DeliveryResponse deliveryUpdatedResponse = new DeliveryResponse();
    deliveryUpdatedResponse.createResponseByDeliveryEntity(deliveryUpdated);

    return ResponseEntity.ok(deliveryUpdatedResponse);
  }

}
