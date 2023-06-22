package com.futureh.drone.feeder.controller;

import com.futureh.drone.feeder.middleware.VideoNameMiddleware;
import com.futureh.drone.feeder.response.FileUploadResponse;
import com.futureh.drone.feeder.service.VideoUploadService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Drone VideoUploadController class.
 */
@RestController
@RequestMapping("/drone")
public class VideoUploadController {

  @Autowired
  private VideoUploadService videoUploadService;

  /**
   * UploadVideo method.
   */
  @PostMapping("/uploadVideo")
  public ResponseEntity<FileUploadResponse> uploadVideo(
      @RequestParam("video") MultipartFile multipartFile) throws IOException {

    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
    VideoNameMiddleware.isValidName(fileName);

    Long size = multipartFile.getSize();

    String videoUri = videoUploadService.saveFile(fileName, multipartFile);

    FileUploadResponse response = new FileUploadResponse();
    response.setFileName(fileName);
    response.setSize(size);
    response.setDownloadUri(videoUri);

    return ResponseEntity.ok(response);
  }

}
