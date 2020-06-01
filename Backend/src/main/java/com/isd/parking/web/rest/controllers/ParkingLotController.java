package com.isd.parking.web.rest.controllers;

import com.isd.parking.config.SwaggerConfig;
import com.isd.parking.config.locale.SmartLocaleResolver;
import com.isd.parking.exceptions.ResourceNotFoundException;
import com.isd.parking.models.enums.ParkingLotStatus;
import com.isd.parking.models.subjects.ParkingLot;
import com.isd.parking.services.implementations.ParkingLotLocalServiceImpl;
import com.isd.parking.services.implementations.StatisticsServiceImpl;
import com.isd.parking.storage.util.DataLoader;
import com.isd.parking.web.rest.ApiEndpoints;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.isd.parking.utilities.ColorConsoleOutput.*;


/**
 * Provides methods for getting all parking lots stored in local Java memory
 * (optionally can be extended to get data from SQL database)
 * as well as parking lot reservation and parking lot cancel reservation by administrator
 */
@Api(value = "ParkingLot Controller",
    description = "Operations pertaining to parking lot in database or Java memory")
@RestController
@Slf4j
public class ParkingLotController {

    private final ParkingLotLocalServiceImpl parkingLotLocalService;

    private final StatisticsServiceImpl statisticsService;

    private final DataLoader loader;

    private final ResourceBundleMessageSource messageSource;

    private final SmartLocaleResolver localeResolver;

    @Autowired
    public ParkingLotController(ParkingLotLocalServiceImpl parkingLotLocalService,
                                StatisticsServiceImpl statisticsService,
                                DataLoader loader,
                                ResourceBundleMessageSource messageSource,
                                SmartLocaleResolver localeResolver) {
        this.parkingLotLocalService = parkingLotLocalService;
        this.statisticsService = statisticsService;
        this.loader = loader;
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
    }

    /**
     * Used to get all parking lots from parking lots storage
     *
     * @return Parking lots list
     */
    @ApiOperation(value = "${ParkingLotController.getAllParkingLots.value}",
        response = ParkingLot.class,
        responseContainer = "List",
        notes = "${ParkingLotController.getAllParkingLots.notes}")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success|OK")
    })
    @GetMapping(ApiEndpoints.parking)
    public List<ParkingLot> getAllParkingLots() {
        return parkingLotLocalService.findAll();
    }

    /**
     * Used to get parking lot by its id from parking lots storage
     *
     * @return HTTP response with body of Parking lot if exists in storage else
     * @throws ResourceNotFoundException - if lot not found
     */
    @ApiOperation(value = "${ParkingLotController.getParkingLotById.value}",
        response = ParkingLot.class,
        responseContainer = "ResponseEntity",
        notes = "${ParkingLotController.getParkingLotById.notes}")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success|OK"),
        @ApiResponse(code = 404, message = "Parking Lot not found for this id :: specified id"),
        @ApiResponse(code = 500, message = "Server error")
    })
    @ApiImplicitParams(
        @ApiImplicitParam(name = "parkingLotId",
            value = "${ParkingLotController.getParkingLotById.parkingLotId}",
            required = true, dataType = "Long")
    )
    @GetMapping(ApiEndpoints.parking + "/{id}")
    public @NotNull ResponseEntity<ParkingLot> getParkingLotById(@PathVariable("id") Long parkingLotId,
                                                                 @RequestHeader Map<String, String> headers)
        throws ResourceNotFoundException {

        final Locale locale = localeResolver.resolveLocale(headers);

        ParkingLot parkingLot = parkingLotLocalService.findById(parkingLotId)
            .orElseThrow(() -> new ResourceNotFoundException(
                messageSource.getMessage("ParkingLotController.not-found", null, locale) + " " + parkingLotId));

        return ResponseEntity.ok().body(parkingLot);
    }

    /**
     * Used to reserve parking lot
     *
     * @param parkingLotId - id of target parking lot
     * @return - status of parking lot reservation
     */
    @ApiOperation(value = "${ParkingLotController.reservation.value}",
        response = Boolean.class,
        notes = "${ParkingLotController.reservation.notes}",
        authorizations = {@Authorization(value = SwaggerConfig.TOKEN_TYPE)})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success|OK"),
        @ApiResponse(code = 500, message = "Server error")
    })
    @ApiImplicitParams(
        @ApiImplicitParam(name = "parkingLotId",
            value = "${ParkingLotController.reservation.parkingLotId}",
            required = true, dataType = "Long")
    )
    @RequestMapping(ApiEndpoints.reserve + "/{id}")
    public boolean reservation(@PathVariable("id") Long parkingLotId) {
        log.info(methodMsg("Parking lot number in reservation request: "
            + blTxt(String.valueOf(parkingLotId))));

        Optional<ParkingLot> parkingLotOptional = parkingLotLocalService.findById(parkingLotId);
        @NotNull AtomicBoolean hasErrors = new AtomicBoolean(false);

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
    @ApiOperation(value = "${ParkingLotController.cancelReservation.value}",
        response = Boolean.class,
        notes = "${ParkingLotController.cancelReservation.notes}",
        authorizations = {@Authorization(value = SwaggerConfig.TOKEN_TYPE)})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success|OK"),
        @ApiResponse(code = 500, message = "Server error")
    })
    @ApiImplicitParams(
        @ApiImplicitParam(name = "parkingLotId",
            value = "${ParkingLotController.cancelReservation.parkingLotId}",
            required = true, dataType = "Long")
    )
    @RequestMapping(ApiEndpoints.unreserve + "/{id}")
    public boolean cancelReservation(@PathVariable("id") Long parkingLotId) {

        log.info(methodMsg("Parking lot number in cancel reservation request: "
            + blTxt(String.valueOf(parkingLotId))));
        Optional<ParkingLot> parkingLotOptional = parkingLotLocalService.findById(parkingLotId);
        @NotNull AtomicBoolean hasErrors = new AtomicBoolean(false);

        // if lot with this number exists in database
        parkingLotOptional.ifPresent(parkingLot -> {
            // unreserve only if lot has not already reserved
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
    private void switchStatus(@NotNull ParkingLot parkingLot, ParkingLotStatus newStatus) {
        parkingLot.setStatus(newStatus);       //get enum value from string
        parkingLot.setUpdatedNow();
        log.info(methodMsg("updated parking lot: " + blTxt(String.valueOf(parkingLot))));

        //saving in local Java memory
        parkingLotLocalService.save(parkingLot);
        //save new statistics record to database
        statisticsService.save(parkingLot);
    }
}
