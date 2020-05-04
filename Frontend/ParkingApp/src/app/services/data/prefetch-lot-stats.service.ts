import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, Resolve } from "@angular/router";
import { Statistics } from "@app/models/Statistics";
import { Observable } from "rxjs";
import { DataService } from "./data.service";


/**
 * Service for parking lot usage statistics data preloading
 */
@Injectable({
    providedIn: "root"
})
export class PrefetchLotStatsService
    implements Resolve<Observable<Array<Statistics>>> {

    constructor(private dataService: DataService) {
    }

    /**
     * Load data before page loads
     * @param route - target route
     */
    resolve(route: ActivatedRouteSnapshot): Observable<any> | Promise<any> | any {
        return this.dataService.getStatsByLotNumber(1);
    }
}
