package com.futureh.drone.feeder;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.futureh.drone.feeder.dto.DroneDto;
import com.futureh.drone.feeder.model.Drone;
import com.futureh.drone.feeder.service.DroneService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class DroneControllerTest {

  @MockBean
  private DroneService droneService;

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("1. Teste da rota POST /drone/new --------> OK.")
  public void postDroneOk() throws Exception {
    DroneDto droneToPost = new DroneDto();
    droneToPost.setName("BR01");
    droneToPost.setModel("Embraer XYZ 777");
    droneToPost.setCapacityWeightInKg(10.5f);

    Drone droneToReturn = new Drone(droneToPost.getName(), droneToPost.getModel(),
        droneToPost.getCapacityWeightInKg());
    droneToReturn.setId(1L);

    when(droneService.addDrone(any(DroneDto.class))).thenReturn(droneToReturn);

    this.mockMvc.perform(post("/drone/new")
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(droneToPost))
    ).andExpect(status().isOk())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.id", is(droneToReturn.getId().intValue())))
    .andExpect(jsonPath("$.name", is(droneToReturn.getName())))
    .andExpect(jsonPath("$.model", is(droneToReturn.getModel())))
    .andExpect(jsonPath("$.capacityWeightInKg",
        is(closeTo(droneToReturn.getCapacityWeightInKg(), 0.0001))))
    .andExpect(jsonPath("$.video").doesNotExist());
  }

  @Test
  @DisplayName("2. Teste da rota GET /drone/all --------> OK.")
  public void getAllDrones() throws Exception {
    Drone droneA = new Drone("BR01", "Embraer XYZ 777", 10.5f);
    droneA.setId(1L);
    Drone droneB = new Drone("BR02", "Embraer XYZ 787", 11.6f);
    droneB.setId(2L);
    List<Drone> dronesToReturn = new ArrayList<Drone>();
    dronesToReturn.add(droneA);
    dronesToReturn.add(droneB);

    when(droneService.getAllDrones()).thenReturn(dronesToReturn);

    this.mockMvc.perform(get("/drone/all"))
      .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$.[0]id", is(droneA.getId().intValue())))
        .andExpect(jsonPath("$.[1]id", is(droneB.getId().intValue())))
        .andExpect(jsonPath("$.[0]name", is(droneA.getName())))
        .andExpect(jsonPath("$.[1]name", is(droneB.getName())))
        .andExpect(jsonPath("$.[0]model", is(droneA.getModel())))
        .andExpect(jsonPath("$.[1]model", is(droneB.getModel())))
        .andExpect(jsonPath("$.[0]capacityWeightInKg",
            is(closeTo(droneA.getCapacityWeightInKg(), 0.0001))))
        .andExpect(jsonPath("$.[1]capacityWeightInKg",
            is(closeTo(droneB.getCapacityWeightInKg(), 0.0001))))
        .andExpect(jsonPath("$.[0]video").doesNotExist())
        .andExpect(jsonPath("$.[1]video").doesNotExist());
  }

  @Test
  @DisplayName("3. Teste da rota GET /drone/{id} --------> OK.")
  public void getDroneByIdOk() throws Exception {
    Long paramId = 1L;
    Drone droneToReturn = new Drone("BR01", "Embraer XYZ 777", 10.5f);
    droneToReturn.setId(paramId);

    when(droneService.getDroneById(paramId)).thenReturn(droneToReturn);

    this.mockMvc.perform(get("/drone/" + paramId))
      .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(droneToReturn.getId().intValue())))
        .andExpect(jsonPath("$.name", is(droneToReturn.getName())))
        .andExpect(jsonPath("$.model", is(droneToReturn.getModel())))
        .andExpect(jsonPath("$.capacityWeightInKg",
            is(closeTo(droneToReturn.getCapacityWeightInKg(), 0.0001))))
        .andExpect(jsonPath("$.video").doesNotExist());
  }

  @Test
  @DisplayName("4. Teste da rota DELETE /drone/delete/{id} --------> OK.")
  public void deleteDroneOk() throws Exception {
    Long paramId = 1L;

    when(droneService.removeDrone(paramId)).thenReturn(paramId);

    this.mockMvc.perform(delete("/drone/delete/" + paramId))
      .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Id " + paramId + " has been removed.")));
  }

  @Test
  @DisplayName("5. Teste da rota PUT /drone/update/{id} --------> OK.")
  public void updateDroneOk() throws Exception {
    DroneDto droneToPost = new DroneDto();
    droneToPost.setName("BR02");
    droneToPost.setModel("Embraer XYZ 787");
    droneToPost.setCapacityWeightInKg(11.6f);

    Long paramId = 1L;
    Drone droneToReturn = new Drone(
        droneToPost.getName(), droneToPost.getModel(), droneToPost.getCapacityWeightInKg());
    droneToReturn.setId(paramId);

    when(droneService.updateDrone(any(Long.class), any(DroneDto.class))).thenReturn(droneToReturn);

    this.mockMvc.perform(put("/drone/update/" + paramId)
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(droneToPost))
        ).andExpect(status().isOk())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.id", is(droneToReturn.getId().intValue())))
    .andExpect(jsonPath("$.name", is(droneToReturn.getName())))
    .andExpect(jsonPath("$.model", is(droneToReturn.getModel())))
    .andExpect(jsonPath("$.capacityWeightInKg",
        is(closeTo(droneToReturn.getCapacityWeightInKg(), 0.0001))))
    .andExpect(jsonPath("$.video").doesNotExist());
  }

  static String asJsonString(Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
