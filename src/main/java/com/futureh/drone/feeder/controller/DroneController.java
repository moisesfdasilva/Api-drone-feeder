package com.futureh.drone.feeder.controller;

import com.futureh.drone.feeder.dto.DroneDto;
import com.futureh.drone.feeder.model.Drone;
import com.futureh.drone.feeder.service.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DroneController class.
 */
@RestController
@RequestMapping("/drone")
public class DroneController {

  @Autowired
  private DroneService droneService;

  /** addDrone method.*/
  @PostMapping("/new")
  public ResponseEntity<Drone> addDrone(@RequestBody DroneDto drone) {
    Drone response = droneService.addDrone(drone);
    return ResponseEntity.ok(response);
  }
  
}
