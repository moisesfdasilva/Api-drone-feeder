package com.futureh.drone.feeder.controller;

import com.futureh.drone.feeder.exception.InputNotFoundException;
import com.futureh.drone.feeder.exception.IntServerErrorInVideoFinding;
import com.futureh.drone.feeder.service.VideoDownloadService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Drone VideoDownloadController class.
 */
@RestController
@RequestMapping("/drone")
public class VideoDownloadController {

  @Autowired
  private VideoDownloadService videoDownloadService;

  /**
   * DownloadVideo method.
   */
  @GetMapping("/downloadVideo/{videoName}")
  public ResponseEntity<?> downloadVideo(@PathVariable("videoName") String videoName) {
    Resource resource = null;
    try {
      resource = videoDownloadService.getVideoAsResource(videoName);
    } catch (IOException e) {
      throw new IntServerErrorInVideoFinding();
    }

    if (resource == null) {
      throw new InputNotFoundException("Video not found."
          + " Try to use the standard video name (DRON-yyyy-MM-dd-HHmmss.mp4).");
    }

    String contentType = "application/octet-stream";
    String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(contentType))
        .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
        .body(resource);
  }

}
