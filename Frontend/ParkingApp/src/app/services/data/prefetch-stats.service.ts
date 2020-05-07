import { Injectable } from "@angular/core";
import { Resolve } from "@angular/router";
import { Statistics } from "@app/models/Statistics";
import { Observable } from "rxjs";
import { DataService } from "./data.service";


/**
 * Service for parking lot usage statistics data preloading
 */
@Injectable({
    providedIn: "root"
})
export class PrefetchStatsService
    implements Resolve<Observable<Array<Statistics>>> {

    constructor(private dataService: DataService) {
    }

    /**
     * Load parking lot usage statistics data before page loads
     */
    resolve() {
        return this.dataService.getAllStats();
    }
}
