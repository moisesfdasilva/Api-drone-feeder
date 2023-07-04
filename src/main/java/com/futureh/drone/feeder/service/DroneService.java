package com.futureh.drone.feeder.service;

import com.futureh.drone.feeder.dto.DroneDto;
import com.futureh.drone.feeder.exception.InputNotFoundException;
import com.futureh.drone.feeder.model.Drone;
import com.futureh.drone.feeder.repository.DroneRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * DroneService class.
 */
@Service
public class DroneService {

  String droneIdNotFound = "Drone id not found.";
  String droneNameNotFound = "Drone name not found.";

  @Autowired
  private DroneRepository droneRepository;

  /** addDrone method.*/
  public Drone addDrone(DroneDto drone) {
    String name = drone.getName();
    String model = drone.getModel();
    Float capacityWeightInKg = drone.getCapacityWeightInKg();
    Drone newDrone = droneRepository.save(new Drone(name, model, capacityWeightInKg));
    return newDrone;
  }

  /** getDroneByName method.*/
  public Drone getDroneByName(String droneName) {
    List<Drone> drones = droneRepository.findAll();
    Drone drone = drones.stream()
        .filter(drn -> drn.getName().equals(droneName))
        .findAny().orElse(null);
    if (drone != null) {
      return drone;      
    } else {
      throw new InputNotFoundException(droneNameNotFound);
    }
  }

  /** getAllDrones method.*/
  public List<Drone> getAllDrones() {
    return droneRepository.findAll();
  }

  /** getDroneById method.*/
  public Drone getDroneById(Long id) {
    Drone drone = droneRepository.findById(id).orElse(null);
    if (drone != null) {
      return drone;      
    } else {
      throw new InputNotFoundException(droneIdNotFound);
    }
  }

  /** removeDrone method.*/
  public Long removeDrone(Long id) {
    Drone drone = droneRepository.findById(id).orElse(null);
    if (drone != null) {
      droneRepository.delete(drone);
      return id;
    } else {
      throw new InputNotFoundException(droneIdNotFound);
    }
  }

  /** updateDrone method.*/
  public Drone updateDrone(Long id, DroneDto drone) {
    Drone droneUpdate = droneRepository.findById(id).orElse(null);
    if (droneUpdate != null) {
      droneUpdate.setName(drone.getName());
      droneUpdate.setModel(drone.getModel());
      droneUpdate.setCapacityWeightInKg(drone.getCapacityWeightInKg());
      droneRepository.save(droneUpdate);
      return droneRepository.save(droneUpdate);
    } else {
      throw new InputNotFoundException(droneIdNotFound);
    }
  }

}
