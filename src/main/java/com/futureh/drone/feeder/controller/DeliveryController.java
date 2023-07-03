package com.futureh.drone.feeder.controller;

import com.futureh.drone.feeder.dto.DeliveryDto;
import com.futureh.drone.feeder.dto.VideoDto;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DeliveryController class.
 */
@RestController
@RequestMapping("/delivery")
public class DeliveryController {

  @Autowired
  private DeliveryService deliveryService;

  /** addDelivery method.*/
  @PostMapping("/new")
  public ResponseEntity<Delivery> addDelivery(@RequestBody DeliveryDto delivery) {
    DeliveryMiddleware.isValidDelivery(delivery);

    Delivery newDelivery = deliveryService.addDelivery(delivery);
    return ResponseEntity.ok(newDelivery);
  }

  /** addVideo method.*/
  @PostMapping("/addVideo")
  public ResponseEntity<Delivery> addVideo(@RequestBody VideoDto video) throws IOException {
    VideoNameMiddleware.isValidName(video.getVideoName());

    Delivery response = deliveryService.addVideo(video);
    return ResponseEntity.ok(response);
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
  @GetMapping("/video/{id}")
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
  public ResponseEntity<DeliveryResponse> getDeliveryById(@PathVariable("id") Long id) {
    Delivery delivery = deliveryService.getDeliveryById(id);

    DeliveryResponse deliveryResponse = new DeliveryResponse();
    deliveryResponse.createResponseByDeliveryEntity(delivery);

    return ResponseEntity.ok(deliveryResponse);
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
  public ResponseEntity<Delivery> updateDelivery(@PathVariable("id") Long id,
      @RequestBody DeliveryDto delivery) {
    DeliveryMiddleware.isValidDelivery(delivery);

    Delivery deliveryUpdated = deliveryService.updateDelivery(id, delivery);
    return ResponseEntity.ok(deliveryUpdated);
  }

}
