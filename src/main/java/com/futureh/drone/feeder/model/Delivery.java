package com.futureh.drone.feeder.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Delivery class.
 */
@Entity
@Table(name = "delivery")
public class Delivery {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String address;
  private String zipCode;
  private String longitude;
  private String latitude;
  private String status;
  private Integer weightInKg;

  @OneToOne(cascade = CascadeType.ALL)
  private Video video;

  /**
   * Delivery constructor method.
   */
  public Delivery(String address, String zipCode, String longitude, String latitude,
      Integer weightInKg) {
    this.address = address;
    this.zipCode = zipCode;
    this.longitude = longitude;
    this.latitude = latitude;
    this.status = "TO DELIVER";
    this.weightInKg = weightInKg;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Integer getWeightInKg() {
    return weightInKg;
  }

  public void setWeightInKg(Integer weightInKg) {
    this.weightInKg = weightInKg;
  }

  public Video getVideo() {
    return video;
  }

  public void setVideo(Video video) {
    this.video = video;
  }

}
