package com.futureh.drone.feeder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import com.futureh.drone.feeder.dto.DroneDto;
import com.futureh.drone.feeder.exception.InputNotFoundException;
import com.futureh.drone.feeder.model.Drone;
import com.futureh.drone.feeder.repository.DroneRepository;
import com.futureh.drone.feeder.service.DroneService;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class DroneServiceTest {

  @InjectMocks
  private DroneService droneService;

  @Mock
  private DroneRepository droneRepository;

  private Long drnIdOk = 1L;
  private String drnNameOk = "BR01";
  private String drnModelOk = "Embraer XYZ 777";
  private Float drnCpWeightInKgOk = 10.5f;
  private Long drnIdOkToo = 1L;
  private String drnNameOkToo = "BR02";
  private String drnModelOkToo = "Embraer XYZ 787";
  private Float drnCpWeightInKgOkToo = 11.6f;

  private String notExistentName = "ABCD";
  private Long notExistentId = 999L;

  @Test
  @Order(1)
  @DisplayName("1. Verifica se o método addDrone retorna uma instância da Classe Drone"
      + " adicionada ao banco de dados.")
  public void addDroneOk() throws Exception {
    DroneDto droneToSave = new DroneDto();
    droneToSave.setName(drnNameOk);
    droneToSave.setModel(drnModelOk);
    droneToSave.setCapacityWeightInKg(drnCpWeightInKgOk);

    Drone droneToReturn = new Drone(droneToSave.getName(), droneToSave.getModel(),
        droneToSave.getCapacityWeightInKg());
    droneToReturn.setId(drnIdOk);

    when(droneRepository.save(any(Drone.class))).thenReturn(droneToReturn);

    Drone newDrone = droneService.addDrone(droneToSave);

    assertEquals(newDrone.getId(), droneToReturn.getId());
    assertEquals(newDrone.getName(), droneToReturn.getName());
    assertEquals(newDrone.getModel(), droneToReturn.getModel());
    assertEquals(newDrone.getCapacityWeightInKg(), droneToReturn.getCapacityWeightInKg());
    assertEquals(newDrone.getVideos(), droneToReturn.getVideos());
  }

  @Test
  @Order(2)
  @DisplayName("2.1. Verifica se o método getDroneByName, com o nome cadastrado na banco de dados,"
      + " retorna a instância da Classe Drone encontrada no banco de dados.")
  public void getDroneByNameOk() throws Exception {
    Drone droneA = new Drone(drnNameOk, drnModelOk, drnCpWeightInKgOk);
    droneA.setId(drnIdOk);
    Drone droneB = new Drone(drnNameOkToo, drnModelOkToo, drnCpWeightInKgOkToo);
    droneB.setId(drnIdOkToo);
    List<Drone> dronesToReturn = new ArrayList<Drone>();
    dronesToReturn.add(droneA);
    dronesToReturn.add(droneB);

    when(droneRepository.findAll()).thenReturn(dronesToReturn);

    Drone droneFound = droneService.getDroneByName(droneB.getName());

    assertEquals(droneFound.getId(), droneB.getId());
    assertEquals(droneFound.getName(), droneB.getName());
    assertEquals(droneFound.getModel(), droneB.getModel());
    assertEquals(droneFound.getCapacityWeightInKg(), droneB.getCapacityWeightInKg());
    assertEquals(droneFound.getVideos(), droneB.getVideos());
  }

  @Test
  @Order(3)
  @DisplayName("2.2. Verifica se o método getDroneByName, sem o nome cadastrado na banco de dados,"
      + " retorna uma exceção do tipo InputNotFoundException.")
  public void getDroneByNameNotFound() throws Exception {
    Drone droneA = new Drone(drnNameOk, drnModelOk, drnCpWeightInKgOk);
    droneA.setId(drnIdOk);
    Drone droneB = new Drone(drnNameOkToo, drnModelOkToo, drnCpWeightInKgOkToo);
    droneB.setId(drnIdOkToo);

    List<Drone> dronesToReturn = new ArrayList<Drone>();
    dronesToReturn.add(droneA);
    dronesToReturn.add(droneB);

    when(droneRepository.findAll()).thenReturn(dronesToReturn);

    assertThrows(InputNotFoundException.class,
        () -> droneService.getDroneByName(notExistentName));
  }

  @Test
  @Order(4)
  @DisplayName("3. Verifica se o método getAllDrones retorna uma Lista com as instâncias da Classe"
      + " Drone cadastradas no banco de dados.")
  public void getAllDronesOk() throws Exception {
    Drone droneA = new Drone(drnNameOk, drnModelOk, drnCpWeightInKgOk);
    droneA.setId(drnIdOk);
    Drone droneB = new Drone(drnNameOkToo, drnModelOkToo, drnCpWeightInKgOkToo);
    droneB.setId(drnIdOkToo);

    List<Drone> dronesToReturn = new ArrayList<Drone>();
    dronesToReturn.add(droneA);
    dronesToReturn.add(droneB);

    when(droneRepository.findAll()).thenReturn(dronesToReturn);

    List<Drone> dronesFound = droneService.getAllDrones();

    assertEquals(dronesFound.size(), 2);
    assertEquals(dronesFound.get(0).getId(), droneA.getId());
    assertEquals(dronesFound.get(0).getName(), droneA.getName());
    assertEquals(dronesFound.get(0).getModel(), droneA.getModel());
    assertEquals(dronesFound.get(0).getCapacityWeightInKg(), droneA.getCapacityWeightInKg());
    assertEquals(dronesFound.get(0).getVideos(), droneA.getVideos());
    assertEquals(dronesFound.get(1).getId(), droneB.getId());
    assertEquals(dronesFound.get(1).getName(), droneB.getName());
    assertEquals(dronesFound.get(1).getModel(), droneB.getModel());
    assertEquals(dronesFound.get(1).getCapacityWeightInKg(), droneB.getCapacityWeightInKg());
    assertEquals(dronesFound.get(1).getVideos(), droneB.getVideos());
  }

  @Test
  @Order(5)
  @DisplayName("4.1. Verifica se o método getDroneById, com o id cadastrado na banco de dados,"
      + " retorna a instância da Classe Drone encontrada no banco de dados.")
  public void getDroneByIdOk() throws Exception {
    Drone droneToReturn = new Drone(drnNameOk, drnModelOk, drnCpWeightInKgOk);
    droneToReturn.setId(drnIdOk);

    when(droneRepository.findById(drnIdOk)).thenReturn(Optional.of(droneToReturn));

    Drone droneFound = droneService.getDroneById(drnIdOk);

    assertEquals(droneFound.getId(), droneToReturn.getId());
    assertEquals(droneFound.getName(), droneToReturn.getName());
    assertEquals(droneFound.getModel(), droneToReturn.getModel());
    assertEquals(droneFound.getCapacityWeightInKg(), droneToReturn.getCapacityWeightInKg());
    assertEquals(droneFound.getVideos(), droneToReturn.getVideos());
  }

  @Test
  @Order(6)
  @DisplayName("4.2. Verifica se o método getDroneById, sem o id cadastrado na banco de dados,"
      + " retorna uma exceção do tipo InputNotFoundException.")
  public void getDroneByIdNotFound() throws Exception {
    Drone droneToReturn = new Drone(drnNameOk, drnModelOk, drnCpWeightInKgOk);
    droneToReturn.setId(drnIdOk);

    when(droneRepository.findById(notExistentId)).thenReturn(Optional.empty());

    assertThrows(InputNotFoundException.class,
        () -> droneService.getDroneById(notExistentId));
  }

  @Test
  @Order(7)
  @DisplayName("5.1. Verifica se o método removeDrone, com o id cadastrado na banco de dados,"
      + " retorna o id da instância da Classe Drone removida.")
  public void removeDroneOk() throws Exception {
    Drone droneToReturn = new Drone(drnNameOk, drnModelOk, drnCpWeightInKgOk);
    droneToReturn.setId(drnIdOk);

    when(droneRepository.findById(drnIdOk)).thenReturn(Optional.of(droneToReturn));
    doNothing().when(droneRepository).delete(droneToReturn);

    Long droneIdDeleted = droneService.removeDrone(drnIdOk);

    assertEquals(droneIdDeleted, droneToReturn.getId());
  }

  @Test
  @Order(8)
  @DisplayName("5.2. Verifica se o método removeDrone, sem o id cadastrado na banco de dados,"
      + " retorna uma exceção do tipo InputNotFoundException.")
  public void removeDroneWithIdNotFound() throws Exception {
    when(droneRepository.findById(notExistentId)).thenReturn(Optional.empty());

    assertThrows(InputNotFoundException.class,
        () -> droneService.removeDrone(notExistentId));
  }

  @Test
  @Order(9)
  @DisplayName("6.1. Verifica se o método updateDrone, com o id cadastrado na banco de dados,"
      + " retorna a instância da Classe Drone atualizada.")
  public void updateDroneOk() throws Exception {
    Drone droneA = new Drone(drnNameOk, drnModelOk, drnCpWeightInKgOk);
    when(droneRepository.findById(drnIdOk)).thenReturn(Optional.of(droneA));

    Drone droneB = new Drone(drnNameOkToo, drnModelOkToo, drnCpWeightInKgOkToo);
    droneB.setId(drnIdOk);
    doReturn(droneB).when(droneRepository).save(droneA);

    DroneDto droneDto = new DroneDto();

    Drone droneUpdated = droneService.updateDrone(drnIdOk, droneDto);

    assertEquals(droneUpdated.getId(), droneB.getId());
    assertEquals(droneUpdated.getName(), droneB.getName());
    assertEquals(droneUpdated.getModel(), droneB.getModel());
    assertEquals(droneUpdated.getCapacityWeightInKg(), droneB.getCapacityWeightInKg());
  }

  @Test
  @Order(10)
  @DisplayName("6.2. Verifica se o método updateDrone, sem o id cadastrado na banco de dados,"
      + " retorna uma exceção do tipo InputNotFoundException.")
  public void updateDroneWithIdNotFound() throws Exception {
    when(droneRepository.findById(notExistentId)).thenReturn(Optional.empty());

    DroneDto droneDto = new DroneDto();

    assertThrows(InputNotFoundException.class,
        () -> droneService.updateDrone(notExistentId, droneDto));
  }

}
