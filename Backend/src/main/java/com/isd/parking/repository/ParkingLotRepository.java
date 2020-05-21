package com.isd.parking.repository;

import com.isd.parking.models.subjects.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Parking Lots database repository
 */
@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long> {

    Optional<ParkingLot> findByNumber(Integer parkingLotNumber);

}


