package com.futureh.drone.feeder;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.futureh.drone.feeder.service.FileUploadService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UploadTests {

  @MockBean
  private FileUploadService fileUploadService;

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("1. Deve adicionar o vídeo de entrega do drone e retornar status 200 com o body"
      + " contendo o fileName, size e downloadUri.")
  void uploadWithVideoOk() throws Exception {
    
    MockMultipartFile multipartFile = new MockMultipartFile("video", "yyyy-MM-dd-HH-mm.mp4",
        "video.mp4", "New drone video".getBytes());

    this.mockMvc.perform(
        multipart("/drone/uploadfile").file(multipartFile)
    ).andExpect(status().isOk())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.fileName", is("yyyy-MM-dd-HH-mm.mp4")))
    .andExpect(jsonPath("$.size", is(15)))
    .andExpect(jsonPath("$.downloadUri", containsString("/drone/downloadfile/")));
  }

  @Test
  @DisplayName("2. Deve retornar status 400 se não houver video na requisição.")
  void uploadWithoutVideo() throws Exception {
    MockMultipartFile multipartFile = new MockMultipartFile("not video", "someone file",
        "video.mp4", "new drone video".getBytes());

    this.mockMvc.perform(
        multipart("/drone/uploadfile").file(multipartFile)
    ).andExpect(status().isBadRequest());
  }

}
