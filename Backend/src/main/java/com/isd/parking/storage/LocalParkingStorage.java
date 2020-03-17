package com.isd.parking.storage;

import com.isd.parking.models.ParkingLot;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;


/**
 * Local Java memory parking lots storage class
 */
public class LocalParkingStorage {

    @Autowired
    private final HashMap<Long, ParkingLot> parkingMap;

    public LocalParkingStorage(HashMap<Long, ParkingLot> parkingMap) {
        this.parkingMap = parkingMap;
    }
}
