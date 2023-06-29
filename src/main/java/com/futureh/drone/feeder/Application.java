package com.futureh.drone.feeder;

import com.futureh.drone.feeder.model.Delivery;
import com.futureh.drone.feeder.model.Drone;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring main class.
 */
@SpringBootApplication
public class Application implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    Drone firstDrone = new Drone("G305", "Onixstar Hydra - 12", 12f);
    Drone secondDrone = new Drone("A012", "DJI Matrice 100", 3.6f);
    
    String address = "ABC";
    String zipCode = "ABC";
    String longitude = "ABC";
    String latitude = "ABC";
    Float weightInKg = 4.2f;
    Delivery firstDelivery = new Delivery(address, zipCode, longitude, latitude, weightInKg);
    
  }

}
