package com.isd.parking.web.rest.controllers;

import com.isd.parking.config.SwaggerConfig;
import com.isd.parking.models.subjects.StatisticsRecord;
import com.isd.parking.services.implementations.StatisticsServiceImpl;
import com.isd.parking.web.rest.ApiEndpoints;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Provides methods for getting all statistics records stored in database
 */
@Api(value = "Statistics Controller",
    description = "Operations pertaining to parking lot usage statistics in database")
@RestController
@Slf4j
public class StatisticsController {

    private final StatisticsServiceImpl statisticsService;

    @Autowired
    public StatisticsController(StatisticsServiceImpl statisticsService) {
        this.statisticsService = statisticsService;
    }

    /**
     * Used to get all statistics records from database
     *
     * @return Statistics records list
     */
    @ApiOperation(value = "${StatisticsController.getAllStatistics.value}",
        response = StatisticsRecord.class,
        responseContainer = "List",
        notes = "${StatisticsController.getAllStatistics.notes}",
        authorizations = {@Authorization(value = SwaggerConfig.TOKEN_TYPE)})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success|OK")
    })
    @GetMapping(ApiEndpoints.statistics)
    public List<StatisticsRecord> getAllStatistics() {
        return statisticsService.listAll();
    }

    /**
     * Used to get statistics records from database by parking lot number
     *
     * @return Statistics records list
     */
    @ApiOperation(value = "${StatisticsController.getStatisticsByLotNumber.value}",
        response = StatisticsRecord.class,
        responseContainer = "List",
        notes = "${StatisticsController.getStatisticsByLotNumber.notes}",
        authorizations = {@Authorization(value = SwaggerConfig.TOKEN_TYPE)})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success|OK"),
        @ApiResponse(code = 500, message = "Server error")
    })
    @ApiImplicitParams(
        @ApiImplicitParam(name = "lotNumber",
            value = "${StatisticsController.getStatisticsByLotNumber.lotNumber}",
            required = true, dataType = "String")
    )
    @GetMapping(ApiEndpoints.statisticsByLot + "/{lotNumber}")
    public List<StatisticsRecord> getStatisticsByLotNumber(@PathVariable String lotNumber) {
        return statisticsService.findByLotNumber(Integer.parseInt(lotNumber));
    }
}
