import { Injectable } from '@angular/core';
import { ParkingLot } from '@app/models/ParkingLot';
import { Statistics } from '@app/models/Statistics';
import { HttpClientService } from '@app/services/helpers/http-client.service';
import { api } from '@app/services/navigation/app.endpoints';
import { environment } from '@env';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';


/**
 * Common interface for ensure the availability of the following fields
 */
export interface StatsDateSortable {
    filteredStatistics: Array<Statistics>;
    lotSortedDesc: boolean;
    lotSortedAsc: boolean;
    dateSortedAsc: boolean;
    dateSortedDesc: boolean;
    timeSortedAsc: boolean;
    timeSortedDesc: boolean;
}

/**
 * Service for get data from server
 */
@Injectable({
    providedIn: 'root'
})
export class DataService {
    constructor(private http: HttpClientService) {
    }

    /**
     * Get all parking lots from server
     */
    getAllParkingLots(): Observable<Array<ParkingLot>> {
        return this.http
            .getJsonRequest<Array<ParkingLot>>(
                environment.restUrl + api.parking
            )
            .pipe(map((data) => data.map((pl) => ParkingLot.fromHttp(pl))));
    }

    /**
     * Get all parking lots usage statistics from server
     */
    getAllStats(): Observable<Array<Statistics>> {
        return this.http
            .getJsonRequest<Array<Statistics>>(
                environment.restUrl + api.statistics
            )
            .pipe(map((data) => data.map((st) => Statistics.fromHttp(st))));
    }

    /**
     * Get parking lots usage statistics by parking lot number from server
     */
    getStatsByLotNumber(lotNumber: number): Observable<Array<Statistics>> {
        return this.http
            .getJsonRequest<Array<Statistics>>(
                environment.restUrl + api.statisticsByLot + '/' + lotNumber
            )
            .pipe(map((data) => data.map((st) => Statistics.fromHttp(st))));
    }
}
