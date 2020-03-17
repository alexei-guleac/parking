package com.isd.parking.repository;

import com.isd.parking.models.ParkingLot;
import com.isd.parking.utils.ColorConsoleOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


/**
 * Local Java memory parking lots repository class
 */
@Repository
@Slf4j
public class ParkingLotLocalStorage {

    @Value("${parking.lots.number}")
    private String totalParkingLotsNumber;

    @Autowired
    private ColorConsoleOutput console;

    //Local in-memory storage of parking lots
    private final HashMap<Long, ParkingLot> parkingMap = new HashMap<>();

    public ParkingLotLocalStorage() {
    }

    /**
     * Get all parking lots method
     *
     * @return - Parking lots list
     */
    public synchronized List<ParkingLot> findAll() {
        log.info(console.classMsg(getClass().getSimpleName(), "get all parking lots local list executed..."));
        log.info(console.methodMsg(""));
        return new ArrayList<>(parkingMap.values());
    }

    /**
     * Get parking lot by id method
     *
     * @return - specified parking lot
     */
    public synchronized Optional<ParkingLot> findById(Long parkingLotId) {
        ParkingLot parkingLot = parkingMap.get(parkingLotId);
        return Optional.ofNullable(parkingLot);
    }

    /**
     * Save parking lot in local memory method
     * Used for update status of parking lot
     *
     * @return - Parking lot which was saved in database
     */
    public synchronized ParkingLot save(ParkingLot parkingLot) {
        return parkingMap.put(parkingLot.getId(), parkingLot);
    }

    public synchronized ParkingLot update(ParkingLot parkingLot) {
        if (parkingMap.containsValue(parkingLot)) {
            return parkingMap.put(parkingLot.getId(), parkingLot);
        }
        return null;
    }

    public synchronized ParkingLot delete(ParkingLot parkingLot) {
        if (parkingMap.containsValue(parkingLot)) {
            return parkingMap.remove(parkingLot.getId());
        }
        return null;
    }

    public synchronized long count() {
        return parkingMap.size();
    }
}
