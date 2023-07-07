package com.futureh.drone.feeder.response;

import com.futureh.drone.feeder.model.Delivery;
import com.futureh.drone.feeder.util.DeliveryStatus;

/**
 * DeliveryDetailsResponse class.
 */
public class DeliveryDetailsResponse {

  private Long id;
  private String receiverName;
  private String address;
  private String zipCode;
  private String latitude;
  private String longitude;
  private DeliveryStatus status;
  private Float weightInKg;
  private VideoResponse video;

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

  public VideoResponse getVideo() {
    return video;
  }

  public void setVideo(VideoResponse video) {
    this.video = video;
  }

  /**createResponseByDeliveryEntity method.*/
  public void createResponseByDeliveryEntity(Delivery delivery) {
    setId(delivery.getId());
    setReceiverName(delivery.getReceiverName());
    setAddress(delivery.getAddress());
    setZipCode(delivery.getZipCode());
    setLatitude(delivery.getLatitude());
    setLongitude(delivery.getLongitude());
    setStatus(delivery.getStatus());
    setWeightInKg(delivery.getWeightInKg());

    if (delivery.getVideo() != null) {
      VideoResponse videoResponse = new VideoResponse();
      videoResponse.createResponseByVideoEntity(delivery.getVideo());
      setVideo(videoResponse);
    } else {
      setVideo(null);
    }
  }

}
