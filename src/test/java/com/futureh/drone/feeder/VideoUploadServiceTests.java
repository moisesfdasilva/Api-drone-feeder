package com.futureh.drone.feeder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.futureh.drone.feeder.service.VideoUploadService;
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
class VideoUploadServiceTests {

  @InjectMocks
  private VideoUploadService videoUploadService;

  @Test
  @DisplayName("1. Salva o vídeo enviado no diretório videos-upload.")
  public void uploadWithVideoOk() throws Exception {
    String fileName = "DRON-yyyy-MM-dd-HHmmss.mp4";
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

}
