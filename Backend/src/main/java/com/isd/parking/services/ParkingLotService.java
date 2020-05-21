package com.isd.parking.services;

import com.isd.parking.models.subjects.ParkingLot;

import java.util.List;
import java.util.Optional;


public interface ParkingLotService {

    List<ParkingLot> findAll();

    Optional<ParkingLot> findById(Long parkingLotId);

    ParkingLot save(ParkingLot parkingLot);

    long countAll();

    Optional<ParkingLot> findByLotNumber(Integer parkingLotNumber);
}
