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

  @Test
  @Order(1)
  @DisplayName("1. O método addDrone deve retornar uma instância da Classe Drone.")
  public void addDroneOk() throws Exception {
    DroneDto droneToSave = new DroneDto();
    droneToSave.setName("BR01");
    droneToSave.setModel("Embraer XYZ 777");
    droneToSave.setCapacityWeightInKg(10.5f);

    Drone droneToReturn = new Drone(droneToSave.getName(), droneToSave.getModel(),
        droneToSave.getCapacityWeightInKg());
    droneToReturn.setId(1L);

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
  @DisplayName("2.1. O método getDroneByName, com o id cadastrado na banco de dados, deve retornar"
      + " uma instância da Classe Drone.")
  public void getDroneByNameOk() throws Exception {
    Drone droneA = new Drone("BR01", "Embraer XYZ 777", 10.5f);
    droneA.setId(1L);
    Drone droneB = new Drone("BR02", "Embraer XYZ 787", 11.6f);
    droneB.setId(2L);
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
  @DisplayName("2.2. O método getDroneByName, sem o id cadastrado na banco de dados, deve retornar"
      + " uma exceção do tipo InputNotFoundException.")
  public void getDroneByNameNotFound() throws Exception {
    Drone droneA = new Drone("BR01", "Embraer XYZ 777", 10.5f);
    droneA.setId(1L);
    Drone droneB = new Drone("BR02", "Embraer XYZ 787", 11.6f);
    droneB.setId(2L);

    List<Drone> dronesToReturn = new ArrayList<Drone>();
    dronesToReturn.add(droneA);
    dronesToReturn.add(droneB);

    when(droneRepository.findAll()).thenReturn(dronesToReturn);

    String notExistentName = "ABCD";
    assertThrows(InputNotFoundException.class,
        () -> droneService.getDroneByName(notExistentName));
  }

  @Test
  @Order(4)
  @DisplayName("3. O método getAllDrones deve retornar uma Lista com instâncias da Classe Drone.")
  public void getAllDronesOk() throws Exception {
    Drone droneA = new Drone("BR01", "Embraer XYZ 777", 10.5f);
    droneA.setId(1L);
    Drone droneB = new Drone("BR02", "Embraer XYZ 787", 11.6f);
    droneB.setId(2L);

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
  @DisplayName("4.1. O método getDroneById, com o id cadastrado na banco de dados, deve retornar"
      + " uma instância da Classe Drone.")
  public void getDroneByIdOk() throws Exception {
    Long idToFind = 1L;
    Drone droneToReturn = new Drone("BR01", "Embraer XYZ 777", 10.5f);
    droneToReturn.setId(idToFind);

    when(droneRepository.findById(idToFind)).thenReturn(Optional.of(droneToReturn));

    Drone droneFound = droneService.getDroneById(idToFind);

    assertEquals(droneFound.getId(), droneToReturn.getId());
    assertEquals(droneFound.getName(), droneToReturn.getName());
    assertEquals(droneFound.getModel(), droneToReturn.getModel());
    assertEquals(droneFound.getCapacityWeightInKg(), droneToReturn.getCapacityWeightInKg());
    assertEquals(droneFound.getVideos(), droneToReturn.getVideos());
  }

  @Test
  @Order(6)
  @DisplayName("4.2. O método getDroneById, sem o id cadastrado na banco de dados, deve retornar"
      + " uma exceção do tipo InputNotFoundException.")
  public void getDroneByIdNotFound() throws Exception {
    Long idToFind = 1L;
    Drone droneToReturn = new Drone("BR01", "Embraer XYZ 777", 10.5f);
    droneToReturn.setId(idToFind);
    
    Long notExistentId = 777L;

    when(droneRepository.findById(notExistentId)).thenReturn(Optional.empty());

    assertThrows(InputNotFoundException.class,
        () -> droneService.getDroneById(notExistentId));
  }

  @Test
  @Order(7)
  //@DisplayName("5.1. Método -------------------> removeDrone.")
  @DisplayName("5.1. O método removeDrone, com o id cadastrado na banco de dados, deve retornar"
      + " o id da instância da Classe Drone removida.")
  public void removeDroneOk() throws Exception {
    Long idToFind = 1L;
    Drone droneToReturn = new Drone("BR01", "Embraer XYZ 777", 10.5f);
    droneToReturn.setId(idToFind);

    when(droneRepository.findById(idToFind)).thenReturn(Optional.of(droneToReturn));
    doNothing().when(droneRepository).delete(droneToReturn);

    Long droneIdDeleted = droneService.removeDrone(idToFind);

    assertEquals(droneIdDeleted, droneToReturn.getId());
  }

  @Test
  @Order(8)
  @DisplayName("5.2. O método removeDrone, sem o id cadastrado na banco de dados, deve retornar"
      + " uma exceção do tipo InputNotFoundException.")
  public void removeDroneWithIdNotFound() throws Exception {
    Long notExistentId = 777L;

    when(droneRepository.findById(notExistentId)).thenReturn(Optional.empty());

    assertThrows(InputNotFoundException.class,
        () -> droneService.removeDrone(notExistentId));
  }

  @Test
  @Order(9)
  @DisplayName("6.1. O método updateDrone, com o id cadastrado na banco de dados, deve retornar"
      + " a instância da Classe Drone atualizada.")
  public void updateDroneOk() throws Exception {
    Long idToFind = 1L;
    Drone droneA = new Drone("BR01", "Embraer XYZ 777", 10.5f);
    when(droneRepository.findById(idToFind)).thenReturn(Optional.of(droneA));

    Drone droneB = new Drone("BR02", "Embraer XYZ 787", 11.6f);
    droneB.setId(idToFind);
    doReturn(droneB).when(droneRepository).save(droneA);

    DroneDto droneDto = new DroneDto();
    droneDto.setName("BR02");
    droneDto.setModel("Embraer XYZ 787");
    droneDto.setCapacityWeightInKg(11.6f);
    Drone droneUpdated = droneService.updateDrone(idToFind, droneDto);

    assertEquals(droneUpdated.getId(), droneB.getId());
  }

  @Test
  @Order(10)
  @DisplayName("6.2. O método updateDrone, sem o id cadastrado na banco de dados, deve retornar"
      + " uma exceção do tipo InputNotFoundException.")
  public void updateDroneWithIdNotFound() throws Exception {
    Long notExistentId = 777L;

    when(droneRepository.findById(notExistentId)).thenReturn(Optional.empty());

    DroneDto droneDto = new DroneDto();
    droneDto.setName("BR02");
    droneDto.setModel("Embraer XYZ 787");
    droneDto.setCapacityWeightInKg(11.6f);

    assertThrows(InputNotFoundException.class,
        () -> droneService.updateDrone(notExistentId, droneDto));
  }

}
