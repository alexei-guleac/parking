package com.isd.parking.service;

import com.isd.parking.model.ParkingLot;

import java.util.List;
import java.util.Optional;

public interface ParkingLotService {
    List<ParkingLot> findAll();

    Optional<ParkingLot> findById(Long parkingLotId);

    ParkingLot save(ParkingLot parkingLot);

    long countAll();
}
