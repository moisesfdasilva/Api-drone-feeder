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
import com.futureh.drone.feeder.exception.InputNotFoundException;
import com.futureh.drone.feeder.model.Drone;
import com.futureh.drone.feeder.service.DroneService;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class DroneControllerTest {

  @MockBean
  private DroneService droneService;

  @Autowired
  private MockMvc mockMvc;

  private Long drnIdOk = 1L;
  private String drnNameOk = "BR01";
  private String drnModelOk = "Embraer XYZ 777";
  private Float drnCpWeightOk = 10.5f;

  private Long drnIdOkToo = 2L;
  private String drnNameOkToo = "BR02";
  private String drnModelOkToo = "Embraer XYZ 787";
  private Float drnCpWeightOkToo = 11.6f;

  private String drnNameLonger = "BR001";
  private String drnNameShorter = "BR1";
  private String drnNameLowerCase = "br01";
  private String drnNameNonWordCharacter = "!BR1";
  private String drnModelLonger = "Embraer XYZ 777-00000000000000000";

  @Test
  @Order(1)
  @DisplayName("1.1. A rota POST /drone/new, com nome, modelo e capacidade do Drone corretos,"
      + " deve retornar status 200 e body contendo os dados do Drone.")
  public void postDroneOk() throws Exception {
    DroneDto droneToPost = new DroneDto();
    droneToPost.setName(drnNameOk);
    droneToPost.setModel(drnModelOk);
    droneToPost.setCapacityWeightInKg(drnCpWeightOk);

    Drone droneToReturn = new Drone(droneToPost.getName(), droneToPost.getModel(),
        droneToPost.getCapacityWeightInKg());
    droneToReturn.setId(drnIdOk);

    when(droneService.addDrone(any(DroneDto.class))).thenReturn(droneToReturn);

    this.mockMvc.perform(post("/drone/new")
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(droneToPost))
    ).andExpect(status().isCreated())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.id", is(droneToReturn.getId().intValue())))
    .andExpect(jsonPath("$.name", is(droneToReturn.getName())))
    .andExpect(jsonPath("$.model", is(droneToReturn.getModel())))
    .andExpect(jsonPath("$.capacityWeightInKg",
        is(closeTo(droneToReturn.getCapacityWeightInKg(), 0.0001))))
    .andExpect(jsonPath("$.video").doesNotExist());
  }

  @Test
  @Order(2)
  @DisplayName("1.2. A rota POST /drone/new, com o nome do Drone possuindo mais que 4 caracteres,"
      + " deve retornar status 400 e body contendo a mensagem de erro.")
  public void postDroneWithNameLongerThanDefault() throws Exception {
    DroneDto droneToPost = new DroneDto();
    droneToPost.setName(drnNameLonger);

    this.mockMvc.perform(post("/drone/new")
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(droneToPost))
    ).andExpect(status().isBadRequest())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", is("Drone name must be 4 characters.")));
  }

  @Test
  @Order(3)
  @DisplayName("1.3. A rota POST /drone/new, com o nome do Drone possuindo menos que 4 caracteres,"
      + " deve retornar status 400 e body contendo a mensagem de erro.")
  public void postDroneWithNameShorterThanDefault() throws Exception {
    DroneDto droneToPost = new DroneDto();
    droneToPost.setName(drnNameShorter);

    this.mockMvc.perform(post("/drone/new")
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(droneToPost))
    ).andExpect(status().isBadRequest())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", is("Drone name must be 4 characters.")));
  }

  @Test
  @Order(4)
  @DisplayName("1.4. A rota POST /drone/new, com o nome do Drone possuindo letras minúsculas,"
      + " deve retornar status 400 e body contendo a mensagem de erro.")
  public void postDroneWithLowerCaseName() throws Exception {
    DroneDto droneToPost = new DroneDto();
    droneToPost.setName(drnNameLowerCase);

    this.mockMvc.perform(post("/drone/new")
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(droneToPost))
    ).andExpect(status().isBadRequest())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", is("Drone name must have uppercase characters and numbers.")));
  }

  @Test
  @Order(5)
  @DisplayName("1.5. A rota POST /drone/new, com o nome do Drone possuindo caracteres especiais,"
      + " deve retornar status 400 e body contendo a mensagem de erro.")
  public void postDroneWithNonWordCharacterName() throws Exception {
    DroneDto droneToPost = new DroneDto();
    droneToPost.setName(drnNameNonWordCharacter);

    this.mockMvc.perform(post("/drone/new")
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(droneToPost))
    ).andExpect(status().isBadRequest())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", is("Drone name must have uppercase characters and numbers.")));
  }

  @Test
  @Order(6)
  @DisplayName("1.6. A rota POST /drone/new, com o nome do modelo do Drone possuindo mais que 32"
      + " caracteres, deve retornar status 400 e body contendo a mensagem de erro.")
  public void postDroneWithIncorrectModelName() throws Exception {
    DroneDto droneToPost = new DroneDto();
    droneToPost.setName(drnNameOk);
    droneToPost.setModel(drnModelLonger);

    this.mockMvc.perform(post("/drone/new")
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(droneToPost))
    ).andExpect(status().isBadRequest())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", is("Drone name has more than 32 characters.")));
  }

  @Test
  @Order(7)
  @DisplayName("1.7. A rota POST /drone/new, sem a capacidade do Drone,"
      + " deve retornar status 400 e body contendo a mensagem de erro.")
  public void postDroneWithoutWeightCapacityInKg() throws Exception {
    DroneDto droneToPost = new DroneDto();
    droneToPost.setName(drnNameOk);
    droneToPost.setModel(drnModelOk);
    droneToPost.setCapacityWeightInKg(null);

    this.mockMvc.perform(post("/drone/new")
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(droneToPost))
    ).andExpect(status().isBadRequest())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", is("Drone must have weight capacity in kg.")));
  }

  @Test
  @Order(8)
  @DisplayName("2. A rota GET /drone/all deve retornar status 200 e body contendo a lista de Drones"
      + " cadastrados.")
  public void getAllDrones() throws Exception {
    Drone droneA = new Drone(drnNameOk, drnModelOk, drnCpWeightOk);
    droneA.setId(drnIdOk);
    Drone droneB = new Drone(drnNameOkToo, drnModelOkToo, drnCpWeightOkToo);
    droneB.setId(drnIdOkToo);
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
  @Order(9)
  @DisplayName("3.1. A rota GET /drone/{id}, com o id cadastrado na banco de dados,"
      + " deve retornar status 200 e body contendo os dados do Drone.")
  public void getDroneByIdOk() throws Exception {
    Drone droneToReturn = new Drone(drnNameOk, drnModelOk, drnCpWeightOk);
    droneToReturn.setId(drnIdOk);

    when(droneService.getDroneById(drnIdOk)).thenReturn(droneToReturn);

    this.mockMvc.perform(get("/drone/" + drnIdOk))
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
  @Order(10)
  @DisplayName("3.2. A rota GET /drone/{id}, sem o id cadastrado na banco de dados,"
      + " deve retornar status 404 e body contendo a mensagem de erro.")
  public void getDroneByIdNotFound() throws Exception {
    when(droneService.getDroneById(drnIdOk)).thenThrow(
        new InputNotFoundException("Drone id not found."));

    this.mockMvc.perform(get("/drone/" + drnIdOk))
      .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error", is("Drone id not found.")));
  }

  @Test
  @Order(11)
  @DisplayName("4.1. A rota DELETE /drone/delete/{id}, com o id cadastrado na banco de dados,"
      + " deve retornar status 200 e body contendo a mensagem que o id foi removido.")
  public void deleteDroneOk() throws Exception {
    when(droneService.removeDrone(drnIdOk)).thenReturn(drnIdOk);

    this.mockMvc.perform(delete("/drone/delete/" + drnIdOk))
      .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Id " + drnIdOk + " has been removed.")));
  }

  @Test
  @Order(12)
  @DisplayName("4.2. A rota DELETE /drone/delete/{id}, sem o id cadastrado na banco de dados,"
      + " deve retornar status 404 e body contendo a mensagem de erro.")
  public void deleteDroneWithIdNotFound() throws Exception {
    when(droneService.removeDrone(drnIdOk)).thenThrow(
        new InputNotFoundException("Drone id not found."));

    this.mockMvc.perform(delete("/drone/delete/" + drnIdOk))
      .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error", is("Drone id not found.")));
  }

  @Test
  @Order(13)
  @DisplayName("5.1. A rota PUT /drone/update/{id}, com id, nome, modelo e capacidade do Drone"
      + " corretos, deve retornar status 200 e body contendo os dados atualizados do Drone.")
  public void updateDroneOk() throws Exception {
    DroneDto droneToPost = new DroneDto();
    droneToPost.setName(drnNameOkToo);
    droneToPost.setModel(drnModelOkToo);
    droneToPost.setCapacityWeightInKg(drnCpWeightOkToo);

    Drone droneToReturn = new Drone(
        droneToPost.getName(), droneToPost.getModel(), droneToPost.getCapacityWeightInKg());
    droneToReturn.setId(drnIdOk);

    when(droneService.updateDrone(any(Long.class), any(DroneDto.class))).thenReturn(droneToReturn);

    this.mockMvc.perform(put("/drone/update/" + drnIdOk)
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
  @Order(14)
  @DisplayName("5.2. A rota PUT /drone/update/{id}, com o nome do Drone possuindo mais que 4"
      + " caracteres, deve retornar status 400 e body contendo a mensagem de erro.")
  public void updateDroneWithNameLongerThanDefault() throws Exception {
    DroneDto droneToPost = new DroneDto();
    droneToPost.setName(drnNameLonger);

    this.mockMvc.perform(put("/drone/update/" + drnIdOk)
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(droneToPost))
        ).andExpect(status().isBadRequest())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", is("Drone name must be 4 characters.")));
  }

  @Test
  @Order(15)
  @DisplayName("5.3. A rota PUT /drone/update/{id}, com o nome do Drone possuindo menos que 4"
      + " caracteres, deve retornar status 400 e body contendo a mensagem de erro.")
  public void updateDroneWithNameShorterThanDefault() throws Exception {
    DroneDto droneToPost = new DroneDto();
    droneToPost.setName(drnNameShorter);

    this.mockMvc.perform(put("/drone/update/" + drnIdOk)
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(droneToPost))
        ).andExpect(status().isBadRequest())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", is("Drone name must be 4 characters.")));
  }

  @Test
  @Order(16)
  @DisplayName("5.4. A rota PUT /drone/update/{id}, com o nome do Drone possuindo letras"
      + " minúsculas, deve retornar status 400 e body contendo a mensagem de erro.")
  public void updateDroneWithLowerCaseName() throws Exception {
    DroneDto droneToPost = new DroneDto();
    droneToPost.setName(drnNameLowerCase);

    this.mockMvc.perform(put("/drone/update/" + drnIdOk)
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(droneToPost))
        ).andExpect(status().isBadRequest())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", is("Drone name must have uppercase characters and numbers.")));
  }

  @Test
  @Order(17)
  @DisplayName("5.5. A rota PUT /drone/update/{id}, com o nome do Drone possuindo caracteres"
      + " especiais, deve retornar status 400 e body contendo a mensagem de erro.")
  public void updateDroneWithNonWordCharacterName() throws Exception {
    DroneDto droneToPost = new DroneDto();
    droneToPost.setName(drnNameNonWordCharacter);

    this.mockMvc.perform(put("/drone/update/" + drnIdOk)
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(droneToPost))
        ).andExpect(status().isBadRequest())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", is("Drone name must have uppercase characters and numbers.")));
  }

  @Test
  @Order(18)
  @DisplayName("5.6. A rota PUT /drone/update/{id}, com o nome do modelo do Drone possuindo mais"
      + " que 32 caracteres, deve retornar status 400 e body contendo a mensagem de erro.")
  public void updateDroneWithIncorrectModelName() throws Exception {
    DroneDto droneToPost = new DroneDto();
    droneToPost.setName(drnNameOk);
    droneToPost.setModel(drnModelLonger);

    this.mockMvc.perform(put("/drone/update/" + drnIdOk)
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(droneToPost))
        ).andExpect(status().isBadRequest())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", is("Drone name has more than 32 characters.")));
  }

  @Test
  @Order(19)
  @DisplayName("5.7. A rota PUT /drone/update/{id}, sem a capacidade do Drone,"
      + " deve retornar status 400 e body contendo a mensagem de erro.")
  public void updateDroneWithoutWeightCapacityInKg() throws Exception {
    DroneDto droneToPost = new DroneDto();
    droneToPost.setName(drnNameOk);
    droneToPost.setModel(drnModelOk);
    droneToPost.setCapacityWeightInKg(null);

    this.mockMvc.perform(put("/drone/update/" + drnIdOk)
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(droneToPost))
        ).andExpect(status().isBadRequest())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", is("Drone must have weight capacity in kg.")));
  }

  @Test
  @Order(20)
  @DisplayName("5.8. A rota PUT /drone/update/{id}, sem o id cadastrado na banco de dados,"
      + " deve retornar status 404 e body contendo a mensagem de erro.")
  public void updateDroneWithIdNotFound() throws Exception {
    DroneDto droneToPost = new DroneDto();
    droneToPost.setName(drnNameOkToo);
    droneToPost.setModel(drnModelOkToo);
    droneToPost.setCapacityWeightInKg(drnCpWeightOkToo);

    when(droneService.updateDrone(any(Long.class), any(DroneDto.class))).thenThrow(
        new InputNotFoundException("Drone id not found."));

    this.mockMvc.perform(put("/drone/update/" + drnIdOk)
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(droneToPost))
        ).andExpect(status().isNotFound())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.error", is("Drone id not found.")));
  }

  static String asJsonString(Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
