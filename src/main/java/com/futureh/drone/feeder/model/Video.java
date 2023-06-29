package com.futureh.drone.feeder.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Video class.
 */
@Entity
@Table(name = "video")
public class Video {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String fileName;
  private String downloadUri;
  private Long size;

  @ManyToOne
  private Drone drone;

  /** Video default constructor method. */
  public Video() { }

  /** Video constructor method. */
  public Video(String fileName, Long size) {
    this.fileName = fileName;
    this.downloadUri = "/drone/downloadVideo/" + fileName;
    this.size = size;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getDownloadUri() {
    return downloadUri;
  }

  public void setDownloadUri(String downloadUri) {
    this.downloadUri = downloadUri;
  }

  public Long getSize() {
    return size;
  }

  public void setSize(Long size) {
    this.size = size;
  }

  public Drone getDrone() {
    return drone;
  }

  public void setDrone(Drone drone) {
    this.drone = drone;
  }

}
