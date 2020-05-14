import { Injectable } from '@angular/core';
import { Resolve } from '@angular/router';
import { ParkingLot } from '@app/models/ParkingLot';
import { DataService } from '@app/services/data/data.service';
import { Observable } from 'rxjs';


/**
 * Service for parking lot data preloading
 */
@Injectable({
    providedIn: 'root'
})
export class PrefetchParkingLotsService
    implements Resolve<Observable<Array<ParkingLot>>> {

    constructor(private dataService: DataService) {
    }

    /**
     * Load parking lot data before page loads
     */
    resolve() {
        return this.dataService.getAllParkingLots();
    }
}
