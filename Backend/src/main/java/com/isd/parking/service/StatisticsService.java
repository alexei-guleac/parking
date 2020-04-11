package com.isd.parking.service;

import com.isd.parking.models.ParkingLot;
import com.isd.parking.models.StatisticsRecord;

import java.util.List;


public interface StatisticsService {

    List<StatisticsRecord> listAll();

    StatisticsRecord save(StatisticsRecord statisticsRecord);

    StatisticsRecord save(ParkingLot parkingLot);
}
