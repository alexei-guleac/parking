package com.isd.parking.storage.util;

import com.isd.parking.models.subjects.ParkingLot;
import com.isd.parking.services.implementations.ParkingLotDBServiceImpl;
import com.isd.parking.services.implementations.ParkingLotLocalServiceImpl;
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

    private final ParkingLotLocalServiceImpl parkingLotLocalService;

    private final ParkingLotDBServiceImpl parkingLotDBService;

    @Autowired
    public DataSaver(ParkingLotLocalServiceImpl parkingLotLocalService,
                     ParkingLotDBServiceImpl parkingLotDBService) {
        this.parkingLotLocalService = parkingLotLocalService;
        this.parkingLotDBService = parkingLotDBService;
    }

    /**
     * Saves all parking lots to SQL database from local Java memory storage
     */
    @PreDestroy
    public void fromLocalToDatabaseTransfer() {
        for (ParkingLot parkingLot : parkingLotLocalService.findAll()) {
            //saving parking lots state to Database from local Java memory
            parkingLotDBService.save(parkingLot);
        }
    }
}
