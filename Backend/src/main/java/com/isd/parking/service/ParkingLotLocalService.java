package com.isd.parking.service;

import com.isd.parking.model.ParkingLot;
import com.isd.parking.repository.ParkingLotLocalRepository;
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
public class ParkingLotLocalService {

    private final ParkingLotLocalRepository parkingLotLocalRepository;

    private final ColorConsoleOutput console;

    @Autowired
    public ParkingLotLocalService(ParkingLotLocalRepository parkingLotLocalRepository, ColorConsoleOutput console) {
        this.parkingLotLocalRepository = parkingLotLocalRepository;
        this.console = console;
    }

    /**
     * Get all parking lots from local Java memory method
     *
     * @return - Parking lots list
     */
    public List<ParkingLot> listAll() {
        log.info(console.classMsg("get all parking lots list executed..."));
        return parkingLotLocalRepository.findAll();
    }

    /**
     * Get parking lot by id from local Java memory method
     *
     * @return - specified parking lot
     */
    public Optional<ParkingLot> findById(Long parkingLotId) {
        log.info(console.classMsg("get parking lot by id executed..."));
        return parkingLotLocalRepository.findById(parkingLotId);
    }

    /**
     * Save parking lot in local Java memory method
     * Used for update status of parking lot
     *
     * @return - Parking lot which was saved in database
     */
    public ParkingLot save(ParkingLot parkingLot) {
        log.info(console.classMsg("save parking lot executed..."));
        return parkingLotLocalRepository.save(parkingLot);
    }
}
