package com.futureh.drone.feeder.dto;

/**
 * DroneDto class.
 */
public class DroneDto {

  private String name;
  private String model;
  private Float capacityWeightInKg;

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

}
