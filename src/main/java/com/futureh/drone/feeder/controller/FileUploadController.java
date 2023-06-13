package com.futureh.drone.feeder.controller;

import com.futureh.drone.feeder.response.FileUploadResponse;
import com.futureh.drone.feeder.util.FileUploadUtil;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Drone FileUploadController class.
 */
@RestController
@RequestMapping("/drone")
public class FileUploadController {

  /**
   * UploadFile method.
   */
  @PostMapping("/uploadfile")
  public ResponseEntity<FileUploadResponse> uploadFile(
      @RequestParam("video") MultipartFile multipartFile) throws IOException {

    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
    Long size = multipartFile.getSize();

    String completeFileName = FileUploadUtil.saveFile(fileName, multipartFile);

    FileUploadResponse response = new FileUploadResponse();
    response.setFileName(fileName);
    response.setSize(size);
    response.setDownloadUri("/drone/downloadfile/" + completeFileName);

    return ResponseEntity.ok(response);
  }

}
