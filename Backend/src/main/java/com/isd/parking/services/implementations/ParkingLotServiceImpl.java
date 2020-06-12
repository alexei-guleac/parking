package com.isd.parking.services.implementations;

import com.isd.parking.models.enums.ParkingLotStatus;
import com.isd.parking.models.subjects.ParkingLot;
import com.isd.parking.repository.ParkingLotLocalStorage;
import com.isd.parking.services.ParkingLotService;
import com.isd.parking.storage.util.DataLoader;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.isd.parking.utilities.ColorConsoleOutput.*;


/**
 * Parking Lot Service class for database repository
 * Contains methods for
 * - getting all parking lots,
 * - get parking lot by id,
 * - saving (in this case updating) parking lot
 */
@Service
@Slf4j
public class ParkingLotServiceImpl implements ParkingLotService {

    // This dependency may need to record data directly to SQL database
    // private final ParkingLotRepository parkingLotStorage;

    private final ParkingLotLocalStorage parkingLotStorage;

    private final StatisticsServiceImpl statisticsService;

    private final DataLoader loader;

    @Autowired
    public ParkingLotServiceImpl(ParkingLotLocalStorage parkingLotStorage,
                                 StatisticsServiceImpl statisticsService,
                                 DataLoader loader) {
        this.parkingLotStorage = parkingLotStorage;
        this.statisticsService = statisticsService;
        this.loader = loader;
    }

    /**
     * Get all parking lots from database method
     *
     * @return - Parking lots list
     */
    @Transactional
    @Override
    public @NotNull List<ParkingLot> findAll() {
        // log.info(methodMsg("get all parking lots list executed..."));
        return parkingLotStorage.findAll();
    }

    /**
     * Get parking lot by id from database method
     *
     * @return - specified parking lot
     */
    @Transactional
    @Override
    public @NotNull Optional<ParkingLot> findById(@NotNull Long parkingLotId) {
        // log.info(methodMsg("get parking lot by id executed..."));
        return parkingLotStorage.findById(parkingLotId);
    }

    /**
     * Get parking lot by number from database method
     *
     * @return - specified parking lot
     */
    @Transactional
    @Override
    public Optional<ParkingLot> findByLotNumber(Integer parkingLotNumber) {
        // log.info(methodMsg("get parking lot by number executed..."));
        return parkingLotStorage.findByNumber(parkingLotNumber);
    }

    /**
     * Save parking lot in database method
     * Used for update status of parking lot
     *
     * @return - Parking lot which was saved in database
     */
    @Transactional
    @Override
    public @NotNull ParkingLot save(@NotNull ParkingLot parkingLot) {
        log.info(methodMsg("save parking lot in database executed..."));
        return Objects.requireNonNull(parkingLotStorage.save(parkingLot));
    }

    /**
     * Count parking lots in SQL database
     *
     * @return - parking lots total count
     */
    @Transactional
    @Override
    public long countAll() {
        log.info(methodMsg("count parking lots..."));
        return parkingLotStorage.count();
    }

    /**
     * Used to reserve parking lot
     *
     * @param parkingLotId - id of target parking lot
     * @return - status of parking lot reservation
     */
    public boolean reservation(Long parkingLotId) {
        log.info(methodMsg("Parking lot number in reservation request: "
            + blTxt(String.valueOf(parkingLotId))));

        Optional<ParkingLot> parkingLotOptional = this.findById(parkingLotId);
        @NotNull AtomicBoolean reserved = new AtomicBoolean(true);

        // if lot with this number exists in database
        parkingLotOptional.ifPresent(parkingLot -> {
            // if parking lot is already reserved
            if (parkingLot.getStatus() == ParkingLotStatus.RESERVED) {
                reserved.set(false);
            } else {
                switchStatus(parkingLot, ParkingLotStatus.RESERVED);
            }
        });

        // show all parking lots from local Java memory to verify change
        loader.fetchParkingLots(parkingLotStorage, redTxt(" from LOCAL Java memory:"));

        return reserved.get();
    }

    /**
     * Used to cancel reservation status of parking lot
     *
     * @param parkingLotId - id of parking lot
     * @return - status of parking lot reservation
     */
    public boolean cancelReservation(Long parkingLotId) {

        log.info(methodMsg("Parking lot number in cancel reservation request: "
            + blTxt(String.valueOf(parkingLotId))));
        Optional<ParkingLot> parkingLotOptional = this.findById(parkingLotId);
        @NotNull AtomicBoolean cancelled = new AtomicBoolean(true);

        // if lot with this number exists in database
        parkingLotOptional.ifPresent(parkingLot -> {
            // unreserve only if lot has not already reserved
            if (parkingLot.getStatus() != ParkingLotStatus.RESERVED) {
                cancelled.set(false);
            } else {
                // if parking lot is not reserved
                switchStatus(parkingLot, ParkingLotStatus.FREE);
            }
        });

        return cancelled.get();
    }

    /**
     * Set status of parking lot
     *
     * @param parkingLot - target parking lot
     * @param newStatus  - target status
     */
    private void switchStatus(@NotNull ParkingLot parkingLot, ParkingLotStatus newStatus) {
        parkingLot.setStatus(newStatus);       //get enum value from string
        parkingLot.setUpdatedNow();
        log.info(methodMsg("updated parking lot: " + blTxt(String.valueOf(parkingLot))));

        //saving in storage
        this.save(parkingLot);
        //save new statistics record to database
        statisticsService.save(parkingLot);
    }
}
