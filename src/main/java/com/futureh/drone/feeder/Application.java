package com.futureh.drone.feeder;

import com.futureh.drone.feeder.model.Delivery;
import com.futureh.drone.feeder.model.Drone;
import com.futureh.drone.feeder.model.Video;
import com.futureh.drone.feeder.repository.DeliveryRepository;
import com.futureh.drone.feeder.repository.DroneRepository;
import com.futureh.drone.feeder.util.DeliveryStatus;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring main class.
 */
@SpringBootApplication
public class Application implements CommandLineRunner {

  @Autowired
  private DroneRepository droneRepository;

  @Autowired
  private DeliveryRepository deliveryRepository;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    Drone firstDrone = new Drone("A011", "DJI Matrice 100", 3.6f);
    Drone secondDrone = new Drone("A012", "DJI Matrice 100", 3.6f);
    Drone thirdDrone = new Drone("G305", "Onixstar Hydra - 12", 12f);
    droneRepository.saveAll(Arrays.asList(firstDrone, secondDrone, thirdDrone));

    String address1 = "Praça Mauá, 1 - Centro, Rio de Janeiro - RJ";
    String zipCode1 = "20081-240";
    String longitude1 = "-22.895642";
    String latitude1 = "-43.180038";
    Float weightInKg1 = 5.2f;
    Delivery firstDelivery = new Delivery(address1, zipCode1, longitude1, latitude1, weightInKg1);

    String fileName = "G305-2023-06-12-191255.mp4";
    Long size = 2861955L;
    Video firstVideo = new Video(fileName, size);
    firstVideo.setDrone(thirdDrone);
    firstDelivery.setVideo(firstVideo);
    firstDelivery.setStatus(DeliveryStatus.DELIVERED);

    String address2 = "Praça Pio X, s/n - Centro, Rio de Janeiro - RJ";
    String zipCode2 = "20040-020";
    String longitude2 = "-22.900698";
    String latitude2 = "-43.177297";
    Float weightInKg2 = 2.5f;
    Delivery secondDelivery = new Delivery(address2, zipCode2, longitude2, latitude2, weightInKg2);

    deliveryRepository.saveAll(Arrays.asList(firstDelivery, secondDelivery));
  }

}
