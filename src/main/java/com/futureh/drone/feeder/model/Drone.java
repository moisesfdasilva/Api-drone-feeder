package com.futureh.drone.feeder.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Drone class.
 */
@Entity
@Table(name = "drone")
public class Drone {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String model;
  private Float capacityWeightInKg;

  @OneToMany
  @JoinColumn(name = "video_id")
  private List<Video> videos;

  /**
   * Drone constructor method.
   */
  public Drone(String name, String model, Float capacityWeightInKg) {
    this.name = name;
    this.model = model;
    this.capacityWeightInKg = capacityWeightInKg;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public Float getCapacityWeightInKg() {
    return capacityWeightInKg;
  }

  public void setCapacityWeightInKg(Float capacityWeightInKg) {
    this.capacityWeightInKg = capacityWeightInKg;
  }

  public List<Video> getVideos() {
    return videos;
  }

  public void setVideos(List<Video> videos) {
    this.videos = videos;
  }

  public void addVideo(Video video) {
    this.videos.add(video);
  }

}
