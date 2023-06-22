package com.futureh.drone.feeder.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

/**
 * VideoDownloadService class.
 */
@Service
public class VideoDownloadService {

  private Path foundVideo;

  /**
   * getVideoAsResource method.
   */
  public Resource getVideoAsResource(String videoName) throws IOException {
    Path uploadDirectory = Paths.get("videos-uploads");

    Stream<Path> files = Files.list(uploadDirectory);

    foundVideo = files.filter(video -> video.getFileName().toString().equals(videoName))
        .findAny().orElse(null);

    files.close();

    if (foundVideo != null) {
      return new UrlResource(foundVideo.toUri());
    } else {
      return null;
    }
  }

}
