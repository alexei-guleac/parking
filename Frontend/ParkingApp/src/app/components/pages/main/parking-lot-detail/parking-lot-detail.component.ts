import { AfterViewInit, Component, EventEmitter, Input, Output } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { ParkingLot } from "@app/models/ParkingLot";
import { parkingStatuses } from "@app/models/ParkingLotStatus";
import {
    ascendingStatisticsSortByTime,
    descendingStatisticsSortByTime,
    getStatisticsByUpdatedAtAscSortComparator,
    Statistics
} from "@app/models/Statistics";
import { AuthenticationService } from "@app/services/account/auth.service";
import { DataService } from "@app/services/data/data.service";
import { ObjectsSortService } from "@app/services/data/objects-sort.service";
import { ReservationService } from "@app/services/data/reservation.service";
import { ModalService } from "@app/services/modals/modal.service";
import { delay } from "rxjs/operators";


/**
 * Parking lot details
 */
@Component({
    selector: "app-parking-lot-detail",
    templateUrl: "./parking-lot-detail.component.html",
    styleUrls: ["./parking-lot-detail.component.scss"]
})
export class ParkingLotDetailComponent implements AfterViewInit {

    @Input()
    parkingLot: ParkingLot;

    @Output()
    goBackEvent = new EventEmitter();

    parkingLotStatus = parkingStatuses;

    private reservationSuccess = false;

    private statistics: Array<Statistics>;

    private filteredStatistics: Array<Statistics>;

    private p = 1;              // declaration of page index used for pagination

    // set the table row color depending on status
    private colors = [
        { status: parkingStatuses.FREE, background: "#28a745" },
        { status: parkingStatuses.OCCUPIED, background: "#dc3545" },
        { status: parkingStatuses.UNKNOWN, background: "gray" },
        { status: parkingStatuses.RESERVED, background: "#ffbf0f" }
    ];

    private dateSortedAsc = false;

    private dateSortedDesc = false;

    private timeSortedAsc = false;

    private timeSortedDesc = false;

    constructor(
        private route: ActivatedRoute,
        private authenticationService: AuthenticationService,
        private reservationService: ReservationService,
        private modalService: ModalService,
        private dataService: DataService,
        private objectsSortService: ObjectsSortService
    ) {
    }

    /**
     * Initialize the directive/component after Angular first displays the data-bound properties
     * and sets the directive/component's input properties.
     * Called once, after the first ngOnChanges()
     */
    ngAfterViewInit() {
        this.loadData();
    }

    /**
     * Go back to main parking page
     */
    goBack() {
        delay(100);
        this.goBackEvent.emit();
    }

    /**
     * Open parking lot admin reservation confirm modal window
     */
    showReservationModal() {
        const modalRef = this.modalService.openReservationModal(
            this.parkingLot.number,
            this.parkingLot.status
        );
        modalRef.result.then(
            (result) => {
                if (this.modalService.isSubmitResult(result)) {
                    this.handleReservation();
                }
            },
            this.modalService.handleDismissResult()
        );
    }

    /**
     * Load parking lots statistics data
     */
    private loadData() {
        this.dataService.getStatsByLotNumber(this.parkingLot.number).subscribe(
            (data) => {
                this.statistics = data;
                // preliminarily sort statistics by date parking lot status updated in ascending order
                this.statistics.sort(getStatisticsByUpdatedAtAscSortComparator());
                this.filteredStatistics = this.statistics;
            }
        );
    }

    /**
     * Sort statistics data by parking lot updated date
     */
    private sortStatisticsTableByDate() {
        this.objectsSortService.sortTable(
            this,
            "dateSortedAsc",
            "dateSortedDesc",
            this.filteredStatistics,
            "updatedAt",
            false,
            false,
            (this.timeSortedDesc = false),
            (this.timeSortedAsc = false)
        );
    }

