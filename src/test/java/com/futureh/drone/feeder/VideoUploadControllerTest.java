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
class VideoUploadControllerTest {

  @MockBean
  private VideoUploadService videoUploadService;

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("1. Verifica quando inserido o vídeo com nome no padrão DRON-yyyy-MM-dd-HHmmss.mp4,"
      + " deve adicionar o vídeo de entrega do drone e retornar status 200 com o body contendo o "
      + "fileName, size e downloadUri.")
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
  @DisplayName("2. Verifica se houver vídeo no body da requisição deve retornar status 400 e a "
      + "mensagem que o não há video.")
  public void uploadWithoutVideo() throws Exception {
    MockMultipartFile multipartFile = new MockMultipartFile("not video", "someone file",
        "video.mp4", "new drone video".getBytes());

    this.mockMvc.perform(
        multipart("/drone/uploadVideo").file(multipartFile)
    ).andExpect(status().isBadRequest())
    .andExpect(jsonPath("$.error", is("Required request part 'video' is not present")));
  }

  @Test
  @DisplayName("3. Verifica se tamanho do nome do vídeo é diferente de 26 caracteres, deve retornar"
      + " status 400 e a mensagem que o tamanho do texto não está conforme o esperado.")
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
  @DisplayName("4. Verifica se o nome do drone não está conforme o padrão, deve retornar status 400"
      + " e a mensagem que o nome está fora do padrão.")
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
  @DisplayName("5. Verifica se o formato do video não é .mp4, deve retornar status 400 e a mensagem"
      + " formato do vídeo está fora do padrão.")
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
  @DisplayName("6. Verifica se a data e hora não está no formato yyyy-MM-dd-HHmmss, deve retornar "
      + "status 400 e a mensagem formato data-hora está fora do padrão.")
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
