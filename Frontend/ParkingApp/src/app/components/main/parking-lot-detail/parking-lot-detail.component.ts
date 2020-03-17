import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {AuthenticationService} from '../../../services/account/auth.service';
import {Observable} from 'rxjs';
import {delay} from 'rxjs/operators';
import {setAcceptJsonHeaders} from '../../../services/data/data.service';
import {ParkingLot} from 'src/app/models/ParkingLot';
import {environment} from '../../../../environments/environment';
import {api} from '../../../services/navigation/app.endpoints';


@Component({
    selector: 'app-parking-lot-detail',
    templateUrl: './parking-lot-detail.component.html',
    styleUrls: ['./parking-lot-detail.component.css']
})
export class ParkingLotDetailComponent implements OnInit {

    @Input()
    parkingLot: ParkingLot;

    @Output()
    goBackEvent = new EventEmitter();

    @Output()
    bookingEvent = new EventEmitter();

    private action: string;

    private reservationSuccess = false;

    constructor(private route: ActivatedRoute,
                private authenticationService: AuthenticationService,
                private  http: HttpClient) {
    }

    ngOnInit() {
        // testing
        this.isAdminLoggedIn();

        this.route.queryParams.subscribe(
            params => {
                this.action = params.action;
            }
        );
    }

    goBack() {
        delay(100);
        this.goBackEvent.emit();
    }

    handleReservation() {
        const parkingLotNumber = this.parkingLot.number;
        const parkingLotStatus = this.parkingLot.status;
        console.log('handleReservation() parking lot #' + parkingLotNumber + ' ' + parkingLotStatus);

        if (parkingLotStatus === 'FREE') {
            this.reserveParkingLot(parkingLotNumber).subscribe(response => {

                if (response) {
                    alert('Reservation success.');

                    this.disableAfterDelay();
                    this.bookingEvent.emit();
                } else {
                    alert('Reservation failed.');
                }
            }, error => {
                console.log(error);
                this.reservationSuccess = false;
                alert('Reservation failed.');
            });
        }

        if (parkingLotStatus === 'RESERVED') {
            this.unreserveParkingLot(parkingLotNumber).subscribe(response => {

                if (response) {
                    alert('Cancel reservation success.');

                    this.disableAfterDelay();
                    this.bookingEvent.emit();
                } else {
                    alert('Cancel reservation failed.');
                }
            }, error => {
                console.log(error);
                this.reservationSuccess = false;
                alert('Cancel reservation failed.');
            });
        }
    }

    private disableAfterDelay() {
        setInterval(() => {
            this.reservationSuccess = true;
        }, 1050);
    }

    reserveParkingLot(parkingLotNumber: number): Observable<boolean> {
        const url = environment.restUrl + api.reservation + '/1' + parkingLotNumber;
        console.log('Reservation... parking lot #' + parkingLotNumber);

        return this.http.get<boolean>(url, {
            headers: setAcceptJsonHeaders()
        });
    }

    unreserveParkingLot(parkingLotNumber: number): Observable<boolean> {
        const url = environment.restUrl + api.unreservation + '/1' + parkingLotNumber;
        console.log('Cancel reservation... parking lot #' + parkingLotNumber);

        return this.http.get<boolean>(url, {
            headers: setAcceptJsonHeaders()
        });
    }

    isAdminLoggedIn() {
        return this.authenticationService.isAdminLoggedIn();
    }

    isUserLoggedIn() {
        return this.authenticationService.isUserLoggedIn();
    }

    refresh(): void {
        window.location.reload();
    }
}
