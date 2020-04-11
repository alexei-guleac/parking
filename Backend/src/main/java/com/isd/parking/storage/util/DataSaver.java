package com.isd.parking.storage.util;

import com.isd.parking.models.ParkingLot;
import com.isd.parking.service.implementations.ParkingLotDBServiceImpl;
import com.isd.parking.service.implementations.ParkingLotLocalServiceImpl;
import com.isd.parking.utils.ColorConsoleOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Service
@Slf4j
public class DataSaver {

    private final ParkingLotLocalServiceImpl parkingLotLocalService;

    private final ParkingLotDBServiceImpl parkingLotDBService;

    private final ColorConsoleOutput console;

    @Autowired
    public DataSaver(ParkingLotLocalServiceImpl parkingLotLocalService,
                     ParkingLotDBServiceImpl parkingLotDBService, ColorConsoleOutput console) {
        this.parkingLotLocalService = parkingLotLocalService;
        this.parkingLotDBService = parkingLotDBService;
        this.console = console;
    }

    @PreDestroy
    public void fromLocalToDatabaseTransfer() {
        log.info(console.methodMsg(""));
        for (ParkingLot parkingLot : parkingLotLocalService.findAll()) {
            //saving parking lots state to Database  from local Java memory
            parkingLotDBService.save(parkingLot);
        }
    }
}
