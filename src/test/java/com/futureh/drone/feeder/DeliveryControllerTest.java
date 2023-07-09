package com.futureh.drone.feeder;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.containsString;
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
import com.futureh.drone.feeder.exception.InputNotFoundException;
import com.futureh.drone.feeder.mock.VideoDownloadControllerMock;
import com.futureh.drone.feeder.model.Delivery;
import com.futureh.drone.feeder.model.Drone;
import com.futureh.drone.feeder.model.Video;
import com.futureh.drone.feeder.service.DeliveryService;
import com.futureh.drone.feeder.util.DeliveryStatus;
import java.io.IOException;
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
  String dlvInvalidReceiverName = "Invalid Receiver Name aaaaaaaaaaa";
  String dlvInvalidAddress = "Invalid Address aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
  String dlvInvalidZipCode = "0";
  String dlvInvalidLatitude = "01234";
  String dlvInvalidLongitude = "01234";
  Float dlvInvalidWeight = 12.01F;

  Long videoIdOk = 1L;
  String videoNameOk = "BR01-2022-05-30-101010.mp4";
  Long videoSizeOk = 8888938L;
  Long videoIdOkToo = 2L;
  String videoNameOkToo = "BR01-2022-05-29-111111.mp4";
  Long videoSizeOkToo = 7777827L;
  String vdoNameNone = "None";
  String vdoNameInvalidLength = "DRON--2022-05-30-101010.mp4";
  String vdoNameInvalidDroneName = "????-2022-05-30-101010.mp4";
  String vdoNameInvalidFormat = "DRON-2022-05-30-101010.avi";
  String vdoNameInvalidDate = "DRON-2022-30-30-101010.mp4";

  Long drnIdOk = 1L;
  String drnNameOk = "BR01";
  String drnModelOk = "Embraer XYZ 777";
  Float drnCpWeightOk = 10.5f;
  String drnNameNone = "None";

  @Test
  @Order(1)
  @DisplayName("1.1. A rota POST /delivery/new, ----> OK.")
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
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(deliveryDto))
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
  @DisplayName("1.2. A rota POST /delivery/new, ----> BAD REQUEST.")
  public void postDeliveryWithInvalidReceiverName() throws Exception {
    DeliveryDto deliveryDto = new DeliveryDto();
    deliveryDto.setReceiverName(dlvInvalidReceiverName);

    this.mockMvc.perform(post("/delivery/new")
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(deliveryDto))
    ).andExpect(status().isBadRequest())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", is("Delivery receiver name has more than 32 characters.")));
  }

  @Test
  @Order(3)
  @DisplayName("1.3. A rota POST /delivery/new, ----> BAD REQUEST.")
  public void postDeliveryWithInvalidAddress() throws Exception {
    DeliveryDto deliveryDto = new DeliveryDto();
    deliveryDto.setReceiverName(dlvReceiverNameOk);
    deliveryDto.setAddress(dlvInvalidAddress);

    this.mockMvc.perform(post("/delivery/new")
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(deliveryDto))
    ).andExpect(status().isBadRequest())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", is("Delivery address has more than 100 characters.")));
  }

  @Test
  @Order(4)
  @DisplayName("1.4. A rota POST /delivery/new, ----> BAD REQUEST.")
  public void postDeliveryWithInvalidZipCode() throws Exception {
    DeliveryDto deliveryDto = new DeliveryDto();
    deliveryDto.setReceiverName(dlvReceiverNameOk);
    deliveryDto.setAddress(dlvAddressOk);
    deliveryDto.setZipCode(dlvInvalidZipCode);

    this.mockMvc.perform(post("/delivery/new")
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(deliveryDto))
    ).andExpect(status().isBadRequest())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", is("Zip code isn't in default format (12345-123).")));
  }

  @Test
  @Order(5)
  @DisplayName("1.5. A rota POST /delivery/new, ----> BAD REQUEST.")
  public void postDeliveryWithInvalidLatitude() throws Exception {
    DeliveryDto deliveryDto = new DeliveryDto();
    deliveryDto.setReceiverName(dlvReceiverNameOk);
    deliveryDto.setAddress(dlvAddressOk);
    deliveryDto.setZipCode(dlvZipCodeOk);
    deliveryDto.setLatitude(dlvInvalidLatitude);

    this.mockMvc.perform(post("/delivery/new")
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(deliveryDto))
    ).andExpect(status().isBadRequest())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", is("Latitude isn't in the format.")));
  }

  @Test
  @Order(6)
  @DisplayName("1.6. A rota POST /delivery/new, ----> BAD REQUEST.")
  public void postDeliveryWithInvalidLongitude() throws Exception {
    DeliveryDto deliveryDto = new DeliveryDto();
    deliveryDto.setReceiverName(dlvReceiverNameOk);
    deliveryDto.setAddress(dlvAddressOk);
    deliveryDto.setZipCode(dlvZipCodeOk);
    deliveryDto.setLatitude(dlvLatitudeOk);
    deliveryDto.setLongitude(dlvInvalidLongitude);

    this.mockMvc.perform(post("/delivery/new")
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(deliveryDto))
    ).andExpect(status().isBadRequest())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", is("Longitude isn't in the format.")));
  }

  @Test
  @Order(7)
  @DisplayName("1.7. A rota POST /delivery/new, ----> BAD REQUEST.")
  public void postDeliveryWithInvalidWeight() throws Exception {
    DeliveryDto deliveryDto = new DeliveryDto();
    deliveryDto.setReceiverName(dlvReceiverNameOk);
    deliveryDto.setAddress(dlvAddressOk);
    deliveryDto.setZipCode(dlvZipCodeOk);
    deliveryDto.setLatitude(dlvLatitudeOk);
    deliveryDto.setLongitude(dlvLongitudeOk);
    deliveryDto.setWeightInKg(dlvInvalidWeight);

    this.mockMvc.perform(post("/delivery/new")
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(deliveryDto))
    ).andExpect(status().isBadRequest())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", is("The weight exceeded the limit (12Kg).")));
  }

  @Test
  @Order(8)
  @DisplayName("2.1. A rota POST /delivery/{id}/uploadVideo, ----> OK.")
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
  @Order(9)
  @DisplayName("2.2. A rota POST /delivery/{id}/uploadVideo, ----> Bad Request.")
  public void uploadWithoutVideoField() throws Exception {
    MockMultipartFile multipartFile = new MockMultipartFile("null", videoNameOk, "video.mp4",
        "New drone video".getBytes());

    this.mockMvc.perform(
        multipart("/delivery/" + dlvIdOk + "/uploadVideo").file(multipartFile)
    ).andExpect(status().isBadRequest())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", is("Required request part 'video' is not present")));
  }

  @Test
  @Order(10)
  @DisplayName("2.3. A rota POST /delivery/{id}/uploadVideo, ----> Bad Request.")
  public void uploadContainVideoNameWithInvalidLength() throws Exception {
    MockMultipartFile multipartFile = new MockMultipartFile("video", vdoNameInvalidLength,
        "video.mp4", "New drone video".getBytes());

    this.mockMvc.perform(
        multipart("/delivery/" + dlvIdOk + "/uploadVideo").file(multipartFile)
    ).andExpect(status().isBadRequest())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", containsString("The video name length isn't 26 characters.")));
  }

  @Test
  @Order(11)
  @DisplayName("2.4. A rota POST /delivery/{id}/uploadVideo, ----> Bad Request.")
  public void uploadContainVideoNameWithInvalidDroneName() throws Exception {
    MockMultipartFile multipartFile = new MockMultipartFile("video", vdoNameInvalidDroneName,
        "video.mp4", "New drone video".getBytes());

    this.mockMvc.perform(
        multipart("/delivery/" + dlvIdOk + "/uploadVideo").file(multipartFile)
    ).andExpect(status().isBadRequest())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", containsString("The default drone name isn't correct.")));
  }

  @Test
  @Order(12)
  @DisplayName("2.5. A rota POST /delivery/{id}/uploadVideo, ----> Bad Request.")
  public void uploadContainVideoNameWithInvalidFormat() throws Exception {
    MockMultipartFile multipartFile = new MockMultipartFile("video", vdoNameInvalidFormat,
        "video.mp4", "New drone video".getBytes());

    this.mockMvc.perform(
        multipart("/delivery/" + dlvIdOk + "/uploadVideo").file(multipartFile)
    ).andExpect(status().isBadRequest())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", containsString("The video format isn't correct.")));
  }

  @Test
  @Order(13)
  @DisplayName("2.6. A rota POST /delivery/{id}/uploadVideo, ----> Bad Request.")
  public void uploadContainVideoNameWithInvalidDate() throws Exception {
    MockMultipartFile multipartFile = new MockMultipartFile("video", vdoNameInvalidDate,
        "video.mp4", "New drone video".getBytes());

    this.mockMvc.perform(
        multipart("/delivery/" + dlvIdOk + "/uploadVideo").file(multipartFile)
    ).andExpect(status().isBadRequest())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", containsString("The delivery date-time isn't correct.")));
  }

  @Test
  @Order(14)
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
  @Order(15)
  @DisplayName("4.1. A rota GET /delivery/video/{id}, ----> OK.")
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
  @Order(16)
  @DisplayName("4.2. A rota GET /delivery/video/{id}, ----> NOT FOUND.")
  public void getVideoDetailsWithIdNotFound() throws Exception {
    when(deliveryService.getVideoById(videoIdOk)).thenThrow(
        new InputNotFoundException("Video id not found."));

    this.mockMvc.perform(get("/delivery/video/" + videoIdOk))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error", is("Video id not found.")));
  }

  @Test
  @Order(17)
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
  @Order(18)
  @DisplayName("6.1. A rota GET /delivery/{id}, ----> OK.")
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
  @Order(19)
  @DisplayName("6.2. A rota GET /delivery/{id}, ----> NOT FOUND.")
  public void getDeliveryByIdWithIdNotFound() throws Exception {
    when(deliveryService.getDeliveryById(dlvIdOk)).thenThrow(
        new InputNotFoundException("Delivery id not found."));

    this.mockMvc.perform(get("/delivery/" + dlvIdOk))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error", is("Delivery id not found.")));
  }

  @Test
  @Order(20)
  @DisplayName("7.1. A rota DELETE /delivery/delete/{id}, ----> OK.")
  public void removeDeliveryOk() throws Exception {
    when(deliveryService.removeDelivery(dlvIdOk)).thenReturn(dlvIdOk);

    this.mockMvc.perform(delete("/delivery/delete/" + dlvIdOk))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Delivery id " + dlvIdOk + " has been removed.")));
  }

  @Test
  @Order(21)
  @DisplayName("7.2. A rota DELETE /delivery/delete/{id}, ----> OK.")
  public void removeDeliveryWithIdNotFound() throws Exception {
    when(deliveryService.removeDelivery(dlvIdOk)).thenThrow(
        new InputNotFoundException("Delivery id not found."));

    this.mockMvc.perform(delete("/delivery/delete/" + dlvIdOk))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error", is("Delivery id not found.")));
  }

  @Test
  @Order(22)
  @DisplayName("8.1. A rota PUT /delivery/update/{id}, ----> OK.")
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
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(deliveryDto))
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
  @Order(23)
  @DisplayName("8.2. A rota PUT /delivery/update/{id}, ----> BAD REQUEST.")
  public void updateDeliveryWithInvalidReceiverName() throws Exception {
    DeliveryDto deliveryDto = new DeliveryDto();
    deliveryDto.setReceiverName(dlvInvalidReceiverName);

    this.mockMvc.perform(put("/delivery/update/" + dlvIdOk)
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(deliveryDto))
    ).andExpect(status().isBadRequest())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", is("Delivery receiver name has more than 32 characters.")));
  }

  @Test
  @Order(24)
  @DisplayName("8.3. A rota PUT /delivery/update/{id}, ----> BAD REQUEST.")
  public void updateDeliveryWithInvalidAddress() throws Exception {
    DeliveryDto deliveryDto = new DeliveryDto();
    deliveryDto.setReceiverName(dlvReceiverNameOk);
    deliveryDto.setAddress(dlvInvalidAddress);

    this.mockMvc.perform(put("/delivery/update/" + dlvIdOk)
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(deliveryDto))
    ).andExpect(status().isBadRequest())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", is("Delivery address has more than 100 characters.")));
  }

  @Test
  @Order(25)
  @DisplayName("8.4. A rota PUT /delivery/update/{id}, ----> BAD REQUEST.")
  public void updateDeliveryWithInvalidZipCode() throws Exception {
    DeliveryDto deliveryDto = new DeliveryDto();
    deliveryDto.setReceiverName(dlvReceiverNameOk);
    deliveryDto.setAddress(dlvAddressOk);
    deliveryDto.setZipCode(dlvInvalidZipCode);

    this.mockMvc.perform(put("/delivery/update/" + dlvIdOk)
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(deliveryDto))
    ).andExpect(status().isBadRequest())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", is("Zip code isn't in default format (12345-123).")));
  }

  @Test
  @Order(26)
  @DisplayName("8.5. A rota PUT /delivery/update/{id}, ----> BAD REQUEST.")
  public void updateDeliveryWithInvalidLatitude() throws Exception {
    DeliveryDto deliveryDto = new DeliveryDto();
    deliveryDto.setReceiverName(dlvReceiverNameOk);
    deliveryDto.setAddress(dlvAddressOk);
    deliveryDto.setZipCode(dlvZipCodeOk);
    deliveryDto.setLatitude(dlvInvalidLatitude);

    this.mockMvc.perform(put("/delivery/update/" + dlvIdOk)
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(deliveryDto))
    ).andExpect(status().isBadRequest())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", is("Latitude isn't in the format.")));
  }

  @Test
  @Order(27)
  @DisplayName("8.6. A rota PUT /delivery/update/{id}, ----> BAD REQUEST.")
  public void updateDeliveryWithInvalidLongitude() throws Exception {
    DeliveryDto deliveryDto = new DeliveryDto();
    deliveryDto.setReceiverName(dlvReceiverNameOk);
    deliveryDto.setAddress(dlvAddressOk);
    deliveryDto.setZipCode(dlvZipCodeOk);
    deliveryDto.setLatitude(dlvLatitudeOk);
    deliveryDto.setLongitude(dlvInvalidLongitude);

    this.mockMvc.perform(put("/delivery/update/" + dlvIdOk)
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(deliveryDto))
    ).andExpect(status().isBadRequest())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", is("Longitude isn't in the format.")));
  }

  @Test
  @Order(28)
  @DisplayName("8.7. A rota PUT /delivery/update/{id}, ----> BAD REQUEST.")
  public void updateDeliveryWithInvalidWeight() throws Exception {
    DeliveryDto deliveryDto = new DeliveryDto();
    deliveryDto.setReceiverName(dlvReceiverNameOk);
    deliveryDto.setAddress(dlvAddressOk);
    deliveryDto.setZipCode(dlvZipCodeOk);
    deliveryDto.setLatitude(dlvLatitudeOk);
    deliveryDto.setLongitude(dlvLongitudeOk);
    deliveryDto.setWeightInKg(dlvInvalidWeight);

    this.mockMvc.perform(put("/delivery/update/" + dlvIdOk)
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(deliveryDto))
    ).andExpect(status().isBadRequest())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", is("The weight exceeded the limit (12Kg).")));
  }

  @Test
  @Order(29)
  @DisplayName("8.8. A rota PUT /delivery/update/{id}, ----> NOT FOUND.")
  public void updateDeliveryWithIdNotFound() throws Exception {
    DeliveryDto deliveryDto = new DeliveryDto();
    deliveryDto.setReceiverName(dlvReceiverNameOk);
    deliveryDto.setAddress(dlvAddressOk);
    deliveryDto.setZipCode(dlvZipCodeOk);
    deliveryDto.setLatitude(dlvLatitudeOk);
    deliveryDto.setLongitude(dlvLongitudeOk);
    deliveryDto.setWeightInKg(dlvWeightInKgOk);
    
    when(deliveryService.updateDelivery(any(Long.class), any(DeliveryDto.class))).thenThrow(
        new InputNotFoundException("Delivery id not found."));

    this.mockMvc.perform(put("/delivery/update/" + dlvIdOk)
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(deliveryDto))
    ).andExpect(status().isNotFound())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", is("Delivery id not found.")));
  }

  @Test
  @Order(30)
  @DisplayName("9.1. A rota GET /delivery/{id}/downloadVideo, ----> OK.")
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
  @Order(31)
  @DisplayName("9.2. A rota GET /delivery/{id}/downloadVideo, ----> NOT FOUND.")
  public void downloadNotExistentVideo() throws Exception {
    Delivery delivery = new Delivery(dlvReceiverNameOk, dlvAddressOk, dlvZipCodeOk, dlvLatitudeOk,
        dlvLongitudeOk, dlvWeightInKgOk);
    delivery.setId(dlvIdOk);

    when(deliveryService.getDeliveryById(dlvIdOk)).thenReturn(delivery);

    this.mockMvc.perform(
        get("/delivery/" + dlvIdOk + "/downloadVideo")
    ).andExpect(status().isNotFound())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", is("The delivery hasn't video.")));
  }

  @Test
  @Order(32)
  @DisplayName("9.3. A rota GET /delivery/{id}/downloadVideo, ----> InternalServerError.")
  public void downloadInternalServerErrorA() throws Exception {
    Delivery delivery = new Delivery(dlvReceiverNameOk, dlvAddressOk, dlvZipCodeOk, dlvLatitudeOk,
        dlvLongitudeOk, dlvWeightInKgOk);
    delivery.setId(dlvIdOk);
    Video video = new Video(videoNameOk, videoSizeOk);
    video.setId(videoIdOk);
    delivery.setVideo(video);
    when(deliveryService.getDeliveryById(dlvIdOk)).thenReturn(delivery);
    when(deliveryService.getVideoAsResource(videoNameOk)).thenReturn(null);

    this.mockMvc.perform(
        get("/delivery/" + dlvIdOk + "/downloadVideo")
    ).andExpect(status().isInternalServerError())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", is("Internal server error in video finding.")));
  }

  @Test
  @Order(33)
  @DisplayName("9.4. A rota GET /delivery/{id}/downloadVideo, ----> InternalServerError.")
  public void downloadInternalServerErrorB() throws Exception {
    Delivery delivery = new Delivery(dlvReceiverNameOk, dlvAddressOk, dlvZipCodeOk, dlvLatitudeOk,
        dlvLongitudeOk, dlvWeightInKgOk);
    delivery.setId(dlvIdOk);
    Video video = new Video(videoNameOk, videoSizeOk);
    video.setId(videoIdOk);
    delivery.setVideo(video);
    when(deliveryService.getDeliveryById(dlvIdOk)).thenReturn(delivery);
    when(deliveryService.getVideoAsResource(videoNameOk)).thenThrow(new IOException());

    this.mockMvc.perform(
        get("/delivery/" + dlvIdOk + "/downloadVideo")
    ).andExpect(status().isInternalServerError())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", is("Internal server error in video finding.")));
  }
  
  @Test
  @Order(34)
  @DisplayName("10.1. A rota DELETE /delivery/{id}/deleteVideo, ----> OK.")
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

  @Test
  @Order(35)
  @DisplayName("10.2. A rota DELETE /delivery/{id}/deleteVideo, ----> NOT FOUND.")
  public void deleteVideoVideoNotFound() throws Exception {
    Delivery delivery = new Delivery(dlvReceiverNameOk, dlvAddressOk, dlvZipCodeOk, dlvLatitudeOk,
        dlvLongitudeOk, dlvWeightInKgOk);
    delivery.setId(dlvIdOk);
    when(deliveryService.getDeliveryById(dlvIdOk)).thenReturn(delivery);

    this.mockMvc.perform(delete("/delivery/" + dlvIdOk + "/deleteVideo"))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error", is("The delivery hasn't video.")));
  }

  static String asJsonString(Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
