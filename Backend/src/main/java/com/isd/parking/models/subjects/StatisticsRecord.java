package com.isd.parking.models.subjects;

import com.isd.parking.models.enums.ParkingLotStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;


/**
 * Statistics Record model class
 */
@Entity(name = "statistics")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Statistics entry model")
public class StatisticsRecord {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(notes = "Statistics record unique id")
    private Long id;

    @Column(name = "lot_number")
    @ApiModelProperty(notes = "Statistics record lot number")
    private Integer lotNumber;

    @Column(name = "updated_at")
    @ApiModelProperty(notes = "Statistics record date")
    private Date updatedAt;

    @Column(name = "parking_lot_status")
    @ApiModelProperty(notes = "Statistics record lot status")
    private ParkingLotStatus status;
}
