package com.futureh.drone.feeder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.futureh.drone.feeder.exception.InputFileException;
import com.futureh.drone.feeder.service.VideoUploadService;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

@SpringBootTest
class VideoUploadServiceTest {

  @InjectMocks
  private VideoUploadService videoUploadService;

  @Test
  @DisplayName("1. Verifica se um vídeo válido é salvo no diretório videos-upload.")
  public void uploadWithVideoOk() throws Exception {
    String fileName = "DRON-2022-05-30-101010.mp4";
    String doanloadUri = "/drone/downloadVideo/" + fileName;

    MockMultipartFile multipartFile = new MockMultipartFile("video", fileName, "video.mp4",
        "New drone video".getBytes());

    try (MockedStatic<Files> utilities = Mockito.mockStatic(Files.class)) {
      utilities.when(() -> Files.copy(Mockito.any(InputStream.class), Mockito.any(Path.class),
          Mockito.any(CopyOption.class))).thenReturn(1L);

      String videoUri = videoUploadService.saveFile(fileName, multipartFile);

      assertEquals(videoUri, doanloadUri);
    }
  }

  @Test
  @DisplayName("2. Verifica ocorre erro quando não há nome no vídeo.")
  public void uploadWithoutVideo() throws Exception {
    String fileName = "";

    MockMultipartFile multipartFile = new MockMultipartFile("video", fileName, "video.mp4",
        "New drone video".getBytes());

    try (MockedStatic<Files> utilities = Mockito.mockStatic(Files.class)) {
      utilities.when(() -> Files.copy(Mockito.any(InputStream.class), Mockito.any(Path.class),
          Mockito.any(CopyOption.class))).thenThrow(IOException.class);
      assertThrows(InputFileException.class,
          () -> videoUploadService.saveFile(fileName, multipartFile));
    }
  }

}
