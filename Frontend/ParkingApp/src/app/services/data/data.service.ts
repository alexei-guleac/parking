import {Injectable} from '@angular/core';
import {ParkingLot} from '@app/models/ParkingLot';
import {Statistics} from '@app/models/Statistics';
import {HttpClientService} from '@app/services/helpers/http-client.service';
import {api} from '@app/services/navigation/app.endpoints';
import {environment} from '@env';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';


@Injectable({
    providedIn: 'root',
})
export class DataService {
    constructor(private http: HttpClientService) {
    }

    getAllParkingLots(): Observable<Array<ParkingLot>> {
        return this.http
            .getJsonRequest<Array<ParkingLot>>(
                environment.restUrl + api.parking
            )
            .pipe(map((data) => data.map((pl) => ParkingLot.fromHttp(pl))));
    }

    getAllStats(): Observable<Array<Statistics>> {
        return this.http
            .getJsonRequest<Array<Statistics>>(
                environment.restUrl + api.statistics
            )
            .pipe(map((data) => data.map((st) => Statistics.fromHttp(st))));
    }
}
