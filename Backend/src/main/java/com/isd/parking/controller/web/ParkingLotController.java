package com.isd.parking.controller.web;

import com.isd.parking.exceptions.ResourceNotFoundException;
import com.isd.parking.model.ParkingLot;
import com.isd.parking.model.enums.ParkingLotStatus;
import com.isd.parking.service.ParkingLotLocalService;
import com.isd.parking.service.StatisticsService;
import com.isd.parking.storage.util.DataLoader;
import com.isd.parking.utils.ColorConsoleOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.isd.parking.controller.web.RestApiEndpoints.parking;
import static com.isd.parking.utils.ColorConsoleOutput.blTxt;
import static com.isd.parking.utils.ColorConsoleOutput.redTxt;


/**
 * Parking lots controller
 * Provides methods for getting all parking lots stored in local Java memory and database
 */
@RestController
@Slf4j
public class ParkingLotController {

    /*private final ParkingLotDBService parkingLotDBService;*/

    private final ParkingLotLocalService parkingLotLocalService;

    private final StatisticsService statisticsService;

    private final LdapContextSource contextSource;

    private final ColorConsoleOutput console;

    private final DataLoader loader;

    @Autowired
    public ParkingLotController(ParkingLotLocalService parkingLotLocalService, StatisticsService statisticsService, LdapContextSource contextSource, ColorConsoleOutput console, DataLoader loader) {
        this.parkingLotLocalService = parkingLotLocalService;
        this.statisticsService = statisticsService;
        this.contextSource = contextSource;
        this.console = console;
        this.loader = loader;
    }

    /**
     * Parking lots get controller
     * Used to get all parking lots from the local Java memory
     *
     * @return Parking lots list
     */
    @GetMapping(parking)
    public List<ParkingLot> getAllParkingLots() {
        log.info(console.classMsg(getClass().getSimpleName(), "get all parking lots"));

        return parkingLotLocalService.findAll();
    }

    /**
     * Parking lots get by id controller
     * Used to get parking lot by its id from the local Java memory
     *
     * @return ResponseEntity.OK with body of Parking lot if exists in storage else
     * @throw ResourceNotFoundException
     */
    @GetMapping(parking + "/{id}")
    public ResponseEntity<ParkingLot> getParkingLotById(@PathVariable("id") Long parkingLotId)
            throws ResourceNotFoundException {
        log.info(console.classMsg(getClass().getSimpleName(), "get parking lot by id"));

        ParkingLot parkingLot = parkingLotLocalService.findById(parkingLotId)
                .orElseThrow(() -> new ResourceNotFoundException("Parking Lot not found for this id :: " + parkingLotId));

        return ResponseEntity.ok().body(parkingLot);
    }

    /**
     * Parking lot reservation controller
     * Used to reservate parking lot
     *
     * @param parkingLotId - id of parking lot
     * @return - success status of parking lot reservation
     */
    @RequestMapping("/reserve/{id}")
    public boolean reservation(@PathVariable("id") Long parkingLotId) {
        log.info(console.classMsg(getClass().getSimpleName(), "Parking lot number in reservation request: ") + blTxt(String.valueOf(parkingLotId)));

        Optional<ParkingLot> parkingLotOptional = parkingLotLocalService.findById(parkingLotId);
        AtomicBoolean hasErrors = new AtomicBoolean(false);

        //if lot with this number exists in database
        parkingLotOptional.ifPresent(parkingLot -> {
            log.info(console.classMsg(getClass().getSimpleName(), "Parking lot found in database: ") + blTxt(String.valueOf(parkingLot)));

            // if parking lot is already reserved
            if (parkingLot.getStatus() == ParkingLotStatus.RESERVED) {
                hasErrors.set(true);
            } else {
                switchStatus(hasErrors, parkingLot, ParkingLotStatus.RESERVED);
            }
        });

        // show all parking lots from local Java memory
        loader.fetchParkingLots(parkingLotLocalService, redTxt(" from LOCAL Java memory:"));

        return !hasErrors.get();
    }

    /**
     * Parking lot reset reservation controller
     * Used to cancel reservation status of parking lot
     *
     * @param parkingLotId - id of parking lot
     * @return - success status of parking lot reservation
     */
    @RequestMapping("/unreserve/{id}")
    public boolean cancelReservation(@PathVariable("id") Long parkingLotId) {

        log.info(console.classMsg(getClass().getSimpleName(), "Parking lot number in cancel reservation request: ") + blTxt(String.valueOf(parkingLotId)));
        Optional<ParkingLot> parkingLotOptional = parkingLotLocalService.findById(parkingLotId);
        AtomicBoolean hasErrors = new AtomicBoolean(false);

        //if lot with this number exists in database
        parkingLotOptional.ifPresent(parkingLot -> {
            log.info(console.classMsg(getClass().getSimpleName(), "Parking lot found in database: ") + blTxt(String.valueOf(parkingLot)));

            if (parkingLot.getStatus() != ParkingLotStatus.RESERVED) {
                hasErrors.set(true);
            } else {
                // if parking lot is not reserved
                switchStatus(hasErrors, parkingLot, ParkingLotStatus.FREE);
            }
        });

        return !hasErrors.get();
    }

    private void switchStatus(AtomicBoolean hasErrors, ParkingLot parkingLot, ParkingLotStatus newStatus) {
        parkingLot.setStatus(newStatus);       //get enum value from string
        parkingLot.setUpdatedNow();
        log.info(console.classMsg(getClass().getSimpleName(), "updated parking lot: ") + blTxt(String.valueOf(parkingLot)));

        //saving in local Java memory
        parkingLotLocalService.save(parkingLot);
        //save new statistics record to database
        statisticsService.addStatisticsRecord(parkingLot);
    }
}
