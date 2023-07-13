package com.futureh.drone.feeder.controller;

import com.futureh.drone.feeder.dto.DroneDto;
import com.futureh.drone.feeder.middleware.DroneMiddleware;
import com.futureh.drone.feeder.model.Drone;
import com.futureh.drone.feeder.response.DroneResponse;
import com.futureh.drone.feeder.service.DroneService;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
  public ResponseEntity<DroneResponse> addDrone(@RequestBody DroneDto drone) {
    DroneMiddleware.isValidDrone(drone);

    Drone newDrone = droneService.addDrone(drone);

    DroneResponse newDroneResponse = new DroneResponse();
    newDroneResponse.createResponseByDroneEntity(newDrone);

    return ResponseEntity.status(HttpStatus.CREATED).body(newDroneResponse);
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
  public ResponseEntity<HashMap<String, String>> removeDrone(@PathVariable("id") Long id) {
    Long idRemoved = droneService.removeDrone(id);

    HashMap<String, String> response = new HashMap<String, String>();
    String message = "Id " + idRemoved + " has been removed.";
    response.put("message", message);

    return ResponseEntity.ok(response);
  }

  /** updateDrone method.*/
  @PutMapping("/update/{id}")
  public ResponseEntity<DroneResponse> updateDrone(@PathVariable("id") Long id,
      @RequestBody DroneDto drone) {
    DroneMiddleware.isValidDrone(drone);

    Drone droneUpdated = droneService.updateDrone(id, drone);

    DroneResponse droneUpdatedResponse = new DroneResponse();
    droneUpdatedResponse.createResponseByDroneEntity(droneUpdated);

    return ResponseEntity.ok(droneUpdatedResponse);
  }

}
