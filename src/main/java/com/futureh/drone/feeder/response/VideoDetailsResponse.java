package com.futureh.drone.feeder.response;

import com.futureh.drone.feeder.model.Video;

/**
 * VideoDetailsResponse class.
 */
public class VideoDetailsResponse {

  private Long id;
  private String fileName;
  private Long size;
  private DroneResponse drone;

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

  public DroneResponse getDrone() {
    return drone;
  }

  public void setDrone(DroneResponse drone) {
    this.drone = drone;
  }

  /** createResponseByVideoEntity method.*/
  public void createResponseByVideoEntity(Video video) {
    setId(video.getId());
    setFileName(video.getFileName());
    setSize(video.getSize());

    DroneResponse droneResponse = new DroneResponse();
    if (video.getDrone() != null) {
      droneResponse.createResponseByDroneEntity(video.getDrone());
      setDrone(droneResponse);     
    } else {
      setDrone(droneResponse);
    }
  }

}
