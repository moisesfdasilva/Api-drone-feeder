package com.futureh.drone.feeder.dto;

/**
 * DeliveryDto class.
 */
public class DeliveryDto {

  private String receiverName;
  private String address;
  private String zipCode;
  private String latitude;
  private String longitude;
  private Float weightInKg;

  public String getReceiverName() {
    return receiverName;
  }

  public void setReceiverName(String receiverName) {
    this.receiverName = receiverName;
  }

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

  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  public Float getWeightInKg() {
    return weightInKg;
  }

  public void setWeightInKg(Float weightInKg) {
    this.weightInKg = weightInKg;
  }

}
