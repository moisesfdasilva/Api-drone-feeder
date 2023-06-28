package com.futureh.drone.feeder.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Video class.
 */
@Entity
public class Video {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String fileName;
  private String downloadUri;
  private Long size;

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

}
