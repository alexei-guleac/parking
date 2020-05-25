package com.isd.parking.models.enums;

import java.util.List;
import java.util.Random;


/**
 * Permissible parking lot statuses enumeration
 */
public enum ParkingLotStatus {
    FREE,
    OCCUPIED,
    RESERVED,
    UNKNOWN;

    /**
     * Gets random parking lot status
     * Used for fake statistics generator
     */
    private static final List<ParkingLotStatus> VALUES = List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static ParkingLotStatus getRandomStatus()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
