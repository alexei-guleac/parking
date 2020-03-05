package com.isd.parking.service;

import com.isd.parking.model.ParkingLot;
import com.isd.parking.repository.ParkingLotRepository;
import com.isd.parking.utils.ColorConsoleOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


/**
 * Parking Lot Service class for database repository
 * Contains methods for
 * getting all parking lots,
 * get parking lot by id,
 * saving (in this case updating) parking lot
 */
@Service
@Slf4j
public class ParkingLotDBService implements ParkingLotService {

    private final ParkingLotRepository parkingLotRepository;

    private final ColorConsoleOutput console;

    @Autowired
    public ParkingLotDBService(ParkingLotRepository parkingLotRepository, ColorConsoleOutput console) {
        this.parkingLotRepository = parkingLotRepository;
        this.console = console;
    }

    /**
     * Get all parking lots from database method
     *
     * @return - Parking lots list
     */
    @Transactional
    @Override
    public List<ParkingLot> findAll() {
        log.info(console.classMsg(getClass().getSimpleName(),"get all parking lots list executed..."));
        return parkingLotRepository.findAll();
    }

    /**
     * Get parking lot by id from database method
     *
     * @return - specified parking lot
     */
    @Transactional
    @Override
    public Optional<ParkingLot> findById(Long parkingLotId) {
        log.info(console.classMsg(getClass().getSimpleName(),"get parking lot by id executed..."));
        return parkingLotRepository.findById(parkingLotId);
    }

    /**
     * Save parking lot in database method
     * Used for update status of parking lot
     *
     * @return - Parking lot which was saved in database
     */
    @Transactional
    @Override
    public ParkingLot save(ParkingLot parkingLot) {
        log.info(console.classMsg(getClass().getSimpleName(),"save parking lot in database executed..."));
        return parkingLotRepository.save(parkingLot);
    }

    @Transactional
    public long countAll() {
        log.info(console.classMsg(getClass().getSimpleName(),"count parking lots..."));
        return parkingLotRepository.count();
    }
}

