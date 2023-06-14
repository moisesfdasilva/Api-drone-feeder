package com.futureh.drone.feeder.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * FileUploadUtil class.
 */
@Service
public class FileUploadService {

  /**
   * SaveFile method.
   */
  public String saveFile(String fileName, MultipartFile multipartFile) throws IOException {
    Path uploadDirectory = Paths.get("videos-uploads");
    String fileCode = RandomStringUtils.randomAlphanumeric(5);

    try {
      InputStream inputStream = multipartFile.getInputStream();
      Path filePath = uploadDirectory
          .resolve(fileName.replace(".mp4", "") + "_" + fileCode + ".mp4");
      Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException err) {
      throw new IOException("Error: IOException.", err);
    }

    return fileCode;
  }

}
