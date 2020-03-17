package com.isd.parking.service;

import com.isd.parking.models.ParkingLot;
import com.isd.parking.repository.ParkingLotLocalStorage;
import com.isd.parking.utils.ColorConsoleOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * Parking Lot Local Service class for local Java memory repository
 * Contains methods for
 * getting all parking lots,
 * get parking lot by id,
 * saving (in this case updating) parking lot
 */
@Service
@Slf4j
public class ParkingLotLocalService implements ParkingLotService {

    private final ParkingLotLocalStorage parkingLotLocalStorage;

    private final ColorConsoleOutput console;

    @Autowired
    public ParkingLotLocalService(ParkingLotLocalStorage parkingLotLocalStorage, ColorConsoleOutput console) {
        this.parkingLotLocalStorage = parkingLotLocalStorage;
        this.console = console;
    }

    /**
     * Get all parking lots from local Java memory method
     *
     * @return - Parking lots list
     */
    public List<ParkingLot> findAll() {
        log.info(console.classMsg(getClass().getSimpleName(),"get all parking lots list executed..."));
        return parkingLotLocalStorage.findAll();
    }

    /**
     * Get parking lot by id from local Java memory method
     *
     * @return - specified parking lot
     */
    public Optional<ParkingLot> findById(Long parkingLotId) {
        log.info(console.classMsg(getClass().getSimpleName(),"get parking lot by id executed..."));
        return parkingLotLocalStorage.findById(parkingLotId);
    }

    /**
     * Save parking lot in local Java memory method
     * Used for update status of parking lot
     *
     * @return - Parking lot which was saved in database
     */
    public ParkingLot save(ParkingLot parkingLot) {
        log.info(console.classMsg(getClass().getSimpleName(),"save parking lot executed..."));
        return parkingLotLocalStorage.save(parkingLot);
    }

    public long countAll() {
        log.info(console.classMsg(getClass().getSimpleName(),"count parking lots..."));
        return parkingLotLocalStorage.count();
    }
}
