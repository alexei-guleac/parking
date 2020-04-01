import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {environment} from 'src/environments/environment';
import {ParkingLot} from '../../models/ParkingLot';
import {Statistics} from '../../models/Statistics';
import {HttpClientService} from '../helpers/http-client.service';
import {api} from '../navigation/app.endpoints';


@Injectable({
    providedIn: 'root'
})
export class DataService {
    constructor(private http: HttpClientService) {
    }

    getAllParkingLots(): Observable<Array<ParkingLot>> {
        return this.http
            .getJsonRequest<Array<ParkingLot>>(
                environment.restUrl + api.parking
            )
            .pipe(map(data => data.map(pl => ParkingLot.fromHttp(pl))));
    }

    getAllStats(): Observable<Array<Statistics>> {
        return this.http
            .getJsonRequest<Array<Statistics>>(
                environment.restUrl + api.statistics
            )
            .pipe(map(data => data.map(st => Statistics.fromHttp(st))));
    }
}
