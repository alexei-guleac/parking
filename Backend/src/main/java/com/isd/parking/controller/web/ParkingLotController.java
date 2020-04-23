package com.isd.parking.controller.web;

import com.isd.parking.exceptions.ResourceNotFoundException;
import com.isd.parking.models.ParkingLot;
import com.isd.parking.models.enums.ParkingLotStatus;
import com.isd.parking.service.implementations.ParkingLotLocalServiceImpl;
import com.isd.parking.service.implementations.StatisticsServiceImpl;
import com.isd.parking.storage.util.DataLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.isd.parking.controller.ApiEndpoints.*;
import static com.isd.parking.utils.ColorConsoleOutput.*;


/**
 * Provides methods for getting all parking lots stored in local Java memory
 * (optionally can be extended to get data from SQL database)
 */
@RestController
@Slf4j
public class ParkingLotController {

    private final ParkingLotLocalServiceImpl parkingLotLocalService;

    private final StatisticsServiceImpl statisticsService;

    private final DataLoader loader;

    @Autowired
    public ParkingLotController(ParkingLotLocalServiceImpl parkingLotLocalService,
                                StatisticsServiceImpl statisticsService,
                                DataLoader loader) {
        this.parkingLotLocalService = parkingLotLocalService;
        this.statisticsService = statisticsService;
        this.loader = loader;
    }

    /**
     * Used to get all parking lots from parking lots storage
     *
     * @return Parking lots list
     */
    @GetMapping(parking)
    public List<ParkingLot> getAllParkingLots() {
        log.info(methodMsg("get all parking lots"));
        return parkingLotLocalService.findAll();
    }

    /**
     * Used to get parking lot by its id from parking lots storage
     *
     * @return HTTP response with body of Parking lot if exists in storage else
     * @throws ResourceNotFoundException
     */
    @GetMapping(parking + "/{id}")
    public ResponseEntity<ParkingLot> getParkingLotById(@PathVariable("id") Long parkingLotId)
        throws ResourceNotFoundException {
        log.info(methodMsg("get parking lot by id"));

        ParkingLot parkingLot = parkingLotLocalService.findById(parkingLotId)
            .orElseThrow(() -> new ResourceNotFoundException("Parking Lot not found for this id :: " + parkingLotId));

        return ResponseEntity.ok().body(parkingLot);
    }

    /**
     * Used to reserve parking lot
     *
     * @param parkingLotId - id of target parking lot
     * @return - status of parking lot reservation
     */
    @RequestMapping(reserve + "/{id}")
    public boolean reservation(@PathVariable("id") Long parkingLotId) {
        log.info(methodMsg("Parking lot number in reservation request: " + blTxt(String.valueOf(parkingLotId))));

        Optional<ParkingLot> parkingLotOptional = parkingLotLocalService.findById(parkingLotId);
        AtomicBoolean hasErrors = new AtomicBoolean(false);

        // if lot with this number exists in database
        parkingLotOptional.ifPresent(parkingLot -> {
            // if parking lot is already reserved
            if (parkingLot.getStatus() == ParkingLotStatus.RESERVED) {
                hasErrors.set(true);
            } else {
                switchStatus(parkingLot, ParkingLotStatus.RESERVED);
            }
        });

        // show all parking lots from local Java memory to verify change
        loader.fetchParkingLots(parkingLotLocalService, redTxt(" from LOCAL Java memory:"));

        return !hasErrors.get();
    }

    /**
     * Used to cancel reservation status of parking lot
     *
     * @param parkingLotId - id of parking lot
     * @return - status of parking lot reservation
     */
    @RequestMapping(unreserve + "/{id}")
    public boolean cancelReservation(@PathVariable("id") Long parkingLotId) {

        log.info(methodMsg("Parking lot number in cancel reservation request: " + blTxt(String.valueOf(parkingLotId))));
        Optional<ParkingLot> parkingLotOptional = parkingLotLocalService.findById(parkingLotId);
        AtomicBoolean hasErrors = new AtomicBoolean(false);

        //if lot with this number exists in database
        parkingLotOptional.ifPresent(parkingLot -> {
            if (parkingLot.getStatus() != ParkingLotStatus.RESERVED) {
                hasErrors.set(true);
            } else {
                // if parking lot is not reserved
                switchStatus(parkingLot, ParkingLotStatus.FREE);
            }
        });

        return !hasErrors.get();
    }

    /**
     * Set status of parking lot
     *
     * @param parkingLot - target parking lot
     * @param newStatus  - target status
     */
    private void switchStatus(ParkingLot parkingLot, ParkingLotStatus newStatus) {
        parkingLot.setStatus(newStatus);       //get enum value from string
        parkingLot.setUpdatedNow();
        log.info(methodMsg("updated parking lot: " + blTxt(String.valueOf(parkingLot))));

        //saving in local Java memory
        parkingLotLocalService.save(parkingLot);
        //save new statistics record to database
        statisticsService.save(parkingLot);
    }
}
