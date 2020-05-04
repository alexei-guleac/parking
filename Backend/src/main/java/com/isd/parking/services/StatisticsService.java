package com.isd.parking.services;

import com.isd.parking.models.subjects.ParkingLot;
import com.isd.parking.models.subjects.StatisticsRecord;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public interface StatisticsService {

    @NotNull List<StatisticsRecord> listAll();

    @NotNull List<StatisticsRecord> findByLotNumber(int parkingLotNumber);

    @NotNull StatisticsRecord save(StatisticsRecord statisticsRecord);

    @NotNull StatisticsRecord save(ParkingLot parkingLot);
}
