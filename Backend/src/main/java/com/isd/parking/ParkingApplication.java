package com.isd.parking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@ComponentScan(basePackages = {"com.isd.parking.*"})
@Profile("default")
@EnableJpaRepositories
@EnableScheduling
@PropertySources({
    @PropertySource("classpath:application.properties"),
    @PropertySource("classpath:postgresql-config.properties"),
})
@Slf4j
public class ParkingApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ParkingApplication.class);
        application.run(args);
    }

}
