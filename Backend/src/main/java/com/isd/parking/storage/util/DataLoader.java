package com.isd.parking.storage.util;

import com.isd.parking.model.ParkingLot;
import com.isd.parking.model.enums.ParkingLotStatus;
import com.isd.parking.service.ParkingLotLocalService;
import com.isd.parking.service.ParkingLotService;
import com.isd.parking.utils.ColorConsoleOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

import static com.isd.parking.utils.ColorConsoleOutput.*;


/**
 * Utility class
 * Fills the database and local Java memory storage with initial data
 */
@Component
@Slf4j
public class DataLoader implements ApplicationRunner {

    @Value("${parking.lots.number}")
    private String totalParkingLotsNumber;

    private final ParkingLotService parkingLotService;

    private final ParkingLotLocalService parkingLotLocalService;

    private final ColorConsoleOutput console;

    @Autowired
    public DataLoader(ParkingLotService parkingLotService, ParkingLotLocalService parkingLotLocalService, ColorConsoleOutput console) {
        this.parkingLotService = parkingLotService;
        this.parkingLotLocalService = parkingLotLocalService;
        this.console = console;
    }

    /**
     * Method initiates the database and local Java memory storage with necessary data
     * This method runs once at every application start.
     *
     * @param parkingLotService - parking lots service
     * @return - result of provided operation
     */
    @Bean
    public CommandLineRunner loadData(ParkingLotService parkingLotService) {
        return (args) -> {

            // save parking lots
            Date date = new Date(System.currentTimeMillis());

            int totalParkingLotsNumber = Integer.parseInt(this.totalParkingLotsNumber);
            for (int i = 1; i <= totalParkingLotsNumber; i++) {

                //initial saving parking lots to database
                parkingLotService.save(new ParkingLot((long) i+10, i, date, ParkingLotStatus.UNKNOWN));
                //initial saving parking lots to local Java memory
                parkingLotLocalService.save(new ParkingLot((long) i+10, i, date, ParkingLotStatus.UNKNOWN));
            }

            // fetch all parking lots from database
            fetchParkingLots(parkingLotService, " from DATABASE:");
            // fetch all parking lots from local Java memory
            fetchParkingLots(parkingLotService, redTxt(" LOCAL Java memory:"));
        };
    }

    private void fetchParkingLots(ParkingLotService parkingLotService, String from) {
        log.info(console.classMsg("ParkingLot found with ") + puBrTxt("findAll()") + grTxt(from));
        printSeparator();
        for (ParkingLot parkingLot : parkingLotService.listAll()) {
            log.info(parkingLot.toString());
        }
        log.info("");

        // fetch an individual parking lot by ID
        Optional<ParkingLot> parkingLot = parkingLotService.findById(1L);

        log.info(grTxt("Parking Lot found with ") + puBrTxt("findById(1L):"));
        printSeparator();
        log.info(parkingLot.toString());
        log.info("");
    }

    private void printSeparator() {
        log.info("-------------------------------");
    }

    /**
     * Fallback method for initializing database.
     * Use this if the previous one does not work
     *
     * @param args - application arguments
     */
    @Override
    public void run(ApplicationArguments args) {
        Date date = new Date(System.currentTimeMillis());

        //initiate parking lots in database
        // int totalParkingLotsNumber = Integer.parseInt(this.totalParkingLotsNumber);
        /*for (int i = 1; i <= totalParkingLotsNumber; i++) {
            //an fallback method to load initial data
            //parkingLotService.save(new ParkingLot((long) i, i, date, ParkingLotStatus.FREE));
            //parkingLotLocalService.save(new ParkingLot((long) i, i, date, ParkingLotStatus.FREE));
        }*/
    }
}
