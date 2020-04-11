package com.isd.parking.scheduler;

import com.isd.parking.service.implementations.StatisticsServiceImpl;
import com.isd.parking.utils.ColorConsoleOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;


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

    private final ColorConsoleOutput console;

    @Autowired
    public ScheduleStatisticsDeleter(StatisticsServiceImpl statisticsService,
                                     ColorConsoleOutput console) {
        this.statisticsService = statisticsService;
        this.console = console;
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
        log.info(console.classMsg(getClass().getSimpleName(),"Delete stats schedule job executing..."));
        statisticsService.deleteStatsOlderThanWeek();
    }

    /**
     * Configuration method used for setting up ScheduledExecutorService's pool size
     *
     * @return - ThreadPoolTaskScheduler bean
     */
    //for resolve conflict launching schedule in main thread
    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

        scheduler.setPoolSize(4);               //change this for increase amount of threads
        scheduler.setThreadNamePrefix("scheduled-task-");
        scheduler.setDaemon(true);

        return scheduler;
    }
}
