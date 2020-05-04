import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { setAcceptJsonHeaders } from "@app/config/http-config";
import { api } from "@app/services/navigation/app.endpoints";
import { environment } from "@env";
import { Observable } from "rxjs";


/**
 * Service for parking lot reservation related operations
 */
@Injectable({
    providedIn: 'root',
})
export class ReservationService {

    constructor(private http: HttpClient) {
    }

    /**
     * Reserve parking lot by number
     * @param parkingLotNumber - target parking lot number
     */
    reserveParkingLot(parkingLotNumber: number): Observable<boolean> {
        const url =
            environment.restUrl + api.reservation + "/" + parkingLotNumber;
        console.log('Reservation... parking lot #' + parkingLotNumber);

        return this.http.get<boolean>(url, {
            headers: setAcceptJsonHeaders(),
        });
    }

    /**
     * Unreserve parking lot by number
     * @param parkingLotNumber - target parking lot number
     */
    unreserveParkingLot(parkingLotNumber: number): Observable<boolean> {
        const url =
            environment.restUrl +
            api.cancelReservation +
            "/" + parkingLotNumber;
        console.log('Cancel reservation... parking lot #' + parkingLotNumber);

        return this.http.get<boolean>(url, {
            headers: setAcceptJsonHeaders(),
        });
    }
}
