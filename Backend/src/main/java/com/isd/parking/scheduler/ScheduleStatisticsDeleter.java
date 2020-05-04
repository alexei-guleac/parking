package com.isd.parking.scheduler;

import com.isd.parking.services.implementations.StatisticsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import static com.isd.parking.utilities.ColorConsoleOutput.methodMsg;


/**
 * Schedule statistics deleter
 * Contains method for delete stats in background
 */
@EnableScheduling
@Configuration
@ConditionalOnProperty(name = "spring.enable.scheduling")
@Slf4j
public class ScheduleStatisticsDeleter {

    private final StatisticsServiceImpl statisticsService;

    @Autowired
    public ScheduleStatisticsDeleter(StatisticsServiceImpl statisticsService) {
        this.statisticsService = statisticsService;
    }

    /**
     * Deletes statistics older than one week
     *
     * Cron:
     * 0 0 1 * * * =>
     * second, minute, hour, day of month, month, day(s) of week
     * (*) means match any
     *  X means "every X"
     * */
    @Scheduled(cron = "0 0 1 * * *")            //task will be executed at 13:00 every day
    public void scheduleTaskDeleteStats() {
        log.info(methodMsg("Delete stats schedule job executing..."));
        statisticsService.deleteStatsOlderThanWeek();
    }

    /**
     * Configuration method used for setting up ScheduledExecutorService's pool size
     *
     * @return - ThreadPoolTaskScheduler bean
     */
    //for resolve conflict launching schedule in main thread
    @Bean
    public @NotNull ThreadPoolTaskScheduler taskScheduler() {
        @NotNull ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

        scheduler.setPoolSize(4);               //change this for increase amount of threads
        scheduler.setThreadNamePrefix("scheduled-task-");
        scheduler.setDaemon(true);

        return scheduler;
    }
}
