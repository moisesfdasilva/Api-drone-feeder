package com.futureh.drone.feeder;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.futureh.drone.feeder.dto.DeliveryDto;
import com.futureh.drone.feeder.mock.VideoDownloadControllerMock;
import com.futureh.drone.feeder.model.Delivery;
import com.futureh.drone.feeder.model.Drone;
import com.futureh.drone.feeder.model.Video;
import com.futureh.drone.feeder.service.DeliveryService;
import com.futureh.drone.feeder.util.DeliveryStatus;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class DeliveryControllerTest {

  @MockBean
  private DeliveryService deliveryService;

  @Autowired
  private MockMvc mockMvc;

  Long dlvIdOk = 1L;
  String dlvReceiverNameOk = "Alberto Santos Dumont";
  String dlvAddressOk = "Avenida Ayrton Senna, 2541 - Barra da Tijuca, Rio de Janeiro - RJ";
  String dlvZipCodeOk = "22775-002";
  String dlvLatitudeOk = "-22.987029";
  String dlvLongitudeOk = "-43.366164";
  Float dlvWeightInKgOk = 4.3F;
  Long dlvIdOkToo = 2L;
  String dlvReceiverNameOkToo = "Joaquim Maria Machado de Assis";
  String dlvAddressOkToo = "Praça Senador Salgado Filho, s/n - Centro, Rio de Janeiro - RJ";
  String dlvZipCodeOkToo = "20021-340";
  String dlvLatitudeOkToo = "-22.910948";
  String dlvLongitudeOkToo = "-43.167084";
  Float dlvWeightInKgOkToo = 6.9F;

  Long videoIdOk = 1L;
  String videoNameOk = "BR01-2022-05-30-101010.mp4";
  Long videoSizeOk = 8888938L;
  Long videoIdOkToo = 2L;
  String videoNameOkToo = "BR01-2022-05-29-111111.mp4";
  Long videoSizeOkToo = 7777827L;
  String vdoNameNone = "None";

  Long drnIdOk = 1L;
  String drnNameOk = "BR01";
  String drnModelOk = "Embraer XYZ 777";
  Float drnCpWeightOk = 10.5f;
  String drnNameNone = "None";

  @Test
  @Order(1)
  @DisplayName("1. A rota POST /delivery/new, ----> OK.")
  public void postDeliveryOk() throws Exception {
    DeliveryDto deliveryDto = new DeliveryDto();
    deliveryDto.setReceiverName(dlvReceiverNameOk);
    deliveryDto.setAddress(dlvAddressOk);
    deliveryDto.setZipCode(dlvZipCodeOk);
    deliveryDto.setLatitude(dlvLatitudeOk);
    deliveryDto.setLongitude(dlvLongitudeOk);
    deliveryDto.setWeightInKg(dlvWeightInKgOk);

    Delivery delivery = new Delivery(deliveryDto.getReceiverName(), deliveryDto.getAddress(),
        deliveryDto.getZipCode(), deliveryDto.getLatitude(), deliveryDto.getLongitude(),
        deliveryDto.getWeightInKg());
    delivery.setId(dlvIdOk);

    when(deliveryService.addDelivery(any(DeliveryDto.class))).thenReturn(delivery);

    this.mockMvc.perform(post("/delivery/new")
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(delivery))
    ).andExpect(status().isCreated())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.id", is(delivery.getId().intValue())))
    .andExpect(jsonPath("$.receiverName", is(delivery.getReceiverName())))
    .andExpect(jsonPath("$.address", is(delivery.getAddress())))
    .andExpect(jsonPath("$.zipCode", is(delivery.getZipCode())))
    .andExpect(jsonPath("$.latitude", is(delivery.getLatitude())))
    .andExpect(jsonPath("$.longitude", is(delivery.getLongitude())))
    .andExpect(jsonPath("$.status", is(delivery.getStatus().toString())))
    .andExpect(jsonPath("$.weightInKg", is(closeTo(delivery.getWeightInKg(), 0.0001))));
  }

  @Test
  @Order(2)
  @DisplayName("2. A rota POST /delivery/{id}/uploadVideo, ----> OK.")
  public void uploadVideoOk() throws Exception {
    when(deliveryService.getVideoByName(videoNameOk)).thenReturn(null);

    MockMultipartFile multipartFile = new MockMultipartFile("video", videoNameOk, "video.mp4",
        "New drone video".getBytes());
    doNothing().when(deliveryService).saveFile(videoNameOk, multipartFile);

    Delivery delivery = new Delivery(dlvReceiverNameOk, dlvAddressOk, dlvZipCodeOk, dlvLatitudeOk,
        dlvLongitudeOk, dlvWeightInKgOk);
    delivery.setId(dlvIdOk);
    Video video = new Video(videoNameOk, videoSizeOk);
    video.setId(videoIdOk);
    delivery.setVideo(video);

    when(deliveryService.addVideo(any(Long.class), any(Video.class))).thenReturn(delivery);

    this.mockMvc.perform(
        multipart("/delivery/" + dlvIdOk + "/uploadVideo").file(multipartFile)
    ).andExpect(status().isCreated())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.id", is(delivery.getId().intValue())))
    .andExpect(jsonPath("$.receiverName", is(delivery.getReceiverName())))
    .andExpect(jsonPath("$.address", is(delivery.getAddress())))
    .andExpect(jsonPath("$.zipCode", is(delivery.getZipCode())))
    .andExpect(jsonPath("$.latitude", is(delivery.getLatitude())))
    .andExpect(jsonPath("$.longitude", is(delivery.getLongitude())))
    .andExpect(jsonPath("$.status", is(delivery.getStatus().toString())))
    .andExpect(jsonPath("$.weightInKg", is(closeTo(delivery.getWeightInKg(), 0.0001))))
    .andExpect(jsonPath("$.videoName", is(delivery.getVideo().getFileName())));
  }

  @Test
  @Order(3)
  @DisplayName("3. A rota GET /delivery/allVideos, ----> OK.")
  public void getAllVideos() throws Exception {
    Video videoA = new Video(videoNameOk, videoSizeOk);
    videoA.setId(videoIdOk);
    Video videoB = new Video(videoNameOkToo, videoSizeOkToo);
    videoB.setId(videoIdOkToo);
    List<Video> allVideos = new ArrayList<Video>();
    allVideos.add(videoA);
    allVideos.add(videoB);

    when(deliveryService.getAllVideos()).thenReturn(allVideos);

    this.mockMvc.perform(get("/delivery/allVideos"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$.[*]id", containsInRelativeOrder(videoA.getId().intValue(),
            videoB.getId().intValue())))
        .andExpect(jsonPath("$.[*]fileName", containsInRelativeOrder(videoA.getFileName(),
            videoB.getFileName())))
        .andExpect(jsonPath("$.[*]size", containsInRelativeOrder(videoA.getSize().intValue(),
            videoB.getSize().intValue())))
        .andExpect(jsonPath("$.[*]droneName", containsInRelativeOrder(drnNameNone, drnNameNone)));
  }

  @Test
  @Order(4)
  @DisplayName("4. A rota GET /delivery/video/{id}, ----> OK.")
  public void getVideoDetailsOk() throws Exception {
    Video video = new Video(videoNameOk, videoSizeOk);
    video.setId(videoIdOk);
    Drone drone = new Drone(drnNameOk, drnModelOk, drnCpWeightOk);
    drone.setId(drnIdOk);
    video.setDrone(drone);

    when(deliveryService.getVideoById(videoIdOk)).thenReturn(video);

    this.mockMvc.perform(get("/delivery/video/" + videoIdOk))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(video.getId().intValue())))
        .andExpect(jsonPath("$.fileName", is(video.getFileName())))
        .andExpect(jsonPath("$.size", is(video.getSize().intValue())))
        .andExpect(jsonPath("$.drone.id", is(video.getDrone().getId().intValue())))
        .andExpect(jsonPath("$.drone.name", is(video.getDrone().getName())))
        .andExpect(jsonPath("$.drone.model", is(video.getDrone().getModel())))
        .andExpect(jsonPath("$.drone.capacityWeightInKg", is(
            closeTo(video.getDrone().getCapacityWeightInKg(), 0.0001))));
  }

  @Test
  @Order(5)
  @DisplayName("5. A rota GET /delivery/all, ----> OK.")
  public void getAllDeliveries() throws Exception {
    Delivery deliveryA = new Delivery(dlvReceiverNameOk, dlvAddressOk, dlvZipCodeOk, dlvLatitudeOk,
        dlvLongitudeOk, dlvWeightInKgOk);
    deliveryA.setId(dlvIdOk);
    Delivery deliveryB = new Delivery(dlvReceiverNameOkToo, dlvAddressOkToo, dlvZipCodeOkToo,
        dlvLatitudeOkToo, dlvLongitudeOkToo, dlvWeightInKgOkToo);
    deliveryB.setId(dlvIdOkToo);
    List<Delivery> allDeliveries = new ArrayList<Delivery>();
    allDeliveries.add(deliveryA);
    allDeliveries.add(deliveryB);

    when(deliveryService.getAllDeliveries()).thenReturn(allDeliveries);

    this.mockMvc.perform(get("/delivery/all"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$.[*]id", containsInRelativeOrder(deliveryA.getId().intValue(),
            deliveryB.getId().intValue())))
        .andExpect(jsonPath("$.[*]receiverName", containsInRelativeOrder(deliveryA
            .getReceiverName(), deliveryB.getReceiverName())))
        .andExpect(jsonPath("$.[*]address", containsInRelativeOrder(deliveryA.getAddress(),
            deliveryB.getAddress())))
        .andExpect(jsonPath("$.[*]zipCode", containsInRelativeOrder(deliveryA.getZipCode(),
            deliveryB.getZipCode())))
        .andExpect(jsonPath("$.[*]latitude", containsInRelativeOrder(deliveryA.getLatitude(),
            deliveryB.getLatitude())))
        .andExpect(jsonPath("$.[*]longitude", containsInRelativeOrder(deliveryA.getLongitude(),
            deliveryB.getLongitude())))
        .andExpect(jsonPath("$.[*]status", containsInRelativeOrder(deliveryA.getStatus().toString(),
            deliveryB.getStatus().toString())))
        .andExpect(jsonPath("$.[*]weightInKg", containsInRelativeOrder(closeTo(deliveryA
            .getWeightInKg(), 0.0001), closeTo(deliveryB.getWeightInKg(), 0.0001))))
        .andExpect(jsonPath("$.[*]videoName", containsInRelativeOrder(vdoNameNone, vdoNameNone)));
  }

  @Test
  @Order(6)
  @DisplayName("6. A rota GET /delivery/{id}, ----> OK.")
  public void getDeliveryByIdOk() throws Exception {
    Delivery delivery = new Delivery(dlvReceiverNameOk, dlvAddressOk, dlvZipCodeOk, dlvLatitudeOk,
        dlvLongitudeOk, dlvWeightInKgOk);
    delivery.setId(dlvIdOk);
    Video video = new Video(videoNameOk, videoSizeOk);
    video.setId(videoIdOk);
    Drone drone = new Drone(drnNameOk, drnModelOk, drnCpWeightOk);
    drone.setId(drnIdOk);
    video.setDrone(drone);
    delivery.setVideo(video);
    delivery.setStatus(DeliveryStatus.DELIVERED);

    when(deliveryService.getDeliveryById(dlvIdOk)).thenReturn(delivery);

    this.mockMvc.perform(get("/delivery/" + dlvIdOk))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(delivery.getId().intValue())))
        .andExpect(jsonPath("$.receiverName", is(delivery.getReceiverName())))
        .andExpect(jsonPath("$.address", is(delivery.getAddress())))
        .andExpect(jsonPath("$.zipCode", is(delivery.getZipCode())))
        .andExpect(jsonPath("$.latitude", is(delivery.getLatitude())))
        .andExpect(jsonPath("$.longitude", is(delivery.getLongitude())))
        .andExpect(jsonPath("$.status", is(delivery.getStatus().toString())))
        .andExpect(jsonPath("$.weightInKg", is(closeTo(delivery.getWeightInKg(), 0.0001))))
        .andExpect(jsonPath("$.video.id", is(delivery.getVideo().getId().intValue())))
        .andExpect(jsonPath("$.video.fileName", is(delivery.getVideo().getFileName())))
        .andExpect(jsonPath("$.video.size", is(delivery.getVideo().getSize().intValue())))
        .andExpect(jsonPath("$.video.droneName", is(delivery.getVideo().getDrone().getName())));
  }

  @Test
  @Order(7)
  @DisplayName("7. A rota DELETE /delivery/delete/{id}, ----> OK.")
  public void removeDeliveryOk() throws Exception {
    when(deliveryService.removeDelivery(dlvIdOk)).thenReturn(dlvIdOk);

    this.mockMvc.perform(delete("/delivery/delete/" + dlvIdOk))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Delivery id " + dlvIdOk + " has been removed.")));
  }

  @Test
  @Order(8)
  @DisplayName("8. A rota PUT /delivery/update/{id}, ----> OK.")
  public void updateDeliveryOk() throws Exception {
    DeliveryDto deliveryDto = new DeliveryDto();
    deliveryDto.setReceiverName(dlvReceiverNameOkToo);
    deliveryDto.setAddress(dlvAddressOkToo);
    deliveryDto.setZipCode(dlvZipCodeOkToo);
    deliveryDto.setLatitude(dlvLatitudeOkToo);
    deliveryDto.setLongitude(dlvLongitudeOkToo);
    deliveryDto.setWeightInKg(dlvWeightInKgOkToo);

    Delivery delivery = new Delivery(deliveryDto.getReceiverName(), deliveryDto.getAddress(),
        deliveryDto.getZipCode(), deliveryDto.getLatitude(), deliveryDto.getLongitude(),
        deliveryDto.getWeightInKg());
    delivery.setId(dlvIdOk);

    when(deliveryService.updateDelivery(any(Long.class), any(DeliveryDto.class)))
        .thenReturn(delivery);

    this.mockMvc.perform(put("/delivery/update/" + dlvIdOk)
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(delivery))
    ).andExpect(status().isOk())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.id", is(delivery.getId().intValue())))
    .andExpect(jsonPath("$.receiverName", is(delivery.getReceiverName())))
    .andExpect(jsonPath("$.address", is(delivery.getAddress())))
    .andExpect(jsonPath("$.zipCode", is(delivery.getZipCode())))
    .andExpect(jsonPath("$.latitude", is(delivery.getLatitude())))
    .andExpect(jsonPath("$.longitude", is(delivery.getLongitude())))
    .andExpect(jsonPath("$.status", is(delivery.getStatus().toString())))
    .andExpect(jsonPath("$.weightInKg", is(closeTo(delivery.getWeightInKg(), 0.0001))));
  }

  @Test
  @Order(9)
  @DisplayName("9. A rota GET /delivery/{id}/downloadVideo, ----> OK.")
  public void downloadVideoOk() throws Exception {
    Delivery delivery = new Delivery(dlvReceiverNameOk, dlvAddressOk, dlvZipCodeOk, dlvLatitudeOk,
        dlvLongitudeOk, dlvWeightInKgOk);
    delivery.setId(dlvIdOk);
    Video video = new Video(videoNameOk, videoSizeOk);
    video.setId(videoIdOk);
    delivery.setVideo(video);
    when(deliveryService.getDeliveryById(dlvIdOk)).thenReturn(delivery);

    Resource videoResource = VideoDownloadControllerMock.getMockedResource(videoNameOk);
    when(deliveryService.getVideoAsResource(videoNameOk)).thenReturn(videoResource);

    this.mockMvc.perform(
        get("/delivery/" + dlvIdOk + "/downloadVideo")
    ).andExpect(status().isOk())
        .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + videoResource.getFilename() + "\""))
        .andExpect(content().bytes(videoNameOk.getBytes()));
  }
  
  @Test
  @Order(10)
  @DisplayName("10. A rota DELETE /delivery/{id}/deleteVideo, ----> OK.")
  public void deleteVideoVideoOk() throws Exception {
    Delivery deliveryA = new Delivery(dlvReceiverNameOk, dlvAddressOk, dlvZipCodeOk, dlvLatitudeOk,
        dlvLongitudeOk, dlvWeightInKgOk);
    deliveryA.setId(dlvIdOk);
    Video video = new Video(videoNameOk, videoSizeOk);
    video.setId(videoIdOk);
    deliveryA.setVideo(video);
    when(deliveryService.getDeliveryById(dlvIdOk)).thenReturn(deliveryA);

    Delivery deliveryB = new Delivery(dlvReceiverNameOk, dlvAddressOk, dlvZipCodeOk, dlvLatitudeOk,
        dlvLongitudeOk, dlvWeightInKgOk);
    deliveryB.setId(dlvIdOk);
    when(deliveryService.deleteVideo(dlvIdOk, videoNameOk)).thenReturn(deliveryB);

    this.mockMvc.perform(delete("/delivery/" + dlvIdOk + "/deleteVideo"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(deliveryA.getId().intValue())))
        .andExpect(jsonPath("$.receiverName", is(deliveryA.getReceiverName())))
        .andExpect(jsonPath("$.address", is(deliveryA.getAddress())))
        .andExpect(jsonPath("$.zipCode", is(deliveryA.getZipCode())))
        .andExpect(jsonPath("$.latitude", is(deliveryA.getLatitude())))
        .andExpect(jsonPath("$.longitude", is(deliveryA.getLongitude())))
        .andExpect(jsonPath("$.status", is(deliveryA.getStatus().toString())))
        .andExpect(jsonPath("$.weightInKg", is(closeTo(deliveryA.getWeightInKg(), 0.0001))))
        .andExpect(jsonPath("$.videoName", is(vdoNameNone)));
  }

  static String asJsonString(Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
