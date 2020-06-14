import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { parking } from '@app/constants/app-constants';
import { getParkingLotByIdPredicate, getParkingLotsComparator, ParkingLot } from '@app/models/ParkingLot';
import { parkingStatuses } from '@app/models/ParkingLotStatus';
import { AuthenticationService } from '@app/services/account/auth.service';
import { DataService, parseDataFromWsMsg } from '@app/services/data/data.service';
import { appRoutes } from '@app/services/navigation/app.endpoints';
import { NavigationService } from '@app/services/navigation/navigation.service';
import { WebSocketApiService } from '@app/services/web-socket-api.service';
import { NgbTooltip } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { delay } from 'rxjs/operators';


/**
 * Main application page
 */
@Component({
    selector: 'app-main',
    templateUrl: './main.component.html',
    styleUrls: ['./main.component.scss']
})
export class MainComponent implements OnInit {

    parkingLotStatus = parkingStatuses;

    @ViewChild('parkingLotDetailTooltip', { static: false }) parkingLotDetailTooltip: NgbTooltip;

    @ViewChild('parkingLotMapTooltip', { static: false }) parkingLotMapTooltip: NgbTooltip;

    private parkingLots: Array<ParkingLot>;

    private selectedParkingLot: ParkingLot;

    private action: string;

    private noData: Array<number>;

    private webSocketAPI: WebSocketApiService;

    private wsMsgBody: ParkingLot;

    constructor(
        private dataService: DataService,
        private router: Router,
        private route: ActivatedRoute,
        private navigationService: NavigationService,
        private authenticationService: AuthenticationService,
        private translate: TranslateService
    ) {
    }

    /**
     * Initialize the directive/component after Angular first displays the data-bound properties
     * and sets the directive/component's input properties.
     * Called once, after the first ngOnChanges()
     */
    ngOnInit() {
        this.connectToServerViaWS();

        // mock up view before server data is loaded
        this.mockData();
        this.loadData();
        this.processUrlParams();
    }

    private connectToServerViaWS() {
        this.webSocketAPI = new WebSocketApiService(this);
        this.connect();
    }

    private mockData() {
        this.noData = new Array<number>();
        for (let i = 1; i <= parking.lotsNumber; i++) {
            this.noData.push(i);
        }
    }

    /**
     * Refresh parking lots data
     */
    async refresh() {
        this.loadData();
        // delays for data refresh guarantee
        await delay(500);
        await this.router.navigate([appRoutes.main]);
        await delay(1500);
        this.loadData();
    }

    /**
     * Method, which needs to identify each element uniquely,
     * in the associated component and assign it to the ngFor directive
     * @param index - element unique index
     * @param elem - data element
     */
    trackByMethod(index: number, elem: any): number {
        return elem.id;
    }

    /**
     * Prevent showing parking lot parent div tooltip when child tooltip is showing
     */
    preventParentTooltip() {
        this.parkingLotDetailTooltip.close();
        this.parkingLotDetailTooltip.disableTooltip = true;
    }

    /**
     * Allow showing parking lot parent div tooltip when child element is leaved
     */
    allowParentTooltip(parentElement = null) {
        this.parkingLotDetailTooltip.disableTooltip = false;

        /*const isHovered = e => !!(e.querySelector(':hover') || e.parentNode.querySelector(':hover') === e);
        const hovered = isHovered(parentElement);
        // allow parent tooltip only if it still hovered
        if (hovered) {
            this.parkingLotDetailTooltip.open();
        }*/
    }

    /**
     * Subscribing to query string URL parameters
     */
    private processUrlParams() {
        this.route.queryParams.subscribe(
            (params) => (this.action = params.action)
        );
    }

    /**
     * Load page data
     */
    private loadData() {
        this.dataService.getAllParkingLots().subscribe(
            this.handleParkingLotsResponse(),
            this.handleParkingLotsServerError()
        );
    }

    /**
     * Handle parking lots server response
     */
    private handleParkingLotsResponse() {
        return (data) => {
            // if data is provided
            if (data.length !== 0) {
                // sort data
                this.parkingLots = data.sort(getParkingLotsComparator());
            } else {
                this.parkingLots = null;
            }
        };
    }

    /**
     * Handle parking lots server error
     */
    private handleParkingLotsServerError() {
        return (error) => {
            this.parkingLots = null;
        };
    }

    /**
     * Show parking lot detail by switching to parking lot detail layout
     * @param id - target parking lot id
     */
    private showDetails(id: number) {
        this.navigationService.navigateToParkingLotDetail(id, appRoutes.main);
        this.selectedParkingLot = this.parkingLots.find(getParkingLotByIdPredicate(id));
        this.processUrlParams();
    }

    /**
     * Check if user is logged in
     */
    private isUserLoggedIn() {
        return this.authenticationService.isUserLoggedIn();
    }

    /**
     * Show parking lot on map by navigating to parking layout
     * @param id - target parking lot id
     */
    private showParkingLotOnMap(id: number) {
        this.navigationService.navigateToParkingLayoutWithExtras(id);
    }

    connect() {
        this.webSocketAPI.connect();
    }

    disconnect() {
        this.webSocketAPI.disconnect();
    }

    sendMessage(message: string) {
        this.webSocketAPI.send(message);
    }

    handleMessage(message: any) {
        // double parse to get valid ws message body object
        this.wsMsgBody = parseDataFromWsMsg(message);

        if (this.wsMsgBody.number && this.wsMsgBody.status) {
            // console.log(this.wsMsgBody.number);
            const mapLots = (lot: ParkingLot) => {
                if (lot.number === this.wsMsgBody.number) {
                    lot.status = this.wsMsgBody.status;
                }
                return lot;
            };

            this.parkingLots.map(mapLots);
        }
    }
}
