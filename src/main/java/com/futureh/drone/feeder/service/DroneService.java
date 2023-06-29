package com.futureh.drone.feeder.service;

import com.futureh.drone.feeder.dto.DroneDto;
import com.futureh.drone.feeder.model.Drone;
import com.futureh.drone.feeder.repository.DroneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * DroneService class.
 */
@Service
public class DroneService {

  @Autowired
  private DroneRepository droneRepository;

  /** addDrone method.*/
  public Drone addDrone(DroneDto drone) {
    String name = drone.getName();
    String model = drone.getModel();
    Drone newDrone = droneRepository.save(new Drone(name, model));
    return newDrone;
  }

}
