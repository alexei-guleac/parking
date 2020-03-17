import {Injectable} from '@angular/core';
import {Resolve} from '@angular/router';
import {Observable} from 'rxjs';
import {DataService} from './data.service';
import {ParkingLot} from '../../models/ParkingLot';

@Injectable({
    providedIn: 'root'
})
export class PrefetchParkingLotsService implements Resolve<Observable<Array<ParkingLot>>> {

    constructor(private dataService: DataService) {
    }

    resolve() {
        return this.dataService.getAllParkingLots();
    }
}
