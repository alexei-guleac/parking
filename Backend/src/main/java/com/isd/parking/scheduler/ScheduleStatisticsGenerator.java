package com.isd.parking.scheduler;

import com.isd.parking.models.enums.ParkingLotStatus;
import com.isd.parking.models.subjects.ParkingLot;
import com.isd.parking.services.implementations.ParkingLotServiceImpl;
import com.isd.parking.services.implementations.StatisticsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.isd.parking.utilities.ColorConsoleOutput.methodMsg;


/**
 * Schedule fake statistics generator
 * Contains method for generate statistics records in background
 */
@EnableScheduling
@Configuration
@ConditionalOnProperty(name = "spring.enable.scheduling")
@Slf4j
public class ScheduleStatisticsGenerator {

    @Value("${scheduler.generate-stats.enabled}")
    private boolean generatorEnabled;

    private final ParkingLotServiceImpl parkingLotService;

    private final StatisticsServiceImpl statisticsService;

    @Autowired
    public ScheduleStatisticsGenerator(ParkingLotServiceImpl parkingLotService,
                                       StatisticsServiceImpl statisticsService) {
        this.parkingLotService = parkingLotService;
        this.statisticsService = statisticsService;
    }

    /**
     * Every day at 9:30 AM generate 100 new statistics records from last day 9-30 -- 18-30 period
     */
    @Scheduled(cron = "0 10 9 * * ?")
    public void generateStats() {

        if (generatorEnabled) {
            log.info(methodMsg("Generate new stats schedule job executing..."));

            final List<ParkingLot> parkingLots = parkingLotService.findAll();
            final List<Integer> parkingLotsNumbers = parkingLots.stream()
                .map(ParkingLot::getNumber)
                .collect(Collectors.toList());

            Integer randomLotNum;
            ParkingLotStatus randomStatus;

            // create new records
            for (int i = 0; i < 100; i++) {
                // get random lot number from stored lots
                randomLotNum = parkingLotsNumbers.get(new Random().nextInt(parkingLotsNumbers.size()));
                randomStatus = ParkingLotStatus.getRandomStatus();

                // get random date from last day 9-30 -- 18-30 period
                final LocalDateTime now = LocalDateTime.now();
                LocalDateTime ldtStartDate = now.minusDays(1);
                LocalDateTime ldtEndDate = ldtStartDate.plusHours(9);

                final ZoneId zone = ZoneId.systemDefault();
                Date startDate = Date.from(ldtStartDate.atZone(zone).toInstant());
                Date endDate = Date.from(ldtEndDate.atZone(zone).toInstant());

                Date randomDate = new Date(ThreadLocalRandom.current()
                    .nextLong(startDate.getTime(), endDate.getTime()));

                // save new record
                final ParkingLot parkingLot = ParkingLot.builder()
                    .id(1L)
                    .number(randomLotNum)
                    .status(randomStatus)
                    .updatedAt(randomDate)
                    .build();
                statisticsService.save(parkingLot);
            }
        }
    }
}