    /**
     * Sort statistics data by parking lot updated time
     */
    private sortStatisticsTableByTime() {
        for (let i = 0; i < this.filteredStatistics.length - 1; i++) {
            if (this.filteredStatistics[i].updatedAt.getTime() >
                this.filteredStatistics[i + 1].updatedAt.getTime()) {
                this.timeSortedDesc = true;
                this.timeSortedAsc = false;
            }

            if (this.filteredStatistics[i].updatedAt.getTime() <
                this.filteredStatistics[i + 1].updatedAt.getTime()) {
                this.timeSortedAsc = true;
                this.timeSortedDesc = false;
            }
        }

        if (this.timeSortedAsc) {
            this.filteredStatistics.sort(descendingStatisticsSortByTime());

            this.timeSortedAsc = false;
            this.timeSortedDesc = true;
        } else {
            this.filteredStatistics.sort(ascendingStatisticsSortByTime());

            this.timeSortedDesc = false;
            this.timeSortedAsc = true;
        }

        this.dateSortedDesc = false;
        this.dateSortedAsc = false;
    }

    /**
     * Define statistics table row color based on parking lot status
     * @param parkingLotStatus - target parking lot status
     */
    private getColor(parkingLotStatus: string) {
        return this.colors.filter((item) => item.status === parkingLotStatus)[0]
            .background;
    }

    /**
     * Handle parking lot admin reservation
     */
    private handleReservation() {
        const parkingLotNumber = this.parkingLot.id;
        const parkingLotStatus = this.parkingLot.status;

        // reservate only if parking lot status is FREE to avoid conflicts
        if (parkingLotStatus === parkingStatuses.FREE) {
            this.processReservation(parkingLotNumber);
        }

        // unreserve only if parking lot status is RESERVED to avoid conflicts
        if (parkingLotStatus === parkingStatuses.RESERVED) {
            this.processResetReservation(parkingLotNumber);
        }
    }

    /**
     * Process parking lot reservation
     * @param parkingLotNumber - target parking lot number
     */
    private processReservation(parkingLotNumber) {
        const msg = "Reservation";

        this.reservationService
            .reserveParkingLot(parkingLotNumber)
            .subscribe(
                this.handleReservationResponse(msg),
                this.handleReservationError(msg)
            );
    }

    /**
     * Process parking lot reservation reset or cancel it
     * @param parkingLotNumber - target parking lot number
     */
    private processResetReservation(parkingLotNumber) {
        const msg = "Cancel reservation";

        this.reservationService
            .unreserveParkingLot(parkingLotNumber)
            .subscribe(
                this.handleReservationResponse(msg),
                this.handleReservationError(msg)
            );
    }

    /**
     * Handle parking lot reservation server response
     * @param msg - operation message
     */
    private handleReservationResponse(msg: string) {
        return (response) => {
            if (response) {
                alert(msg + " success.");

                this.switchParkingLotState();
                this.disableReserveButtonAfterDelay();
            } else {
                alert(msg + " failed.");
            }
        };
    }

    /**
     * Handle parking lot reservation server error
     * @param msg - operation message
     */
    private handleReservationError(msg: string) {
        return (error) => {
            // console.log(error);
            this.reservationSuccess = false;
            alert(msg + " failed.");
        };
    }

    /**
     * Check if admin is logged in
     */
    private isAdminLoggedIn() {
        return this.authenticationService.isAdminLoggedIn();
    }

    /**
     * Disable reservation button after delay
     */
    private disableReserveButtonAfterDelay() {
        setInterval(() => {
            this.reservationSuccess = true;
        }, 1050);
    }

    /**
     * Switch parking lot reservation state
     */
    private switchParkingLotState() {
        if (this.parkingLot.status === parkingStatuses.FREE) {
            this.parkingLot.status = parkingStatuses.RESERVED;
        } else {
            if (this.parkingLot.status === parkingStatuses.RESERVED) {
                this.parkingLot.status = parkingStatuses.FREE;
            }
        }
        this.parkingLot.updatedAt = new Date();
    }

}
