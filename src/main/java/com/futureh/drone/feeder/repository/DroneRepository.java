package com.futureh.drone.feeder.repository;

import com.futureh.drone.feeder.model.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Drone interface.
 */
@Repository
public interface DroneRepository extends JpaRepository<Drone, Long> {

}
