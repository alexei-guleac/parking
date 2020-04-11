import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ParkingLot} from '@app/models/ParkingLot';
import {parkingStatuses} from '@app/models/ParkingLotStatus';
import {AuthenticationService} from '@app/services/account/auth.service';
import {ReservationService} from '@app/services/data/reservation.service';
import {ModalService} from '@app/services/modals/modal.service';
import {delay} from 'rxjs/operators';


@Component({
    selector: 'app-parking-lot-detail',
    templateUrl: './parking-lot-detail.component.html',
    styleUrls: ['./parking-lot-detail.component.scss'],
})
export class ParkingLotDetailComponent implements OnInit {
    parkingLotStatus = parkingStatuses;

    @Input()
    parkingLot: ParkingLot;

    @Output()
    goBackEvent = new EventEmitter();

    @Output()
    bookingEvent = new EventEmitter();

    private action: string;

    private reservationSuccess = false;

    constructor(
        private route: ActivatedRoute,
        private authenticationService: AuthenticationService,
        private reservationService: ReservationService,
        private modalService: ModalService
    ) {
    }

    ngOnInit() {
        this.route.queryParams.subscribe(this.getQueryParamsCallback());
    }

    showReservationModal() {
        const modalRef = this.modalService.openReservationModal(
            this.parkingLot.number,
            this.parkingLot.status
        );
        let logoutModalResult: string;

        modalRef.result.then(
            (result) => {
                logoutModalResult = `Closed with: ${result}`;

                if (this.modalService.isSubmitResult(result)) {
                    this.handleReservation();
                }
                console.log(logoutModalResult);
            },
            (reason) => {
                logoutModalResult = `Dismissed ${this.modalService.getDismissReason(
                    reason
                )}`;
                console.log(logoutModalResult);
            }
        );
    }

    goBack() {
        delay(100);
        this.goBackEvent.emit();
    }

    private getQueryParamsCallback() {
        return (params) => {
            this.action = params.action;
        };
    }

    private handleReservation() {
        const parkingLotNumber = this.parkingLot.number;
        const parkingLotStatus = this.parkingLot.status;
        console.log(
            'handleReservation() parking lot #' +
            parkingLotNumber +
            ' ' +
            parkingLotStatus
        );

        if (parkingLotStatus === parkingStatuses.FREE) {
            this.reservationService
                .reserveParkingLot(parkingLotNumber)
                .subscribe(
                    (response) => {
                        if (response) {
                            alert('Reservation success.');

                            this.switchParkingLotState();
                            this.disableReserveButtonAfterDelay();
                            this.bookingEvent.emit();
                        } else {
                            alert('Reservation failed.');
                        }
                    },
                    (error) => {
                        console.log(error);
                        this.reservationSuccess = false;
                        alert('Reservation failed.');
                    }
                );
        }

        if (parkingLotStatus === parkingStatuses.RESERVED) {
            this.reservationService
                .unreserveParkingLot(parkingLotNumber)
                .subscribe(
                    (response) => {
                        if (response) {
                            alert('Cancel reservation success.');

                            this.switchParkingLotState();
                            this.disableReserveButtonAfterDelay();
                            this.bookingEvent.emit();
                        } else {
                            alert('Cancel reservation failed.');
                        }
                    },
                    (error) => {
                        console.log(error);
                        this.reservationSuccess = false;
                        alert('Cancel reservation failed.');
                    }
                );
        }
    }

    private isAdminLoggedIn() {
        return this.authenticationService.isAdminLoggedIn();
    }

    private disableReserveButtonAfterDelay() {
        setInterval(() => {
            this.reservationSuccess = true;
        }, 1050);
    }

    private switchParkingLotState() {
        if (this.parkingLot.status === parkingStatuses.FREE) {
            this.parkingLot.status = parkingStatuses.RESERVED;
        } else {
            if (this.parkingLot.status === parkingStatuses.RESERVED) {
                this.parkingLot.status = parkingStatuses.FREE;
            }
        }
    }
}
