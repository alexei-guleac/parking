package com.isd.parking.storage.util;

import com.isd.parking.models.subjects.ParkingLot;
import com.isd.parking.repository.ParkingLotRepository;
import com.isd.parking.repository.ParkingLotStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;


/**
 * Utility class
 * Saves all parking lots states to the database from local Java memory storage
 * before server is shutting down
 */
@Service
@Slf4j
public class DataSaver {

    private final ParkingLotStorage parkingLotStorage;

    private final ParkingLotRepository parkingLotRepository;

    @Autowired
    public DataSaver(ParkingLotStorage parkingLotStorage,
                     ParkingLotRepository parkingLotRepository) {
        this.parkingLotStorage = parkingLotStorage;
        this.parkingLotRepository = parkingLotRepository;
    }

    /**
     * Saves all parking lots to SQL database from local Java memory storage
     */
    @PreDestroy
    public void fromLocalToDatabaseTransfer() {
        for (ParkingLot parkingLot : parkingLotStorage.findAll()) {
            //saving parking lots state to Database from local Java memory
            parkingLotRepository.save(parkingLot);
        }
    }
}
