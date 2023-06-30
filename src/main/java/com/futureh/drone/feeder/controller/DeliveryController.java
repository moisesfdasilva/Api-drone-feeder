package com.futureh.drone.feeder.controller;

import com.futureh.drone.feeder.dto.DeliveryDto;
import com.futureh.drone.feeder.dto.VideoDto;
import com.futureh.drone.feeder.model.Delivery;
import com.futureh.drone.feeder.model.Video;
import com.futureh.drone.feeder.service.DeliveryService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    Delivery response = deliveryService.addDelivery(delivery);
    return ResponseEntity.ok(response);
  }

  /** addVideo method.*/
  @PostMapping("/addVideo")
  public ResponseEntity<Delivery> addVideo(@RequestBody VideoDto video) throws IOException {
    Delivery response = deliveryService.addVideo(video);
    return ResponseEntity.ok(response);
  }

  /** getAllVideos method.*/
  @GetMapping("/allVideos")
  public ResponseEntity<List<Video>> getAllVideos() {
    List<Video> response = deliveryService.getAllVideos();
    return ResponseEntity.ok(response);
  }

  /** getAllDeliveries method.*/
  @GetMapping("/all")
  public ResponseEntity<List<Delivery>> getAllDeliveries() {
    List<Delivery> response = deliveryService.getAllDeliveries();
    return ResponseEntity.ok(response);
  }

}
