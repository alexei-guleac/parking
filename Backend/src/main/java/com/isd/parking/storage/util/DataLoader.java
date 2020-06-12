package com.isd.parking.storage.util;

import com.isd.parking.models.enums.ParkingLotStatus;
import com.isd.parking.models.subjects.ParkingLot;
import com.isd.parking.repository.ParkingLotLocalStorage;
import com.isd.parking.repository.ParkingLotRepository;
import com.isd.parking.repository.ParkingLotStorage;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.isd.parking.utilities.ColorConsoleOutput.*;


/**
 * Utility class
 * Fills the database and local Java memory storage with initial parking lots data
 * after server first start
 */
@Component
@Slf4j
public class DataLoader {

    @Value("${parking.lots.number.total}")
    private String totalParkingLotsNumber;

    @Value("${parking.lots.number.permasterboard}")
    private String masterParkingLotsNumber;

    private final ParkingLotRepository parkingLotRepository;

    private final ParkingLotLocalStorage parkingLotLocalStorage;

    @Autowired
    public DataLoader(ParkingLotRepository parkingLotDBService,
                      ParkingLotLocalStorage parkingLotLocalStorage) {
        this.parkingLotRepository = parkingLotDBService;
        this.parkingLotLocalStorage = parkingLotLocalStorage;
    }

    /**
     * Method initiates the database and local Java memory storage with necessary data
     * This method runs once at every application start.
     *
     * @return - result of provided operation
     */
    @PostConstruct
    public void loadDatabase() {
        @NotNull Date date = new Date(System.currentTimeMillis());
        int totalParkingLotsNumber = Integer.parseInt(this.totalParkingLotsNumber);
        long numberOfParkLotsInDatabase = parkingLotRepository.count();

        // if db empty
        if (numberOfParkLotsInDatabase == 0) {
            int masterBoardPrefix = 1;
            // init with FREE status parking lots
            for (int i = 0, j = 0; i < totalParkingLotsNumber; i++, j++) {

                if (j == Integer.parseInt(masterParkingLotsNumber)) {
                    ++masterBoardPrefix;
                    j = 0;
                }
                // initial saving parking lots to local Java memory
                parkingLotLocalStorage.save(
                    new ParkingLot(
                        Long.valueOf(masterBoardPrefix + "" + j), i + 1, date, ParkingLotStatus.FREE)
                );
            }
        } else {
            for (ParkingLot parkingLot : parkingLotRepository.findAll()) {
                // saving parking lots state to local Java memory from Database
                parkingLotLocalStorage.save(parkingLot);
            }
        }

        // show all parking lots from database
        fetchParkingLots(parkingLotRepository, blTxt(" from DATABASE:"));
        // show all parking lots from local Java memory
        fetchParkingLots(parkingLotLocalStorage, redTxt(" from LOCAL Java memory:"));
    }

    /**
     * Get and show to console all parking lots from in-memory storage
     *
     * @param parkingLotStorage - target parking lot storage
     * @param from              - target source console indicator
     */
    public void fetchParkingLots(@NotNull ParkingLotStorage parkingLotStorage, String from) {
        fetchData(from, parkingLotStorage.count(),
            parkingLotStorage.findAll(), parkingLotStorage.findById(11L));
    }

    /**
     * Get and show to console all parking lots from database
     *
     * @param parkingLotRepository - target parking lot storage
     * @param from                 - target source console indicator
     */
    public void fetchParkingLots(@NotNull ParkingLotRepository parkingLotRepository, String from) {
        fetchData(from, parkingLotRepository.count(),
            parkingLotRepository.findAll(), parkingLotRepository.findById(11L));
    }

    private void fetchData(String from, long count, List<ParkingLot> all, Optional<ParkingLot> byId) {
        printSeparator();
        log.info(methodMsg("ParkingLot's found with ") + puBrTxt("findAll()") + grTxt(from));
        log.info(methodMsg("Total number: " + puBrTxt("" + count)));
        for (@NotNull ParkingLot parkingLot : all) {
            log.info(parkingLot.toString());
        }
        log.info("");
        // fetch an individual parking lot by ID
        log.info(grTxt("Parking Lot found with ") + puBrTxt("findById(1L):"));
        printSeparator();
        log.info(byId.toString());
        log.info("");
    }

    private void printSeparator() {
        log.info("-------------------------------");
    }
}
