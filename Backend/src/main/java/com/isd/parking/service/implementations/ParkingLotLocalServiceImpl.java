package com.isd.parking.service.implementations;

import com.isd.parking.models.ParkingLot;
import com.isd.parking.repository.ParkingLotLocalStorage;
import com.isd.parking.service.ParkingLotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.isd.parking.utils.ColorConsoleOutput.methodMsg;


/**
 * Parking Lot Local Service class for local Java memory repository
 * Contains methods for
 * - getting all parking lots,
 * - get parking lot by id,
 * - saving (in this case updating) parking lot
 */
@Service
@Slf4j
public class ParkingLotLocalServiceImpl implements ParkingLotService {

    private final ParkingLotLocalStorage parkingLotLocalStorage;

    @Autowired
    public ParkingLotLocalServiceImpl(ParkingLotLocalStorage parkingLotLocalStorage) {
        this.parkingLotLocalStorage = parkingLotLocalStorage;
    }

    /**
     * Get all parking lots from local Java memory method
     *
     * @return - Parking lots list
     */
    @Override
    public List<ParkingLot> findAll() {
        log.info(methodMsg("get all parking lots list executed..."));
        return parkingLotLocalStorage.findAll();
    }

    /**
     * Get parking lot by id from local Java memory method
     *
     * @return - specified parking lot
     */
    @Override
    public Optional<ParkingLot> findById(Long parkingLotId) {
        log.info(methodMsg("get parking lot by id executed..."));
        return parkingLotLocalStorage.findById(parkingLotId);
    }

    /**
     * Save parking lot in local Java memory method
     * Used for update status of parking lot
     *
     * @return - Parking lot which was saved in database
     */
    @Override
    public ParkingLot save(ParkingLot parkingLot) {
        log.info(methodMsg("save parking lot executed..."));
        return parkingLotLocalStorage.save(parkingLot);
    }

    /**
     * Count parking lots in local repository
     *
     * @return - parking lots total count
     */
    @Override
    public long countAll() {
        log.info(methodMsg("count parking lots..."));
        return parkingLotLocalStorage.count();
    }
}
