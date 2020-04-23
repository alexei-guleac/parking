package com.isd.parking.controller.web;

import com.isd.parking.models.StatisticsRecord;
import com.isd.parking.service.implementations.StatisticsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.isd.parking.controller.ApiEndpoints.statistics;


/**
 * Provides methods for getting all statistics records stored in database
 */
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
    @GetMapping(statistics)
    public List<StatisticsRecord> getAllStats() {
        return statisticsService.listAll();
    }
}
