package com.futureh.drone.feeder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.futureh.drone.feeder.service.VideoDownloadService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

@SpringBootTest
class VideoDownloadServiceTest {

  @InjectMocks
  private VideoDownloadService videoDownloadService;

  @Test
  @DisplayName("1. Verifica se é encontrado um vídeo existente no diretório videos-uploads.")
  public void downloadWithVideoOk() throws Exception {
    String videoName = "DRON-2022-10-10-101010.mp4";

    ArrayList<Path> filesMock = new ArrayList<Path>();
    Path videoMock = Paths.get("videos-uploads/" + videoName);
    filesMock.add(videoMock);

    try (MockedStatic<Files> utilities = Mockito.mockStatic(Files.class)) {
      utilities.when(() -> Files.list(Mockito.any(Path.class))).thenReturn(filesMock.stream());

      Resource videoUri = videoDownloadService.getVideoAsResource(videoName);

      assertEquals(videoUri.getFilename(), videoName);
    }
  }

  @Test
  @DisplayName("2. Verifica se retorna null quando não é encontrado um vídeo no diretório"
      + " videos-uploads.")
  public void downloadWithNotFoundVideo() throws Exception {
    String videoName = "DRON-1999-10-10-101010.mp4";

    String videoNameMock = "DRON-2022-10-10-101010.mp4";
    ArrayList<Path> filesMock = new ArrayList<Path>();
    Path videoMock = Paths.get("videos-uploads/" + videoNameMock);
    filesMock.add(videoMock);

    try (MockedStatic<Files> utilities = Mockito.mockStatic(Files.class)) {
      utilities.when(() -> Files.list(Mockito.any(Path.class))).thenReturn(filesMock.stream());

      Resource videoUri = videoDownloadService.getVideoAsResource(videoName);

      assertEquals(videoUri, null);
    }
  }

  @Test
  @DisplayName("3. Verifica se ocorre um erro IOException quando há falha ao ler o diretório"
      + " videos-uploads.")
  public void downloadWithIoError() throws Exception {
    String videoName = "DRON-2022-10-10-101010.mp4";

    try (MockedStatic<Files> utilities = Mockito.mockStatic(Files.class)) {
      utilities.when(() -> Files.list(Mockito.any(Path.class))).thenThrow(IOException.class);

      assertThrows(IOException.class,
          () -> videoDownloadService.getVideoAsResource(videoName));
    }
  }

}
