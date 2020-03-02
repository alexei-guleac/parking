package com.isd.parking.service;

import com.isd.parking.model.ParkingLot;
import com.isd.parking.model.StatisticsRecord;
import com.isd.parking.repository.StatisticsRepository;
import com.isd.parking.utils.ColorConsoleOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Statistics Service class for database repository
 * Contains methods for
 * getting all statistics records,
 * delete statistics records older than one week
 */
@Service
@Slf4j
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

    private final ColorConsoleOutput console;

    @Autowired
    public StatisticsService(StatisticsRepository statisticsRepository, ColorConsoleOutput console) {
        this.statisticsRepository = statisticsRepository;
        this.console = console;
    }

    /**
     * Get all statistics records from database method
     *
     * @return - Statistics records list
     */
    public List<StatisticsRecord> listAll() {
        log.info(console.classMsg("get statistics list executed..."));
        return statisticsRepository.findAll();
    }

    /**
     * Method deletes all statistics records older than one week
     */
    @Transactional
    public void deleteStatsOlderThanWeek() {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        java.sql.Date oneWeek = new java.sql.Date(cal.getTimeInMillis());

        log.info(console.classMsg("delete statistics older than one week executed..."));

        statisticsRepository.removeOlderThan(oneWeek);

    }

    /**
     * Method creates new statistics record
     *
     * @param parkingLot - input parking lot object
     */
    public void addStatisticsRecord(ParkingLot parkingLot) {
        StatisticsRecord statisticsRecord = StatisticsRecord.builder()
                .lotNumber(parkingLot.getNumber())
                .status(parkingLot.getStatus())
                .updatedAt(new Date(System.currentTimeMillis())).build();

        log.info(console.classMsg("Statistics record: " + ColorConsoleOutput.blTxt(String.valueOf(statisticsRecord))));
        log.info(console.classMsg("Service update statistics executed..."));

        save(statisticsRecord);
    }

    /**
     * Save statistics record in database method
     *
     * @return - StatisticsRecord which was saved in database
     */
    @Transactional
    public StatisticsRecord save(StatisticsRecord statisticsRecord) {
        log.info(console.classMsg("Service save statistics event executed..."));
        return statisticsRepository.save(statisticsRecord);
    }

    /**
     * Save statistics record in database method
     *
     * @param parkingLot - parking lot need to be saved in statistics record
     * @return - StatisticsRecord which was saved in database
     */
    @Transactional
    public StatisticsRecord save(ParkingLot parkingLot) {
        log.info(console.classMsg("Service save statistics event executed..."));

        StatisticsRecord statisticsRecord = StatisticsRecord.builder()
                .lotNumber(parkingLot.getNumber())
                .status(parkingLot.getStatus())
                .updatedAt(new Date(System.currentTimeMillis())).build();

        return statisticsRepository.save(statisticsRecord);
    }
}
