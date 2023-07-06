package com.futureh.drone.feeder.model;

import com.futureh.drone.feeder.util.DeliveryStatus;
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
  private String receiverName;
  private String address;
  private String zipCode;
  private String latitude;
  private String longitude;
  private DeliveryStatus status;
  private Float weightInKg;

  @OneToOne(cascade = CascadeType.ALL)
  private Video video;

  /** Delivery default constructor method. */
  public Delivery() { }

  /** Delivery constructor method. */
  public Delivery(String receiverName, String address, String zipCode, String latitude,
      String longitude, Float weightInKg) {
    this.receiverName = receiverName;
    this.address = address;
    this.zipCode = zipCode;
    this.latitude = latitude;
    this.longitude = longitude;
    this.status = DeliveryStatus.TO_DELIVER;
    this.weightInKg = weightInKg;
  }

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

  public Video getVideo() {
    return video;
  }

  public void setVideo(Video video) {
    this.video = video;
  }

}
