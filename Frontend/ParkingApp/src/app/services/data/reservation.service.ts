import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {setAcceptJsonHeaders} from '@app/config/http-config';
import {api} from '@app/services/navigation/app.endpoints';
import {environment} from '@env';
import {Observable} from 'rxjs';


@Injectable({
    providedIn: 'root',
})
export class ReservationService {
    constructor(private http: HttpClient) {
    }

    reserveParkingLot(parkingLotNumber: number): Observable<boolean> {
        const url =
            environment.restUrl + api.reservation + '/1' + parkingLotNumber;
        console.log('Reservation... parking lot #' + parkingLotNumber);

        return this.http.get<boolean>(url, {
            headers: setAcceptJsonHeaders(),
        });
    }

    unreserveParkingLot(parkingLotNumber: number): Observable<boolean> {
        const url =
            environment.restUrl +
            api.cancelReservation +
            '/1' +
            parkingLotNumber;
        console.log('Cancel reservation... parking lot #' + parkingLotNumber);

        return this.http.get<boolean>(url, {
            headers: setAcceptJsonHeaders(),
        });
    }
}
