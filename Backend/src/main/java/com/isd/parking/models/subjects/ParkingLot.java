package com.isd.parking.models.subjects;

import com.isd.parking.models.enums.ParkingLotStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

import static com.isd.parking.utilities.ColorConsoleOutput.*;


/**
 * Parking Lot model class
 */
@Entity(name = "parking_lots")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@ApiModel(description = "Parking lot model")
public class ParkingLot {

    @Id
    @Column(name = "id")
    @ApiModelProperty(notes = "Parking lot unique id " +
        "(two numbers comes from Arduino scalable infrastructure - master board id + slave board id)")
    private @NotNull Long id = 11L;

    @Column(name = "lot_number")
    @ApiModelProperty(notes = "Parking lot number")
    private Integer number;

    @Column(name = "updated_at")
    @ApiModelProperty(notes = "Parking lot updated at date")
    private Date updatedAt;

    @Column(name = "status")
    @ApiModelProperty(notes = "Parking lot status (free, occupied, unknown, reserved)")
    private ParkingLotStatus status;

    public void setUpdatedNow() {
        this.setUpdatedAt(new Date(System.currentTimeMillis()));
    }

    @Override
    public @NotNull String toString() {
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
    private @NotNull String getStatusColor() {
        return status == ParkingLotStatus.UNKNOWN ? whTxt(String.valueOf(status)) :
            status == ParkingLotStatus.FREE ? grBrTxt(String.valueOf(status)) :
                status == ParkingLotStatus.OCCUPIED ? redBrTxt(String.valueOf(status)) :
                    status == ParkingLotStatus.RESERVED ? ywBrTxt(String.valueOf(status)) : String.valueOf(status);
    }
}
