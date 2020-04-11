import {Injectable} from '@angular/core';
import {Resolve} from '@angular/router';
import {ParkingLot} from '@app/models/ParkingLot';
import {DataService} from '@app/services/data/data.service';
import {Observable} from 'rxjs';


@Injectable({
    providedIn: 'root',
})
export class PrefetchParkingLotsService
    implements Resolve<Observable<Array<ParkingLot>>> {
    constructor(private dataService: DataService) {
    }

    resolve() {
        return this.dataService.getAllParkingLots();
    }
}
