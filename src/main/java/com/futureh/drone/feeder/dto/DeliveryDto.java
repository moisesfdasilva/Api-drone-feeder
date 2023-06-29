package com.futureh.drone.feeder.dto;

/**
 * DeliveryDto class.
 */
public class DeliveryDto {

  private String address;
  private String zipCode;
  private String longitude;
  private String latitude;
  private Float weightInKg;

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  public Float getWeightInKg() {
    return weightInKg;
  }

  public void setWeightInKg(Float weightInKg) {
    this.weightInKg = weightInKg;
  }

}
