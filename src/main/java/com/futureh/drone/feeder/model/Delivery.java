package com.futureh.drone.feeder.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Delivery class.
 */
@Entity
public class Delivery {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String address;
  private String longitude;
  private String latitude;
  private String status;
  private Video video;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAdress() {
    return address;
  }

  public void setAdress(String address) {
    this.address = address;
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

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Video getVideo() {
    return video;
  }

  public void setVideo(Video video) {
    this.video = video;
  }

}
