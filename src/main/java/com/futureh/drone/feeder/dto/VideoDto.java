package com.futureh.drone.feeder.dto;

/**
 * VideoDto class.
 */
public class VideoDto {

  private String videoName;
  private Long deliveryId;

  public String getVideoName() {
    return videoName;
  }

  public void setVideoName(String videoName) {
    this.videoName = videoName;
  }

  public Long getDeliveryId() {
    return deliveryId;
  }

  public void setDeliveryId(Long deliveryId) {
    this.deliveryId = deliveryId;
  }

}
