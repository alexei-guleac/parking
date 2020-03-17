package com.isd.parking.storage.util;

import com.isd.parking.models.ParkingLot;
import com.isd.parking.models.enums.ParkingLotStatus;
import com.isd.parking.service.ParkingLotDBService;
import com.isd.parking.service.ParkingLotLocalService;
import com.isd.parking.service.ParkingLotService;
import com.isd.parking.utils.ColorConsoleOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Optional;

import static com.isd.parking.utils.ColorConsoleOutput.*;


/**
 * Utility class
 * Fills the database and local Java memory storage with initial data
 */
@Component
@Slf4j
public class DataLoader  {

    @Value("${parking.lots.number}")
    private String totalParkingLotsNumber;

    private final ParkingLotDBService parkingLotDBService;

    private final ParkingLotLocalService parkingLotLocalService;

    private final ColorConsoleOutput console;

    public boolean flag = true;

    @Autowired
    public DataLoader(ParkingLotDBService parkingLotDBService, ParkingLotLocalService parkingLotLocalService, ColorConsoleOutput console) {
        this.parkingLotDBService = parkingLotDBService;
        this.parkingLotLocalService = parkingLotLocalService;
        this.console = console;
    }

    /**
     * Method initiates the database and local Java memory storage with necessary data
     * This method runs once at every application start.
     *
     * @return - result of provided operation
     */
    @PostConstruct
    public void loadDatabase() {
        Date date = new Date(System.currentTimeMillis());
        int totalParkingLotsNumber = Integer.parseInt(this.totalParkingLotsNumber);
        long numberOfParkLotsInDatabase = parkingLotDBService.countAll();

        //if db empty
        if (numberOfParkLotsInDatabase == 0) {
            //init with Unknown status parking lots
            for (int i = 1; i <= totalParkingLotsNumber; i++) {
                //initial saving parking lots to local Java memory
                parkingLotLocalService.save(new ParkingLot((long) i + 10, i, date, ParkingLotStatus.FREE));

                //initial saving parking lots to Database
                //parkingLotDBService.save(new ParkingLot((long) i + 10, i, date, ParkingLotStatus.UNKNOWN));
            }
        } else {
            for (ParkingLot parkingLot : parkingLotDBService.findAll()) {
                //saving parking lots state to local Java memory from Database
                parkingLotLocalService.save(parkingLot);
            }
        }

        // show all parking lots from database
        fetchParkingLots(parkingLotDBService, blTxt(" from DATABASE:"));
        // show all parking lots from local Java memory
        fetchParkingLots(parkingLotLocalService, redTxt(" from LOCAL Java memory:"));
    }

    public void fetchParkingLots(ParkingLotService parkingLotService, String from) {
        printSeparator();
        log.info(console.classMsg(getClass().getSimpleName(), "ParkingLot's found with ") + puBrTxt("findAll()") + grTxt(from));
        long numberOfParkLotsInStorage = parkingLotService.countAll();
        log.info(console.classMsg(getClass().getSimpleName(), "Total number: ") + puBrTxt("" + numberOfParkLotsInStorage));
        for (ParkingLot parkingLot : parkingLotService.findAll()) {
            log.info(parkingLot.toString());
        }
        log.info("");

        // fetch an individual parking lot by ID
        Optional<ParkingLot> parkingLot = parkingLotService.findById(11L);

        log.info(grTxt("Parking Lot found with ") + puBrTxt("findById(1L):"));
        printSeparator();
        log.info(parkingLot.toString());
        log.info("");
    }

    private void printSeparator() {
        log.info("-------------------------------");
    }
}
