import { AfterViewInit, Component, OnDestroy, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import {
    getParkingLotByIdPredicate,
    getParkingLotByNumberPredicate,
    getParkingLotsComparator,
    ParkingLot
} from "@app/models/ParkingLot";
import { parkingStatuses } from "@app/models/ParkingLotStatus";
import { DataService } from "@app/services/data/data.service";
import { actions, appRoutes } from "@app/services/navigation/app.endpoints";
import { NavigationService } from "@app/services/navigation/navigation.service";
import { interval, Subscription } from "rxjs";


@Component({
    selector: 'app-feature2',
    templateUrl: './parking-layout.component.html',
    styleUrls: ['./parking-layout.component.scss'],
})
export class ParkingLayoutComponent
    implements OnInit, OnDestroy, AfterViewInit {

    parkingLotStatus = parkingStatuses;

    parkingLots: Array<ParkingLot>;

    private selectedParkingLot: ParkingLot;

    private action: string;

    private dataLoaded = false;

    private updateSubscription: Subscription;

    private connectionLostSubscription: Subscription;

    private loadDataSubscription: Subscription;

    private message = 'Please wait...';

    private loadDataCounter = 0;

    private numberOfParkingLots: Array<number> = new Array<number>(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

    private classApplied: Array<boolean> = new Array<boolean>();

    private selectedLotId: any;

    constructor(
        private dataService: DataService,
        private route: ActivatedRoute,
        private router: Router,
        private navigationService: NavigationService
    ) {
    }

    /**
     * Initialize the directive/component after Angular first displays the data-bound properties
     * and sets the directive/component's input properties.
     * Called once, after the first ngOnChanges()
     */
    ngOnInit() {
        this.loadData();
        this.fillClassHighlightArray();
        this.subscribeOnUrlParams();

        this.updateSubscription = interval(3000).subscribe(() => {
            this.loadData();
        });
    }

    /**
     * Cleanup just before Angular destroys the directive/component.
     * Unsubscribe Observables and detach event handlers to avoid memory leaks.
     * Called just before Angular destroys the directive/component
     */
    ngOnDestroy() {
        this.updateSubscription.unsubscribe();
        this.loadDataSubscription.unsubscribe();
    }

    /**
     * Load parking lots data
     */
    loadData() {
        this.loadDataSubscription = this.dataService
            .getAllParkingLots()
            .subscribe(
                this.handleData(),
                this.handleLoadError());
    }

    /**
     *  Is called after Angular has fully initialized a component's view.
     *  Define an ngAfterViewInit() method to handle any additional initialization tasks
     */
    ngAfterViewInit() {
        // hardcoded timeout for prevent null pointer access
        if (this.selectedLotId) {
            setTimeout(() => {
                this.highlightSelectedLot(this.selectedLotId);
            }, 200);
        }
    }

    /**
     * Subscribing to query string URL parameters
     */
    private subscribeOnUrlParams() {
        this.route.queryParams.subscribe(
            // tslint:disable-next-line: no-string-literal
            this.getQueryparamsCallback()
        );
    }

    /**
     * Callback function for processing query string URL parameters
     */
    private getQueryparamsCallback() {
        return (params) => {
            if (params.action) {
                this.action = params.action;
                if (params.action === actions.show) {
                    if (params.id) {
                        this.selectedLotId = params.id;
                    }
                }
            } else {
                this.action = null;
            }
        };
    }

    /**
     * Handle parking lots data in server response
     */
    private handleData() {
        return (data) => {
            if (data.length !== 0) {
                this.sortParkingLots(data);
                this.dataLoaded = true;
                this.message = "";
                // console.log('loadData');

                this.fillMissingLots();
                this.loadDataCounter = 0;
            } else {
                this.message = "No data found, please contact support";
            }
        };
    }

    /**
     * Handle parking lots loading error
     */
    private handleLoadError() {
        return (error) => {
            setTimeout(() => {
                if (++this.loadDataCounter <= 5) {
                    this.message = 'Connection lost. Please wait...';
                    // console.log(this.loadDataCounter);
                    this.loadData();
                } else {
                    this.message =
                        'Can not connect to server. Please contact support';
                    this.updateSubscription.unsubscribe();
                    this.loadDataSubscription.unsubscribe();
                }
            }, 7000);
        };
    }

    /**
     * If server response contains less the specified number of parking lots
     * they will be filled with mock
     */
    private fillMissingLots() {
        if (this.parkingLots.length < 10) {
            for (let i = 1; i <= 10; i++) {
                const pl = new ParkingLot();
                if (!this.parkingLots.find(getParkingLotByNumberPredicate(i))) {
                    pl.number = pl.id = i;
                    this.parkingLots.push(pl);
                }
            }
        }
    }

    /**
     * Sort parking lots
     * @param data - parking lots
     */
    private sortParkingLots(data) {
        this.parkingLots = data.sort(getParkingLotsComparator());
    }

    /**
     * Highlight selected parking lot on parking layout
     * @param id - target parking lot id
     */
    private highlightSelectedLot(id) {
        const flashingInterval = setInterval(() => {
            this.classApplied[this.parkingLots[id - 1].number] = !this
                .classApplied[this.parkingLots[id - 1].number];
        }, 500);

        setTimeout(() => {
            clearInterval(flashingInterval);
            this.classApplied[this.parkingLots[id - 1].number] = false;
        }, 7000);
    }

    /**
     * Refresh data
     */
    private refresh() {
        this.loadData();
        this.navigationService.navigateToParkingLayout();
    }

    /**
     * Show parking lot details
     * @param id - target parking lot id
     */
    private showDetails(id: number) {
        this.navigationService.navigateToParkingLotDetail(id, appRoutes.layout);
        this.selectedParkingLot = this.parkingLots.find(getParkingLotByIdPredicate(id));
        this.subscribeOnUrlParams();
    }

    /**
     * Initializes an auxiliary parking lot highlight array
     */
    private fillClassHighlightArray() {
        for (let i = 0; i < 10; i++) {
            this.classApplied.push(false);
        }
    }
}
