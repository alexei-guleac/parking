package com.isd.parking.repository;

import com.isd.parking.models.subjects.ParkingLot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;


public interface ParkingLotStorage {

    @NotNull List<ParkingLot> findAll();

    Optional<ParkingLot> findById(Long parkingLotId);

    Optional<ParkingLot> findByNumber(Integer parkingLotNumber);

    @Nullable ParkingLot save(@NotNull ParkingLot parkingLot);

    @Nullable ParkingLot update(@NotNull ParkingLot parkingLot);

    @Nullable ParkingLot delete(@NotNull ParkingLot parkingLot);

    long count();
}
