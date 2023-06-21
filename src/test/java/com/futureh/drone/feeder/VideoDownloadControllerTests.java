package com.futureh.drone.feeder;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.futureh.drone.feeder.mock.VideoDownloadControllerMock;
import com.futureh.drone.feeder.service.VideoDownloadService;
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
class VideoDownloadControllerTests {

  @MockBean
  private VideoDownloadService videoDownloadService;

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("1. Verifica quando inserido o vídeo com nome no padrão DRON-yyyy-MM-dd-HHmmss.mp4, "
      + "existente na galeria, deve retornar o vídeo de entrega do drone com status 200.")
  public void uploadWithVideoOk() throws Exception {
    String fileName = "DRON-2022-05-30-101010.mp4";
    
    Resource videoResource = VideoDownloadControllerMock.getMockedResource(fileName);

    when(videoDownloadService.getVideoAsResource(fileName)).thenReturn(videoResource);

    this.mockMvc.perform(
        get("/drone/downloadVideo/DRON-2022-05-30-101010.mp4")
    ).andExpect(status().isOk())
        .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; fileName=\"" + videoResource.getFilename() + "\""))
        .andExpect(content().bytes(fileName.getBytes()));
  }
  //  VERIFICAR AS FALHAS:
  // 1. Input inválido; e
  // 2. Vídeo inexistente.

}
