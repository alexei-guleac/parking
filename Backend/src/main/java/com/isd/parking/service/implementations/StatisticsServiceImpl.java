package com.isd.parking.service.implementations;

import com.isd.parking.models.ParkingLot;
import com.isd.parking.models.StatisticsRecord;
import com.isd.parking.repository.StatisticsRepository;
import com.isd.parking.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.isd.parking.utils.ColorConsoleOutput.methodMsg;


/**
 * Statistics Service class for database repository
 * Contains methods for
 * getting all statistics records,
 * delete statistics records older than one week
 */
@Service
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsRepository statisticsRepository;

    @Autowired
    public StatisticsServiceImpl(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    /**
     * Get all statistics records from database method
     *
     * @return - statistics records list
     */
    @Override
    public List<StatisticsRecord> listAll() {
        log.info(methodMsg("get statistics list executed..."));
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
        log.info(methodMsg("delete statistics older than one week executed..."));

        statisticsRepository.removeOlderThan(oneWeek);
    }

    /**
     * Save statistics record in database method
     *
     * @return - statistics record which was saved in database
     */
    @Transactional
    @Override
    public StatisticsRecord save(StatisticsRecord statisticsRecord) {
        log.info(methodMsg("Service save statistics event executed..."));
        return statisticsRepository.save(statisticsRecord);
    }

    /**
     * Save statistics record in database method
     *
     * @param parkingLot - parking lot need to be saved in statistics record
     * @return - statistics record which was saved in database
     */
    @Transactional
    @Override
    public StatisticsRecord save(ParkingLot parkingLot) {
        log.info(methodMsg("Service save statistics event executed..."));

        StatisticsRecord statisticsRecord = StatisticsRecord.builder()
            .lotNumber(parkingLot.getNumber())
            .status(parkingLot.getStatus())
            .updatedAt(new Date(System.currentTimeMillis())).build();

        return statisticsRepository.save(statisticsRecord);
    }
}
