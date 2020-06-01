import { AfterViewInit, Component, EventEmitter, Input, Output } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ParkingLot } from '@app/models/ParkingLot';
import { parkingColors, parkingStatuses } from '@app/models/ParkingLotStatus';
import { getStatisticsByUpdatedAtDescSortComparator, Statistics } from '@app/models/Statistics';
import { AuthenticationService } from '@app/services/account/auth.service';
import { DataService, StatsDateSortable } from '@app/services/data/data.service';
import { ObjectsSortService } from '@app/services/data/objects-sort.service';
import { ReservationService } from '@app/services/data/reservation.service';
import { StatisticsService } from '@app/services/data/statistics.service';
import { ModalService } from '@app/services/modals/modal.service';
import { TranslateService } from '@ngx-translate/core';
import { delay } from 'rxjs/operators';


/**
 * Parking lot details
 */
@Component({
    selector: 'app-parking-lot-detail',
    templateUrl: './parking-lot-detail.component.html',
    styleUrls: ['./parking-lot-detail.component.scss']
})
export class ParkingLotDetailComponent implements AfterViewInit, StatsDateSortable {

    @Input()
    parkingLot: ParkingLot;

    @Output()
    goBackEvent = new EventEmitter();

    parkingLotStatus = parkingStatuses;

    private reservationSuccess = false;

    private statistics: Array<Statistics>;

    filteredStatistics: Array<Statistics>;

    dateSortedAsc = false;

    dateSortedDesc = false;

    timeSortedAsc = false;

    timeSortedDesc = false;

    lotSortedAsc: boolean;

    lotSortedDesc: boolean;

    // declaration of page index used for pagination
    private p = 1;

    // set the table row color depending on status
    private colors = parkingColors;

    constructor(
        private route: ActivatedRoute,
        private authenticationService: AuthenticationService,
        private reservationService: ReservationService,
        private modalService: ModalService,
        private dataService: DataService,
        private objectsSortService: ObjectsSortService,
        private statisticsService: StatisticsService,
        private translate: TranslateService
    ) {
    }

    /**
     * Initialize the directive/component after Angular first displays the data-bound properties
     * and sets the directive/component's input properties.
     * Called once, after the first ngOnChanges()
     */
    ngAfterViewInit() {
        this.loadStatsData();
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
    private loadStatsData() {
        this.dataService.getStatsByLotNumber(this.parkingLot.number).subscribe(
            (data) => {
                this.statistics = data;
                // preliminarily sort statistics by date parking lot status updated in ascending order
                this.statistics.sort(getStatisticsByUpdatedAtDescSortComparator());
                this.filteredStatistics = this.statistics;
                this.dateSortedDesc = true;
            }
        );
    }

    /**
     * Sort statistics data by parking lot updated date
     */
    private sortStatisticsTableByDate() {
        this.objectsSortService.sortTable(
            this,
            'dateSortedAsc',
            'dateSortedDesc',
            this.filteredStatistics,
            'updatedAt',
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
        this.statisticsService.sortStatisticsTableByTime(this);
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

        // reserve only if parking lot status is FREE to avoid conflicts
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
        const msg = this.translate.instant('parking-lot-detail.reservation');

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
        const msg = this.translate.instant('parking-lot-detail.cancel-reservation');

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
                alert(msg + ' ' + this.translate.instant('parking-lot-detail.success-msg'));

                this.switchParkingLotState();
                this.disableReserveButtonAfterDelay();
                this.loadStatsData();
            } else {
                alert(msg + ' ' + this.translate.instant('parking-lot-detail.fail-msg'));
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
            alert(msg + ' ' + this.translate.instant('parking-lot-detail.fail-msg'));
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
