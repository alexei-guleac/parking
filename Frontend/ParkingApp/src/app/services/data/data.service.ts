import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {ParkingLot} from '../../models/ParkingLot';
import {Statistics} from '../../models/Statistics';
import {environment} from 'src/environments/environment';
import {api} from '../navigation/app.endpoints';


@Injectable({
    providedIn: 'root'
})
export class DataService {

    constructor(private http: HttpClient) {
    }

    getAllParkingLots(): Observable<Array<ParkingLot>> {

        return this.http.get<Array<ParkingLot>>(environment.restUrl + api.parking, {
            headers: setAcceptJsonHeaders()
        })
            .pipe(
                map(
                    data => data
                        .map(
                            pl => ParkingLot.fromHttp(pl)
                        )
                )
            );
    }

    getAllStats(): Observable<Array<Statistics>> {

        return this.http.get<Array<Statistics>>(environment.restUrl + api.statistics, {
            headers: setAcceptJsonHeaders()
        })
            .pipe(
                map(
                    data => data
                        .map(
                            st => Statistics.fromHttp(st)
                        )
                )
            );
    }
}

export function setAcceptJsonHeaders() {
    return {Accept: 'application/json'};
}
