package com.isd.parking.repository;

import com.isd.parking.models.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Parking Lots database repository
 */
@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long> {
}


