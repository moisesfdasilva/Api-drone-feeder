package com.futureh.drone.feeder;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.futureh.drone.feeder.dto.DeliveryDto;
import com.futureh.drone.feeder.exception.InputNotFoundException;
import com.futureh.drone.feeder.exception.IntServerErrorInVideoFinding;
import com.futureh.drone.feeder.exception.WrongInputDataException;
import com.futureh.drone.feeder.model.Delivery;
import com.futureh.drone.feeder.model.Drone;
import com.futureh.drone.feeder.model.Video;
import com.futureh.drone.feeder.repository.DeliveryRepository;
import com.futureh.drone.feeder.repository.VideoRepository;
import com.futureh.drone.feeder.service.DeliveryService;
import com.futureh.drone.feeder.service.DroneService;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class DeliveryServiceTest {

  @InjectMocks
  private DeliveryService deliveryService;

  @Mock
  private DeliveryRepository deliveryRepository;

  @Mock
  private VideoRepository videoRepository;

  @Mock
  private DroneService droneService;

  private Long dlvIdOk = 1L;
  private String dlvReceiverNameOk = "Alberto Santos Dumont";
  private String dlvAddressOk = "Avenida Ayrton Senna, 2541 - Barra da Tijuca, Rio de Janeiro - RJ";
  private String dlvZipCodeOk = "22775-002";
  private String dlvLatitudeOk = "-22.987029";
  private String dlvLongitudeOk = "-43.366164";
  private Float dlvWeightInKgOk = 4.3F;
  private Long dlvIdOkToo = 2L;
  private String dlvReceiverNameOkToo = "Joaquim Maria Machado de Assis";
  private String dlvAddressOkToo = "Praça Senador Salgado Filho, s/n - Centro, Rio de Janeiro - RJ";
  private String dlvZipCodeOkToo = "20021-340";
  private String dlvLatitudeOkToo = "-22.910948";
  private String dlvLongitudeOkToo = "-43.167084";
  private Float dlvWeightInKgOkToo = 6.9F;

  private Long videoIdOk = 1L;
  private String videoNameOk = "BR01-2022-05-30-101010.mp4";
  private Long videoSizeOk = 8888938L;
  private Long videoIdOkToo = 2L;
  private String videoNameOkToo = "BR01-2022-05-29-111111.mp4";
  private Long videoSizeOkToo = 7777827L;
  private String videoNameEmpty = "";

  private Long drnIdOk = 1L;
  private String drnNameOk = "BR01";
  private String drnModelOk = "Embraer XYZ 777";
  private Float drnCpWeightOk = 10.5f;

  private Long notExistentId = 999L;
  private String notExistentVideoName = "DRON-1999-10-10-101010.mp4";

  @Test
  @Order(1)
  @DisplayName("1. Verifica se o método addDelivery retorna uma instância da Classe Delivery"
      + " adicionada ao banco de dados.")
  public void addDeliveryOk() throws Exception {
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

    when(deliveryRepository.save(any(Delivery.class))).thenReturn(delivery);

    Delivery newDelivery = deliveryService.addDelivery(deliveryDto);

    assertEquals(newDelivery.getId(), delivery.getId());
    assertEquals(newDelivery.getReceiverName(), delivery.getReceiverName());
    assertEquals(newDelivery.getAddress(), delivery.getAddress());
    assertEquals(newDelivery.getZipCode(), delivery.getZipCode());
    assertEquals(newDelivery.getLatitude(), delivery.getLatitude());
    assertEquals(newDelivery.getLongitude(), delivery.getLongitude());
    assertEquals(newDelivery.getWeightInKg(), delivery.getWeightInKg());
  }

  @Test
  @Order(2)
  @DisplayName("2. Verifica se o método getVideoByName --------------------> Ok.")
  public void getVideoByNameOk() throws Exception {
    Video videoA = new Video(videoNameOk, videoSizeOk);
    videoA.setId(videoIdOk);
    Drone drone = new Drone(drnNameOk, drnModelOk, drnCpWeightOk);
    drone.setId(drnIdOk);
    videoA.setDrone(drone);
    Video videoB = new Video(videoNameOkToo, videoSizeOkToo);
    videoB.setId(videoIdOkToo);
    List<Video> allVideos = new ArrayList<Video>();
    allVideos.add(videoA);
    allVideos.add(videoB);

    when(videoRepository.findAll()).thenReturn(allVideos);

    Video videoFound = deliveryService.getVideoByName(videoNameOk);

    assertEquals(videoFound.getId(), videoA.getId());
    assertEquals(videoFound.getFileName(), videoA.getFileName());
    assertEquals(videoFound.getSize(), videoA.getSize());
    assertEquals(videoFound.getDrone().getId(), videoA.getDrone().getId());
  }

  @Test
  @Order(3)
  @DisplayName("3.1. saveFile --------------------> Ok.")
  public void saveFileOk() throws Exception {
    MockMultipartFile multipartFile = new MockMultipartFile("video", videoNameOk, "video.mp4",
        "New drone video".getBytes());

    try (MockedStatic<Files> utilities = Mockito.mockStatic(Files.class)) {
      utilities.when(() -> Files.copy(Mockito.any(InputStream.class), Mockito.any(Path.class),
          Mockito.any(CopyOption.class))).thenReturn(videoIdOk);
      assertDoesNotThrow(() -> deliveryService.saveFile(videoNameOk, multipartFile));
    }
  }

  @Test
  @Order(4)
  @DisplayName("3.2. saveFile --------------------> IOException.")
  public void saveFileWithVideoNameEmpty() throws Exception {
    MockMultipartFile multipartFile = new MockMultipartFile("video", videoNameEmpty, "video.mp4",
        "New drone video".getBytes());

    try (MockedStatic<Files> utilities = Mockito.mockStatic(Files.class)) {
      utilities.when(() -> Files.copy(Mockito.any(InputStream.class), Mockito.any(Path.class),
          Mockito.any(CopyOption.class))).thenThrow(IOException.class);
      assertThrows(WrongInputDataException.class,
          () -> deliveryService.saveFile(videoNameEmpty, multipartFile));
    }
  }

  @Test
  @Order(5)
  @DisplayName("4. addVideo --------------------> Ok.")
  public void addVideoOk() throws Exception {
    Drone drone = new Drone(drnNameOk, drnModelOk, drnCpWeightOk);
    drone.setId(drnIdOk);
    when(droneService.getDroneByName(any(String.class))).thenReturn(drone);

    Delivery delivery = new Delivery(dlvReceiverNameOk, dlvAddressOk, dlvZipCodeOk, 
        dlvLatitudeOk, dlvLongitudeOk, dlvWeightInKgOk);
    delivery.setId(dlvIdOk);
    when(deliveryRepository.findById(dlvIdOk)).thenReturn(Optional.of(delivery));

    when(deliveryRepository.save(delivery)).thenReturn(delivery);
    
    Video video = new Video(videoNameOk, videoSizeOk);
    video.setId(videoIdOk);
    Delivery deliveryUpdate = deliveryService.addVideo(dlvIdOk, video);

    assertEquals(deliveryUpdate.getId(), delivery.getId());
    assertEquals(deliveryUpdate.getReceiverName(), delivery.getReceiverName());
    assertEquals(deliveryUpdate.getAddress(), delivery.getAddress());
    assertEquals(deliveryUpdate.getZipCode(), delivery.getZipCode());
    assertEquals(deliveryUpdate.getLatitude(), delivery.getLatitude());
    assertEquals(deliveryUpdate.getLongitude(), delivery.getLongitude());
    assertEquals(deliveryUpdate.getWeightInKg(), delivery.getWeightInKg());
    assertEquals(deliveryUpdate.getVideo().getId(), delivery.getVideo().getId());
    assertEquals(deliveryUpdate.getVideo().getFileName(), delivery.getVideo().getFileName());
    assertEquals(deliveryUpdate.getVideo().getSize(), delivery.getVideo().getSize());
    assertEquals(deliveryUpdate.getVideo().getDrone().getId(), 
        delivery.getVideo().getDrone().getId());
    assertEquals(deliveryUpdate.getVideo().getDrone().getName(), 
        delivery.getVideo().getDrone().getName());
    assertEquals(deliveryUpdate.getVideo().getDrone().getModel(), 
        delivery.getVideo().getDrone().getModel());
  }

  @Test
  @Order(6)
  @DisplayName("5. getAllVideos --------------------> Ok.")
  public void getAllVideosOk() throws Exception {
    Video videoA = new Video(videoNameOk, videoSizeOk);
    videoA.setId(videoIdOk);
    Video videoB = new Video(videoNameOkToo, videoSizeOkToo);
    videoB.setId(videoIdOkToo);
    List<Video> allVideos = new ArrayList<Video>();
    allVideos.add(videoA);
    allVideos.add(videoB);

    when(videoRepository.findAll()).thenReturn(allVideos);

    List<Video> videos = deliveryService.getAllVideos();

    assertEquals(videos.size(), allVideos.size());
    assertEquals(videos.get(0).getId(), videoA.getId());
    assertEquals(videos.get(0).getFileName(), videoA.getFileName());
    assertEquals(videos.get(0).getSize(), videoA.getSize());
    assertEquals(videos.get(1).getId(), videoB.getId());
    assertEquals(videos.get(1).getFileName(), videoB.getFileName());
    assertEquals(videos.get(1).getSize(), videoB.getSize());
  }

  @Test
  @Order(7)
  @DisplayName("6.1. getVideoById --------------------> Ok.")
  public void getVideoByIdOk() throws Exception {
    Video video = new Video(videoNameOk, videoSizeOk);
    video.setId(videoIdOk);
    Drone drone = new Drone(drnNameOk, drnModelOk, drnCpWeightOk);
    drone.setId(drnIdOk);
    video.setDrone(drone);

    when(videoRepository.findById(videoIdOk)).thenReturn(Optional.of(video));

    Video videoFound = deliveryService.getVideoById(videoIdOk);

    assertEquals(videoFound.getId(), video.getId());
    assertEquals(videoFound.getFileName(), video.getFileName());
    assertEquals(videoFound.getSize(), video.getSize());
    assertEquals(videoFound.getDrone().getId(), video.getDrone().getId());
    assertEquals(videoFound.getDrone().getName(), video.getDrone().getName());
    assertEquals(videoFound.getDrone().getModel(), video.getDrone().getModel());
    assertEquals(videoFound.getDrone().getCapacityWeightInKg(),
        video.getDrone().getCapacityWeightInKg());
  }

  @Test
  @Order(8)
  @DisplayName("6.2. getVideoById --------------------> InputNotFoundException.")
  public void getVideoByIdWithVideoIdNotFound() throws Exception {
    when(videoRepository.findById(notExistentId)).thenReturn(Optional.empty());

    assertThrows(InputNotFoundException.class,
        () -> deliveryService.getVideoById(notExistentId));
  }

  @Test
  @Order(9)
  @DisplayName("7. getAllDeliveries --------------------> Ok.")
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

    when(deliveryRepository.findAll()).thenReturn(allDeliveries);

    List<Delivery> deliveries = deliveryService.getAllDeliveries();

    assertEquals(deliveries.size(), allDeliveries.size());
    assertEquals(deliveries.get(0).getId(), deliveryA.getId());
    assertEquals(deliveries.get(0).getReceiverName(), deliveryA.getReceiverName());
    assertEquals(deliveries.get(0).getAddress(), deliveryA.getAddress());
    assertEquals(deliveries.get(0).getZipCode(), deliveryA.getZipCode());
    assertEquals(deliveries.get(0).getLatitude(), deliveryA.getLatitude());
    assertEquals(deliveries.get(0).getLongitude(), deliveryA.getLongitude());
    assertEquals(deliveries.get(0).getWeightInKg(), deliveryA.getWeightInKg());
    assertEquals(deliveries.get(1).getId(), deliveryB.getId());
    assertEquals(deliveries.get(1).getReceiverName(), deliveryB.getReceiverName());
    assertEquals(deliveries.get(1).getAddress(), deliveryB.getAddress());
    assertEquals(deliveries.get(1).getZipCode(), deliveryB.getZipCode());
    assertEquals(deliveries.get(1).getLatitude(), deliveryB.getLatitude());
    assertEquals(deliveries.get(1).getLongitude(), deliveryB.getLongitude());
    assertEquals(deliveries.get(1).getWeightInKg(), deliveryB.getWeightInKg());
  }

  @Test
  @Order(10)
  @DisplayName("8.1. getDeliveryById --------------------> Ok.")
  public void getDeliveryByIdOk() throws Exception {
    Delivery delivery = new Delivery(dlvReceiverNameOk, dlvAddressOk, dlvZipCodeOk, dlvLatitudeOk,
        dlvLongitudeOk, dlvWeightInKgOk);
    delivery.setId(dlvIdOk);

    when(deliveryRepository.findById(dlvIdOk)).thenReturn(Optional.of(delivery));

    Delivery deliveryFound = deliveryService.getDeliveryById(dlvIdOk);

    assertEquals(deliveryFound.getId(), delivery.getId());
    assertEquals(deliveryFound.getReceiverName(), delivery.getReceiverName());
    assertEquals(deliveryFound.getAddress(), delivery.getAddress());
    assertEquals(deliveryFound.getZipCode(), delivery.getZipCode());
    assertEquals(deliveryFound.getLatitude(), delivery.getLatitude());
    assertEquals(deliveryFound.getLongitude(), delivery.getLongitude());
    assertEquals(deliveryFound.getWeightInKg(), delivery.getWeightInKg());
  }

  @Test
  @Order(11)
  @DisplayName("8.2. getDeliveryById --------------------> InputNotFoundException.")
  public void getDeliveryByIdWithIdNotFound() throws Exception {
    when(deliveryRepository.findById(notExistentId)).thenReturn(Optional.empty());

    assertThrows(InputNotFoundException.class,
        () -> deliveryService.getDeliveryById(notExistentId));
  }

  @Test
  @Order(12)
  @DisplayName("9.1. removeDelivery --------------------> Ok.")
  public void removeDeliveryOk() throws Exception {
    Delivery delivery = new Delivery(dlvReceiverNameOk, dlvAddressOk, dlvZipCodeOk, dlvLatitudeOk,
        dlvLongitudeOk, dlvWeightInKgOk);
    delivery.setId(dlvIdOk);

    when(deliveryRepository.findById(dlvIdOk)).thenReturn(Optional.of(delivery));
    doNothing().when(deliveryRepository).delete(delivery);

    Long idRemoved = deliveryService.removeDelivery(dlvIdOk);

    assertEquals(idRemoved, delivery.getId());
  }

  @Test
  @Order(13)
  @DisplayName("9.2. removeDelivery --------------------> InputNotFoundException.")
  public void removeDeliveryWithIdNotFound() throws Exception {
    when(deliveryRepository.findById(notExistentId)).thenReturn(Optional.empty());

    assertThrows(InputNotFoundException.class,
        () -> deliveryService.getDeliveryById(notExistentId));
  }

  @Test
  @Order(14)
  @DisplayName("10.1. updateDelivery --------------------> Ok.")
  public void updateDeliveryOk() throws Exception {
    DeliveryDto deliveryDto = new DeliveryDto();
    deliveryDto.setReceiverName(dlvReceiverNameOk);
    deliveryDto.setAddress(dlvAddressOk);
    deliveryDto.setZipCode(dlvZipCodeOk);
    deliveryDto.setLatitude(dlvLatitudeOk);
    deliveryDto.setLongitude(dlvLongitudeOk);
    deliveryDto.setWeightInKg(dlvWeightInKgOk);

    Delivery deliveryA = new Delivery();
    when(deliveryRepository.findById(dlvIdOk)).thenReturn(Optional.of(deliveryA));

    Delivery deliveryB = new Delivery(deliveryDto.getReceiverName(), deliveryDto.getAddress(),
        deliveryDto.getZipCode(), deliveryDto.getLatitude(), deliveryDto.getLongitude(),
        deliveryDto.getWeightInKg());
    deliveryB.setId(dlvIdOk);
    when(deliveryRepository.save(deliveryA)).thenReturn(deliveryB);

    Delivery deliveryUpdated = deliveryService.updateDelivery(dlvIdOk, deliveryDto);

    assertEquals(deliveryUpdated.getId(), deliveryB.getId());
    assertEquals(deliveryUpdated.getReceiverName(), deliveryB.getReceiverName());
    assertEquals(deliveryUpdated.getAddress(), deliveryB.getAddress());
    assertEquals(deliveryUpdated.getZipCode(), deliveryB.getZipCode());
    assertEquals(deliveryUpdated.getLatitude(), deliveryB.getLatitude());
    assertEquals(deliveryUpdated.getLongitude(), deliveryB.getLongitude());
    assertEquals(deliveryUpdated.getWeightInKg(), deliveryB.getWeightInKg());
  }

  @Test
  @Order(15)
  @DisplayName("10.2. updateDelivery --------------------> InputNotFoundException.")
  public void updateDeliveryWithIdNotFound() throws Exception {
    when(deliveryRepository.findById(notExistentId)).thenReturn(Optional.empty());

    assertThrows(InputNotFoundException.class,
        () -> deliveryService.getDeliveryById(notExistentId));
  }

  @Test
  @Order(16)
  @DisplayName("11.1. getVideoAsResource --------------------> Ok.")
  public void getVideoAsResourceOk() throws Exception {
    ArrayList<Path> filesMock = new ArrayList<Path>();
    Path videoMock = Paths.get("videos-uploads/" + videoNameOk);
    filesMock.add(videoMock);

    try (MockedStatic<Files> utilities = Mockito.mockStatic(Files.class)) {
      utilities.when(() -> Files.list(Mockito.any(Path.class))).thenReturn(filesMock.stream());

      Resource videoUri = deliveryService.getVideoAsResource(videoNameOk);

      assertEquals(videoUri.getFilename(), videoNameOk);
    }  
  }

  @Test
  @Order(17)
  @DisplayName("11.2. getVideoAsResource --------------------> null.")
  public void getVideoAsResourceWithNotFoundVideo() throws Exception {
    ArrayList<Path> filesMock = new ArrayList<Path>();
    Path videoMock = Paths.get("videos-uploads/" + videoNameOk);
    filesMock.add(videoMock);

    try (MockedStatic<Files> utilities = Mockito.mockStatic(Files.class)) {
      utilities.when(() -> Files.list(Mockito.any(Path.class))).thenReturn(filesMock.stream());

      Resource videoUri = deliveryService.getVideoAsResource(notExistentVideoName);

      assertEquals(videoUri, null);
    }  
  }

  @Test
  @Order(18)
  @DisplayName("11.3. getVideoAsResource --------------------> null.")
  public void getVideoAsResourceWithIoError() throws Exception {
    ArrayList<Path> filesMock = new ArrayList<Path>();
    Path videoMock = Paths.get("videos-uploads/" + videoNameOk);
    filesMock.add(videoMock);

    try (MockedStatic<Files> utilities = Mockito.mockStatic(Files.class)) {
      utilities.when(() -> Files.list(Mockito.any(Path.class))).thenThrow(IOException.class);

      assertThrows(IOException.class,
          () -> deliveryService.getVideoAsResource(videoNameOk));
    }  
  }

  @Test
  @Order(19)
  @DisplayName("12.1. deleteVideo --------------------> Ok.")
  public void deleteVideoOk() throws Exception {
    ArrayList<Path> filesMock = new ArrayList<Path>();
    Path videoMock = Paths.get("videos-uploads/" + videoNameOk);
    filesMock.add(videoMock);
    File fileMock = new File(videoMock.toUri());
    fileMock.createNewFile();

    try (MockedStatic<Files> utilities = Mockito.mockStatic(Files.class)) {
      utilities.when(() -> Files.list(Mockito.any(Path.class))).thenReturn(filesMock.stream());

      Delivery deliveryA = new Delivery();
      deliveryA.setId(dlvIdOk);
      Video video = new Video(videoNameOk, videoSizeOk);
      video.setId(videoIdOk);
      deliveryA.setVideo(video);
      when(deliveryRepository.findById(dlvIdOk)).thenReturn(Optional.of(deliveryA));

      Delivery deliveryB = new Delivery();
      deliveryB.setId(dlvIdOk);
      when(deliveryRepository.save(deliveryA)).thenReturn(deliveryB);

      Delivery deliveryWithoutVideo = deliveryService.deleteVideo(dlvIdOk, videoNameOk);
      assertEquals(deliveryWithoutVideo.getId(), 1L);
    }
  }

  @Test
  @Order(19)
  @DisplayName("12.2. deleteVideo --------------------> InputNotFoundException.")
  public void deleteVideoWithIdNotFound() throws Exception {
    ArrayList<Path> filesMock = new ArrayList<Path>();
    Path videoMock = Paths.get("videos-uploads/" + videoNameOk);
    filesMock.add(videoMock);
    File fileMock = new File(videoMock.toUri());
    fileMock.createNewFile();

    try (MockedStatic<Files> utilities = Mockito.mockStatic(Files.class)) {
      utilities.when(() -> Files.list(Mockito.any(Path.class))).thenReturn(filesMock.stream());
      when(deliveryRepository.findById(dlvIdOk)).thenReturn(Optional.empty());

      assertThrows(InputNotFoundException.class,
          () -> deliveryService.deleteVideo(dlvIdOk, videoNameOk));
    }
  }

  @Test
  @Order(21)
  @DisplayName("12.3. deleteVideo --------------------> IntServerErrorInVideoFinding.")
  public void deleteVideoWithNotFoundVideo() throws Exception {
    ArrayList<Path> filesMock = new ArrayList<Path>();
    Path videoMock = Paths.get("videos-uploads/" + videoNameOk);
    filesMock.add(videoMock);

    try (MockedStatic<Files> utilities = Mockito.mockStatic(Files.class)) {
      utilities.when(() -> Files.list(Mockito.any(Path.class))).thenReturn(filesMock.stream());

      assertThrows(IntServerErrorInVideoFinding.class,
          () -> deliveryService.deleteVideo(dlvIdOk, videoNameOk));
    }
  }

}
