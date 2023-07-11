package com.futureh.drone.feeder.response;

import com.futureh.drone.feeder.model.Video;

/**
 * VideoResponse class.
 */
public class VideoResponse {

  private Long id;
  private String fileName;
  private Long size;
  private String droneName;

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

  public Long getSize() {
    return size;
  }

  public void setSize(Long size) {
    this.size = size;
  }

  public String getDroneName() {
    return droneName;
  }

  public void setDroneName(String droneName) {
    this.droneName = droneName;
  }

  /** createResponseByVideoEntity method.*/
  public void createResponseByVideoEntity(Video video) {
    setId(video.getId());
    setFileName(video.getFileName());
    setSize(video.getSize());

    if (video.getDrone() != null) {
      setDroneName(video.getDrone().getName());      
    } else {
      setDroneName("None");
    }
  }

}
