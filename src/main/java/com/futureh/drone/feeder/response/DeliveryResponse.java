package com.futureh.drone.feeder.response;

import com.futureh.drone.feeder.model.Delivery;
import com.futureh.drone.feeder.util.DeliveryStatus;

/**
 * DeliveryResponse class.
 */
public class DeliveryResponse {

  private Long id;
  private String receiverName;
  private String address;
  private String zipCode;
  private String longitude;
  private String latitude;
  private DeliveryStatus status;
  private Float weightInKg;
  private String videoName;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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

  public DeliveryStatus getStatus() {
    return status;
  }

  public void setStatus(DeliveryStatus status) {
    this.status = status;
  }

  public Float getWeightInKg() {
    return weightInKg;
  }

  public void setWeightInKg(Float weightInKg) {
    this.weightInKg = weightInKg;
  }

  public String getVideoName() {
    return videoName;
  }

  public void setVideoName(String videoName) {
    this.videoName = videoName;
  }

  /**createResponseByDeliveryEntity method.*/
  public void createResponseByDeliveryEntity(Delivery delivery) {
    setId(delivery.getId());
    setReceiverName(delivery.getReceiverName());
    setAddress(delivery.getAddress());
    setZipCode(delivery.getZipCode());
    setLongitude(delivery.getLongitude());
    setLatitude(delivery.getLongitude());
    setStatus(delivery.getStatus());
    setWeightInKg(delivery.getWeightInKg());

    if (delivery.getVideo() != null) {
      setVideoName(delivery.getVideo().getFileName());      
    } else {
      setVideoName("None");
    }
  }

}
