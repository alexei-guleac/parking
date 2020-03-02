package com.isd.parking.controller.arduino;

import com.isd.parking.model.ParkingLot;
import com.isd.parking.model.enums.ParkingLotStatus;
import com.isd.parking.service.ParkingLotLocalService;
import com.isd.parking.service.ParkingLotService;
import com.isd.parking.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

import static com.isd.parking.controller.frontapp.RestApiEndpoints.arduinoApi;


/**
 * Arduino controller
 * Reserve class for network connection over HTTP protocol
 * Contains methods for updating database stored parking lots from Arduino board
 */
@RestController
@Slf4j
public class ArduinoController {

    private final ParkingLotService parkingLotService;

    private final ParkingLotLocalService parkingLotLocalService;

    private final StatisticsService statisticsService;

    @Autowired
    public ArduinoController(ParkingLotService parkingLotService, ParkingLotLocalService parkingLotLocalService, StatisticsService statisticsService) {
        this.parkingLotService = parkingLotService;
        this.parkingLotLocalService = parkingLotLocalService;
        this.statisticsService = statisticsService;
    }

    /**
     * Parking lot status updating controller for PUT request
     * Used to update status of an parking lot in the database
     *
     * @param parkingLot - parking lot object, contains all necessary data
     * @return HttpStatus.OK
     */
    @PutMapping(arduinoApi)
    @ResponseStatus(HttpStatus.OK)
    public void updateParkingLot(@Valid @RequestBody ParkingLot parkingLot) {

        Optional<ParkingLot> parkingLotOptional = parkingLotService.findById(parkingLot.getId());

        parkingLotOptional.ifPresent(updatingParkingLot -> {

            updatingParkingLot.setStatus(parkingLot.getStatus());
            updatingParkingLot.setUpdatedNow();

            parkingLotService.save(updatingParkingLot);
            parkingLotLocalService.save(updatingParkingLot);

            statisticsService.save(parkingLot);
        });
    }

     /*
    alternative fallback method for updating data using id and status
     */

    /**
     * Parking lot status updating controller for PUT request
     * Used to update status of an parking lot in the database by parking lot id
     *
     * @param id               - parking lot id
     * @param parkingLotStatus - parking lot status
     */
    @PutMapping(arduinoApi + "/update")
    @ResponseStatus(HttpStatus.OK)
    public void updateParkingLotById(@RequestParam(value = "id") Long id,
                                     @RequestParam(value = "status") ParkingLotStatus parkingLotStatus) {

        Optional<ParkingLot> parkingLotOptional = parkingLotService.findById(id);

        parkingLotOptional.ifPresent(parkingLot -> {

            parkingLot.setStatus(parkingLotStatus);
            parkingLot.setUpdatedNow();

            parkingLotService.save(parkingLot);
            parkingLotLocalService.save(parkingLot);
            statisticsService.save(parkingLot);
        });
    }
}
