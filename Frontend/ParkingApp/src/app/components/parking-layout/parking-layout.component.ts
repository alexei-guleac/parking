import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {interval, Subscription} from 'rxjs';
import {ParkingLot} from 'src/app/models/ParkingLot';
import {DataService} from 'src/app/services/data/data.service';
import {status} from '../../models/ParkingLotStatus';
import {appRoutes} from "../../services/navigation/app.endpoints";
import {NavigationService} from '../../services/navigation/navigation.service';


@Component({
    selector: 'app-feature2',
    templateUrl: './parking-layout.component.html',
    styleUrls: ['./parking-layout.component.css']
})
export class ParkingLayoutComponent implements OnInit, OnDestroy {
    parkingLotStatus = status;

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

    parkingLots: Array<ParkingLot>;

    private selectedParkingLot: ParkingLot;

    action: string;

    dataLoaded = false;

    message = 'Please wait...';

    private updateSubscription: Subscription;

    private connectionLostSubscription: Subscription;

    private loadDataSubscription: Subscription;

    loadDataCounter = 0;

    constructor(
        private dataService: DataService,
        private route: ActivatedRoute,
        private router: Router,
        private navigation: NavigationService
    ) {
    }

    ngOnInit() {
        this.loadData();
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
            .subscribe(
                data => {
                    if (data.length !== 0) {
                        this.parkingLots = data.sort((a, b) =>
                            a.number > b.number
                                ? 1
                                : a.number < b.number
                                ? -1
                                : 0
                        );
                        this.dataLoaded = true;
                        this.message = '';
                        console.log('loadData');

                        if (this.parkingLots.length < 10) {
                            for (let i = 1; i <= 10; i++) {
                                const pl = new ParkingLot();
                                if (
                                    !this.parkingLots.find(
                                        lot => lot.number === i
                                    )
                                ) {
                                    pl.number = pl.id = i;
                                    this.parkingLots.push(pl);
                                }
                            }
                        }
                        this.loadDataCounter = 0;
                    } else {
                        this.message = 'No data found, please contact support';
                    }
                },
                error => {
                    setTimeout(() => {
                        if (++this.loadDataCounter <= 5) {
                            this.message = 'Connection lost. Please wait...';
                            console.log(this.loadDataCounter);
                            this.loadData();
                        } else {
                            this.message =
                                'Can\'t connect to server. Please contact support';
                            this.updateSubscription.unsubscribe();
                            this.loadDataSubscription.unsubscribe();
                        }
                    }, 7000);
                }
            );
    }

    private subscribeOnUrlParams() {
        this.route.queryParams.subscribe(
            // tslint:disable-next-line: no-string-literal
            params => (this.action = params['action'])
        );
    }

    private refresh() {
        this.loadData();
        this.navigation.navigateToLayout();
    }

    private showDetails(id: number) {
        this.router.navigate([appRoutes.layout], {
            queryParams: {id, action: 'view'}
        });
        this.selectedParkingLot = this.parkingLots.find(pl => pl.id === id);
        this.subscribeOnUrlParams();
    }
}
