package com.isd.parking.repository;

import com.isd.parking.models.ParkingLot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.isd.parking.utils.ColorConsoleOutput.methodMsg;


/**
 * Local Java memory parking lots repository class
 */
@Repository
@Slf4j
public class ParkingLotLocalStorage {

    @Value("${parking.lots.number}")
    private String totalParkingLotsNumber;

    //Local in-memory storage of parking lots
    private final HashMap<Long, ParkingLot> parkingMap = new HashMap<>();

    /**
     * Get all parking lots method
     *
     * @return Parking lots list
     */
    public synchronized List<ParkingLot> findAll() {
        log.info(methodMsg("get all parking lots local list executed..."));
        return new ArrayList<>(parkingMap.values());
    }

    /**
     * Get parking lot by id method
     *
     * @return specified parking lot
     */
    public synchronized Optional<ParkingLot> findById(Long parkingLotId) {
        ParkingLot parkingLot = parkingMap.get(parkingLotId);
        return Optional.ofNullable(parkingLot);
    }

    /**
     * Save parking lot in local memory method
     * Used for create new parking lot
     *
     * @return Parking lot which was saved in database
     */
    public synchronized ParkingLot save(ParkingLot parkingLot) {
        return parkingMap.put(parkingLot.getId(), parkingLot);
    }

    /**
     * Update parking lot in local memory method
     * Used for update status of parking lot
     *
     * @return Parking lot which was updated in database
     */
    public synchronized ParkingLot update(ParkingLot parkingLot) {
        if (parkingMap.containsValue(parkingLot)) {
            return parkingMap.put(parkingLot.getId(), parkingLot);
        }
        return null;
    }

    /**
     * Delete parking lot from local memory method
     *
     * @return Parking lot which was saved in database
     */
    public synchronized ParkingLot delete(ParkingLot parkingLot) {
        if (parkingMap.containsValue(parkingLot)) {
            return parkingMap.remove(parkingLot.getId());
        }
        return null;
    }

    /**
     * Get parking lots count from storage
     *
     * @return parking lots count
     */
    public synchronized long count() {
        return parkingMap.size();
    }
}
