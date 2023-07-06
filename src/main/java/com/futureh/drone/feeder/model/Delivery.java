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
  private String longitude;
  private String latitude;
  private DeliveryStatus status;
  private Float weightInKg;

  @OneToOne(cascade = CascadeType.ALL)
  private Video video;

  /** Delivery default constructor method. */
  public Delivery() { }

  /** Delivery constructor method. */
  public Delivery(String address, String receiverName, String zipCode, String longitude,
      String latitude, Float weightInKg) {
    this.receiverName = receiverName;
    this.address = address;
    this.zipCode = zipCode;
    this.longitude = longitude;
    this.latitude = latitude;
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

  public Video getVideo() {
    return video;
  }

  public void setVideo(Video video) {
    this.video = video;
  }

}
