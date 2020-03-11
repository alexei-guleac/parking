import {Injectable} from '@angular/core';
import {ParkingLot} from './Model/ParkingLot';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {environment} from 'src/environments/environment';
import {Statistics} from './Model/Statistics';
import {AuthenticationService} from './Account/auth.service';

@Injectable({
    providedIn: 'root'
})
export class DataService {

    private authHeaders: {};

    constructor(private http: HttpClient, private authService: AuthenticationService) {
    }

    getAllParkingLots(): Observable<Array<ParkingLot>> {
        this.setHeaders();

        return this.http.get<Array<ParkingLot>>(environment.restUrl + '/parking', {
            headers: this.authHeaders
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
        this.setHeaders();

        return this.http.get<Array<Statistics>>(environment.restUrl + '/statistics', {
            headers: this.authHeaders
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

    private setHeaders() {
        if (this.authService.isUserLoggedIn()) {

            console.log('SET HEADERS');
            console.log(this.authService.isUserLoggedIn());
            console.log(sessionStorage.getItem('token'));

            this.authHeaders = {
                Accept: 'application/json',
                // Authorization: 'Basic ' + sessionStorage.getItem('token')
                Authorization: 'Bearer ' + sessionStorage.getItem('token')
            };
        } else {
            this.authHeaders = {Accept: 'application/json'};
        }
    }
}
