package com.isd.parking.controller.frontapp;

import com.isd.parking.exception.ResourceNotFoundException;
import com.isd.parking.model.ParkingLot;
import com.isd.parking.model.enums.ParkingLotStatus;
import com.isd.parking.service.ParkingLotLocalService;
import com.isd.parking.service.StatisticsService;
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

import static com.isd.parking.controller.frontapp.RestApiEndpoints.parking;


/**
 * Parking lots controller
 * Provides methods for getting all parking lots stored in local Java memory and database
 */
@RestController
@Slf4j
public class ParkingLotController {


    /*private final ParkingLotService parkingLotService;*/

    private final ParkingLotLocalService parkingLotService;
    private final StatisticsService statisticsService;

    private final LdapContextSource contextSource;

    @Autowired
    public ParkingLotController(ParkingLotLocalService parkingLotService, StatisticsService statisticsService, LdapContextSource contextSource) {
        this.parkingLotService = parkingLotService;
        this.statisticsService = statisticsService;
        this.contextSource = contextSource;
    }

    /**
     * Parking lots get controller
     * Used to get all parking lots from the local Java memory
     *
     * @return Parking lots list
     */
    @GetMapping(parking)
    public List<ParkingLot> getAllParkingLots() {
        log.info("{Parking controller} get all parking lots");
        return parkingLotService.listAll();
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
        log.info("{Parking controller} get parking lot by id");
        ParkingLot parkingLot = parkingLotService.findById(parkingLotId)
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

        log.info("{Parking controller} Parking lot number in reservation request: " + parkingLotId);
        Optional<ParkingLot> parkingLotOptional = parkingLotService.findById(parkingLotId);
        AtomicBoolean hasErrors = new AtomicBoolean(false);

        //if lot with this number exists in database
        parkingLotOptional.ifPresent(parkingLot -> {
            log.info("Parking lot found in database: " + parkingLot);

            // if parking lot is already reserved
            if (parkingLot.getStatus() == ParkingLotStatus.RESERVED) {
                hasErrors.set(true);
            } else {
                parkingLot.setStatus(ParkingLotStatus.RESERVED);       //get enum value from string
                parkingLot.setUpdatedNow();
                log.info("Updated parking lot: " + parkingLot);

                //saving in local Java memory
                parkingLotService.save(parkingLot);
                //saving in database
                //parkingLotService.save(parkingLot);
                //save new statistics to database
                statisticsService.addStatisticsRecord(parkingLot);
            }
        });

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

        log.info("{Parking controller} Parking lot number in cancel reservation request: " + parkingLotId);
        Optional<ParkingLot> parkingLotOptional = parkingLotService.findById(parkingLotId);
        AtomicBoolean hasErrors = new AtomicBoolean(false);

        //if lot with this number exists in database
        parkingLotOptional.ifPresent(parkingLot -> {
            log.info("Parking lot found in database: " + parkingLot);

            // if parking lot is not reserved
            if (parkingLot.getStatus() != ParkingLotStatus.RESERVED) {
                hasErrors.set(true);
            } else {
                parkingLot.setStatus(ParkingLotStatus.FREE);       //get enum value from string
                parkingLot.setUpdatedNow();
                log.info("Updated parking lot: " + parkingLot);

                //saving in local Java memory
                parkingLotService.save(parkingLot);
                //saving in database
                //parkingLotService.save(parkingLot);
                //save new statistics to database
                statisticsService.addStatisticsRecord(parkingLot);
            }
        });

        return !hasErrors.get();
    }
}
