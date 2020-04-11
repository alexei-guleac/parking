package com.isd.parking;

import com.isd.parking.service.implementations.StatisticsServiceImpl;
import com.isd.parking.utils.ColorConsoleOutput;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import static org.mockito.Mockito.mock;

/**
 * Scheduling tasks test
 */
@EnableScheduling
@Configuration
@ConditionalOnProperty(name = "spring.enable.scheduling")
@Slf4j
public class ScheduleTest {

    private final ColorConsoleOutput console;

    @Autowired
    public ScheduleTest(ColorConsoleOutput console) {
        this.console = console;
    }

    /**
     * Sample scheduling tasks test
     */
    @Test
    @Scheduled(fixedDelay = 5000)
    public void scheduleFixedDelayTask() {
        StatisticsServiceImpl statisticsService = mock(StatisticsServiceImpl.class);

        log.info("Fixed delay task executing - " + System.currentTimeMillis() / 1000);

        statisticsService.deleteStatsOlderThanWeek();
    }
}
