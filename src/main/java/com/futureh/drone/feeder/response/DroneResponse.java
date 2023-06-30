package com.futureh.drone.feeder.response;

import com.futureh.drone.feeder.model.Drone;

/**
 * DroneResponse class.
 */
public class DroneResponse {

  private Long id;
  private String name;
  private String model;
  private Float capacityWeightInKg;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public Float getCapacityWeightInKg() {
    return capacityWeightInKg;
  }

  public void setCapacityWeightInKg(Float capacityWeightInKg) {
    this.capacityWeightInKg = capacityWeightInKg;
  }

  /** createResponseByDroneEntity method.*/
  public void createResponseByDroneEntity(Drone drone) {
    setId(drone.getId());
    setName(drone.getName());
    setModel(drone.getModel());
    setCapacityWeightInKg(drone.getCapacityWeightInKg());
  }

}
