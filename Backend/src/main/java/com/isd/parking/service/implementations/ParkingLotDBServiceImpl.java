package com.isd.parking.service.implementations;

import com.isd.parking.models.ParkingLot;
import com.isd.parking.repository.ParkingLotRepository;
import com.isd.parking.service.ParkingLotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.isd.parking.utils.ColorConsoleOutput.methodMsg;


/**
 * Parking Lot Service class for database repository
 * Contains methods for
 * - getting all parking lots,
 * - get parking lot by id,
 * - saving (in this case updating) parking lot
 */
@Service
@Slf4j
public class ParkingLotDBServiceImpl implements ParkingLotService {

    private final ParkingLotRepository parkingLotRepository;

    @Autowired
    public ParkingLotDBServiceImpl(ParkingLotRepository parkingLotRepository) {
        this.parkingLotRepository = parkingLotRepository;
    }

    /**
     * Get all parking lots from database method
     *
     * @return - Parking lots list
     */
    @Transactional
    @Override
    public List<ParkingLot> findAll() {
        log.info(methodMsg("get all parking lots list executed..."));
        return parkingLotRepository.findAll();
    }

    /**
     * Get parking lot by id from database method
     *
     * @return - specified parking lot
     */
    @Transactional
    @Override
    public Optional<ParkingLot> findById(Long parkingLotId) {
        log.info(methodMsg("get parking lot by id executed..."));
        return parkingLotRepository.findById(parkingLotId);
    }

    /**
     * Save parking lot in database method
     * Used for update status of parking lot
     *
     * @return - Parking lot which was saved in database
     */
    @Transactional
    @Override
    public ParkingLot save(ParkingLot parkingLot) {
        log.info(methodMsg("save parking lot in database executed..."));
        return parkingLotRepository.save(parkingLot);
    }

    /**
     * Count parking lots in SQL database
     *
     * @return - parking lots total count
     */
    @Transactional
    @Override
    public long countAll() {
        log.info(methodMsg("count parking lots..."));
        return parkingLotRepository.count();
    }
}

