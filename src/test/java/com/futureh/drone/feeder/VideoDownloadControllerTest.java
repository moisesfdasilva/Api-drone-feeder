package com.futureh.drone.feeder;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.futureh.drone.feeder.mock.VideoDownloadControllerMock;
import com.futureh.drone.feeder.service.VideoDownloadService;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class VideoDownloadControllerTest {

  @MockBean
  private VideoDownloadService videoDownloadService;

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("1. Verifica quando inserido um vídeo com nome existente na galeria, "
      + "deve retornar o vídeo de entrega do drone com status 200.")
  public void downloadWithVideoNameOk() throws Exception {
    String fileName = "DRON-2022-05-30-101010.mp4";

    Resource videoResource = VideoDownloadControllerMock.getMockedResource(fileName);

    when(videoDownloadService.getVideoAsResource(fileName)).thenReturn(videoResource);

    this.mockMvc.perform(
        get("/drone/downloadVideo/" + fileName)
    ).andExpect(status().isOk())
        .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + videoResource.getFilename() + "\""))
        .andExpect(content().bytes(fileName.getBytes()));
  }

  @Test
  @DisplayName("2. Verifica quando inserido um vídeo com nome inexistente na galeria, "
      + "deve retornar uma mensagem de erro com o status 404.")
  public void downloadWithVideoNameNotFound() throws Exception {
    String fileName = "DRON-1989-05-30-101010.mp4";
    String errorMessage = "Video not found."
        + " Try to use the standard video name (DRON-yyyy-MM-dd-HHmmss.mp4).";

    when(videoDownloadService.getVideoAsResource(fileName)).thenReturn(null);

    this.mockMvc.perform(
        get("/drone/downloadVideo/" + fileName)
    ).andExpect(status().isNotFound())
    .andExpect(jsonPath("$.error", is(errorMessage)));
  }

  @Test
  @DisplayName("3. Verifica quando há um erro interno deve retornar uma mensagem de erro com o "
      + "status 500.")
  public void downloadWithInternalServerError() throws Exception {
    String fileName = "DRON-2022-05-30-101010.mp4";

    when(videoDownloadService.getVideoAsResource(fileName)).thenThrow(IOException.class);

    this.mockMvc.perform(
        get("/drone/downloadVideo/" + fileName)
    ).andExpect(status().isInternalServerError())
    .andExpect(jsonPath("$.error", is("Internal server error in video finding.")));
  }

}
