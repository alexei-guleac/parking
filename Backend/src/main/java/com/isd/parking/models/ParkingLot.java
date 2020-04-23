package com.isd.parking.models;

import com.isd.parking.models.enums.ParkingLotStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

import static com.isd.parking.utils.ColorConsoleOutput.*;


/**
 * Parking Lot model class
 */
@Entity(name = "parking_lots")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class ParkingLot {

    @Id
    @Column(name = "id")
    private Long id = 11L;

    @Column(name = "lot_number")
    private Integer number;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "status")
    private ParkingLotStatus status;

    public void setUpdatedNow() {
        this.setUpdatedAt(new Date(System.currentTimeMillis()));
    }

    @Override
    public String toString() {
        return cyBrTxt(getClass().getSimpleName()) + " {" +
            "id = " + redTxt(String.valueOf(id)) +
            ", number = " + ywTxt(String.valueOf(number)) +
            ", updatedAt = " + blTxt(String.valueOf(updatedAt)) +
            ", status = " + getStatusColor() +
            " }";
    }

    /**
     * Auxiliary method for console color output of parking lot status
     *
     * @return colored status string
     */
    private String getStatusColor() {
        return status == ParkingLotStatus.UNKNOWN ? whTxt(String.valueOf(status)) :
            status == ParkingLotStatus.FREE ? grBrTxt(String.valueOf(status)) :
                status == ParkingLotStatus.OCCUPIED ? redBrTxt(String.valueOf(status)) :
                    status == ParkingLotStatus.RESERVED ? ywBrTxt(String.valueOf(status)) : String.valueOf(status);
    }
}
