package com.futureh.drone.feeder.mock;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import org.springframework.core.io.Resource;

/**
 * Drone VideoDownloadControllerMock class.
 */
public class VideoDownloadControllerMock {

  /**
   * getMockedResource method.
   */
  public static Resource getMockedResource(String fileName) {
    Resource resource = new Resource() {
      @Override
      public boolean exists() {
        return false;
      }

      @Override
      public URL getURL() throws IOException {
        return null;
      }

      @Override
      public URI getURI() throws IOException {
        return null;
      }

      @Override
      public File getFile() throws IOException {
        File file = new File(fileName);
        return file;
      }

      @Override
      public long contentLength() throws IOException {
        return 0;
      }

      @Override
      public long lastModified() throws IOException {
        return 0;
      }

      @Override
      public Resource createRelative(String relativePath) throws IOException {
        return null;
      }

      @Override
      public String getFilename() {
        return "DRON-2022-05-30-101010.mp4";
      }

      @Override
      public String getDescription() {
        return null;
      }

      @Override
      public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(fileName.getBytes());
      }

    };

    return resource;
  }

}
