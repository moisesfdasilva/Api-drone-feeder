package com.futureh.drone.feeder;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.futureh.drone.feeder.service.VideoUploadService;
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
class VideoUploadControllerTests {

  @MockBean
  private VideoUploadService videoUploadService;

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("1. Deve adicionar o vídeo de entrega do drone e retornar status 200 com o body"
      + " contendo o fileName, size e downloadUri.")
  public void uploadWithVideoOk() throws Exception {
    String fileName = "DRON-2022-05-30-101010.mp4";
    String doanloadUri = "/drone/downloadVideo/" + fileName;

    MockMultipartFile multipartFile = new MockMultipartFile("video", fileName, "video.mp4",
        "New drone video".getBytes());

    when(videoUploadService.saveFile(fileName, multipartFile)).thenReturn(doanloadUri);

    this.mockMvc.perform(
        multipart("/drone/uploadVideo").file(multipartFile)
    ).andExpect(status().isOk())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.fileName", is(fileName)))
    .andExpect(jsonPath("$.size", is(15)))
    .andExpect(jsonPath("$.downloadUri", containsString(doanloadUri)));
  }

  @Test
  @DisplayName("2. Deve retornar status 400 se não houver form-data vídeo no body da requisição.")
  public void uploadWithoutVideo() throws Exception {
    MockMultipartFile multipartFile = new MockMultipartFile("not video", "someone file",
        "video.mp4", "new drone video".getBytes());

    this.mockMvc.perform(
        multipart("/drone/uploadVideo").file(multipartFile)
    ).andExpect(status().isBadRequest())
    .andExpect(jsonPath("$.error", is("Required request part 'video' is not present")));
  }

  @Test
  @DisplayName("3. Deve retornar status 400 se ...")
  public void uploadContainVideoNameWithInvalidLength() throws Exception {
    String fileName = "DRON--2022-05-30-101010.mp4";

    MockMultipartFile multipartFile = new MockMultipartFile("video", fileName, "video.mp4",
        "New drone video".getBytes());

    this.mockMvc.perform(
        multipart("/drone/uploadVideo").file(multipartFile)
    ).andExpect(status().isBadRequest())
    .andExpect(jsonPath("$.error", containsString("The video name length isn't 26 characters.")));
  }

  @Test
  @DisplayName("4. Deve retornar status 400 se ...")
  public void uploadContainVideoNameWithInvalidDroneName() throws Exception {
    String fileName = "????-2022-05-30-101010.mp4";

    MockMultipartFile multipartFile = new MockMultipartFile("video", fileName, "video.mp4",
        "New drone video".getBytes());

    this.mockMvc.perform(
        multipart("/drone/uploadVideo").file(multipartFile)
    ).andExpect(status().isBadRequest())
    .andExpect(jsonPath("$.error", containsString("The default drone name isn't correct.")));
  }

  @Test
  @DisplayName("5. Deve retornar status 400 se ...")
  public void uploadContainVideoNameWithInvalidFormat() throws Exception {
    String fileName = "DRON-2022-05-30-101010.avi";

    MockMultipartFile multipartFile = new MockMultipartFile("video", fileName, "video.mp4",
        "New drone video".getBytes());

    this.mockMvc.perform(
        multipart("/drone/uploadVideo").file(multipartFile)
    ).andExpect(status().isBadRequest())
    .andExpect(jsonPath("$.error", containsString("The video format isn't correct.")));
  }

  @Test
  @DisplayName("6. Deve retornar status 400 se ...")
  public void uploadContainVideoNameWithInvalidDate() throws Exception {
    String fileName = "DRON-2022-30-30-101010.mp4";

    MockMultipartFile multipartFile = new MockMultipartFile("video", fileName, "video.mp4",
        "New drone video".getBytes());

    this.mockMvc.perform(
        multipart("/drone/uploadVideo").file(multipartFile)
    ).andExpect(status().isBadRequest())
    .andExpect(jsonPath("$.error", containsString("The delivery date-time isn't correct.")));
  }

}
