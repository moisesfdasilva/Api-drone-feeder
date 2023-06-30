package com.futureh.drone.feeder.controller;

import com.futureh.drone.feeder.dto.DroneDto;
import com.futureh.drone.feeder.model.Drone;
import com.futureh.drone.feeder.response.DroneResponse;
import com.futureh.drone.feeder.service.DroneService;
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

  /** getAllDrones method.*/
  @GetMapping("/all")
  public ResponseEntity<List<DroneResponse>> getAllDrones() {
    List<Drone> drones = droneService.getAllDrones();

    List<DroneResponse> dronesResponse = drones.stream().map(drn -> {
      DroneResponse droneResponse = new DroneResponse();
      droneResponse.createResponseByDroneEntity(drn);
      return droneResponse;
    }).collect(Collectors.toList());

    return ResponseEntity.ok(dronesResponse);
  }

  /** getDroneById method.*/
  @GetMapping("/{id}")
  public ResponseEntity<DroneResponse> getDroneById(@PathVariable("id") Long id) {
    Drone drone = droneService.getDroneById(id);

    DroneResponse droneResponse = new DroneResponse();
    droneResponse.createResponseByDroneEntity(drone);

    return ResponseEntity.ok(droneResponse);
  }

  /** removeDrone method.*/
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> removeDrone(@PathVariable("id") Long id) {
    Long idRemoved = droneService.removeDrone(id);
    String response = "Id " + idRemoved + " has been removed.";
    return ResponseEntity.ok(response);
  }

  /** updateDrone method.*/
  @PutMapping("/update/{id}")
  public ResponseEntity<Drone> updateDrone(@PathVariable("id") Long id,
      @RequestBody DroneDto drone) {
    Drone droneUpdated = droneService.updateDrone(id, drone);
    return ResponseEntity.ok(droneUpdated);
  }

}
