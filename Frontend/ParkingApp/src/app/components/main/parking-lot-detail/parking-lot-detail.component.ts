import {HttpClient} from '@angular/common/http';
import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs';
import {delay} from 'rxjs/operators';
import {ParkingLot} from 'src/app/models/ParkingLot';
import {environment} from '../../../../environments/environment';
import {setAcceptJsonHeaders} from '../../../config/http-config';
import {status} from '../../../models/ParkingLotStatus';
import {AuthenticationService} from '../../../services/account/auth.service';
import {api} from '../../../services/navigation/app.endpoints';


@Component({
    selector: 'app-parking-lot-detail',
    templateUrl: './parking-lot-detail.component.html',
    styleUrls: ['./parking-lot-detail.component.css']
})
export class ParkingLotDetailComponent implements OnInit {
    parkingLotStatus = status;

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
        private http: HttpClient
    ) {
    }

    ngOnInit() {
        // testing
        this.isAdminLoggedIn();

        this.route.queryParams.subscribe(params => {
            this.action = params.action;
        });
    }

    goBack() {
        delay(100);
        this.goBackEvent.emit();
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

        if (parkingLotStatus === status.FREE) {
            this.reserveParkingLot(parkingLotNumber).subscribe(
                response => {
                    if (response) {
                        alert('Reservation success.');

                        this.disableAfterDelay();
                        this.bookingEvent.emit();
                    } else {
                        alert('Reservation failed.');
                    }
                },
                error => {
                    console.log(error);
                    this.reservationSuccess = false;
                    alert('Reservation failed.');
                }
            );
        }

        if (parkingLotStatus === status.RESERVED) {
            this.unreserveParkingLot(parkingLotNumber).subscribe(
                response => {
                    if (response) {
                        alert('Cancel reservation success.');

                        this.disableAfterDelay();
                        this.bookingEvent.emit();
                    } else {
                        alert('Cancel reservation failed.');
                    }
                },
                error => {
                    console.log(error);
                    this.reservationSuccess = false;
                    alert('Cancel reservation failed.');
                }
            );
        }
    }

    private disableAfterDelay() {
        setInterval(() => {
            this.reservationSuccess = true;
        }, 1050);
    }

    private reserveParkingLot(parkingLotNumber: number): Observable<boolean> {
        const url =
            environment.restUrl + api.reservation + '/1' + parkingLotNumber;
        console.log('Reservation... parking lot #' + parkingLotNumber);

        return this.http.get<boolean>(url, {
            headers: setAcceptJsonHeaders()
        });
    }

    private unreserveParkingLot(parkingLotNumber: number): Observable<boolean> {
        const url =
            environment.restUrl +
            api.cancelReservation +
            '/1' +
            parkingLotNumber;
        console.log('Cancel reservation... parking lot #' + parkingLotNumber);

        return this.http.get<boolean>(url, {
            headers: setAcceptJsonHeaders()
        });
    }

    private isAdminLoggedIn() {
        return this.authenticationService.isAdminLoggedIn();
    }
}
