import {AfterViewInit, Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ParkingLot} from '@app/models/ParkingLot';
import {parkingStatuses} from '@app/models/ParkingLotStatus';
import {DataService} from '@app/services/data/data.service';
import {actions, appRoutes} from '@app/services/navigation/app.endpoints';
import {NavigationService} from '@app/services/navigation/navigation.service';
import {interval, Subscription} from 'rxjs';


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

    private numberOfParkingLots: Array<number> = new Array<number>(
        0,
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9
    );

    private classApplied: Array<boolean> = new Array<boolean>();

    private selectedLotId: any;

    constructor(
        private dataService: DataService,
        private route: ActivatedRoute,
        private router: Router,
        private navigationService: NavigationService
    ) {
    }

    ngOnInit() {
        this.loadData();
        this.fillClassHighlightArray();
        this.subscribeOnUrlParams();

        this.updateSubscription = interval(3000).subscribe(() => {
            this.loadData();
        });
    }

    ngOnDestroy() {
        this.updateSubscription.unsubscribe();
        this.loadDataSubscription.unsubscribe();
    }

    loadData() {
        this.loadDataSubscription = this.dataService
            .getAllParkingLots()
            .subscribe(this.handleData(), this.handleLoadError());
    }

    ngAfterViewInit() {
        this.highlightSelectedLot(this.selectedLotId);
    }

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

    private handleData() {
        return (data) => {
            if (data.length !== 0) {
                this.sortParkingLots(data);
                this.dataLoaded = true;
                this.message = '';
                // console.log('loadData');

                this.fillMissingLots();
                this.loadDataCounter = 0;
            } else {
                this.message = 'No data found, please contact support';
            }
        };
    }

    private fillMissingLots() {
        if (this.parkingLots.length < 10) {
            for (let i = 1; i <= 10; i++) {
                const pl = new ParkingLot();
                if (!this.parkingLots.find((lot) => lot.number === i)) {
                    pl.number = pl.id = i;
                    this.parkingLots.push(pl);
                }
            }
        }
    }

    private sortParkingLots(data) {
        this.parkingLots = data.sort((a, b) =>
            a.number > b.number ? 1 : a.number < b.number ? -1 : 0
        );
    }

    private subscribeOnUrlParams() {
        this.route.queryParams.subscribe(
            // tslint:disable-next-line: no-string-literal
            this.getQueryparamsCallback()
        );
    }

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

    private highlightSelectedLot(id) {
        console.log('ID lot ' + id);
        /*const selectors = '.lot-top';
        console.log(selectors);
        const elem = document.querySelector(selectors);
        console.log(elem);*/

        const flashingInterval = setInterval(() => {
            this.classApplied[this.parkingLots[id - 1].number] = !this
                .classApplied[this.parkingLots[id - 1].number];
        }, 500);
        setTimeout(() => {
            clearInterval(flashingInterval);
            this.classApplied[this.parkingLots[id - 1].number] = false;
        }, 7000);
    }

    private refresh() {
        this.loadData();
        this.navigationService.navigateToParkingLayout();
    }

    private showDetails(id: number) {
        this.navigationService.navigateToParkingLotDetail(id, appRoutes.layout);
        this.selectedParkingLot = this.parkingLots.find((pl) => pl.id === id);
        this.subscribeOnUrlParams();
    }

    private fillClassHighlightArray() {
        for (let i = 0; i < 10; i++) {
            this.classApplied.push(false);
        }
    }
}
