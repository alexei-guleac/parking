import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {interval, Subscription} from 'rxjs';
import {ParkingLot} from 'src/app/models/ParkingLot';
import {DataService} from 'src/app/services/data/data.service';
import {appRoutes} from '../../../services/navigation/app.endpoints';


@Component({
    selector: 'app-parking-layout',
    templateUrl: './parking-layout-old.component.html',
    styleUrls: ['./parking-layout-old.component.css']
})
export class ParkingLayoutOldComponent implements OnInit, OnDestroy {

    noData: Array<number>;

    otherParkingLots: Array<number>;

    parkingLots: Array<ParkingLot>;

    selectedParkingLot: ParkingLot;

    action: string;

    dataLoaded = false;

    message = 'Please wait...';

    updateSubscription: Subscription;

    loadDataCounter = 0;


    constructor(private dataService: DataService,
                private route: ActivatedRoute,
                private router: Router) {
    }

    ngOnInit() {
        this.loadData();

        this.subscribeOnUrlParams();

        this.updateSubscription = interval(5000).subscribe(
            () => {
                this.loadData();
            }
        );
    }

    ngOnDestroy() {
        this.updateSubscription.unsubscribe();
    }

    loadData() {
        this.dataService.getAllParkingLots().subscribe(
            data => {
                if (data.length !== 0) {
                    this.parkingLots = data.sort((a, b) => (a.number > b.number) ? 1 : (a.number < b.number ? -1 : 0));
                    this.dataLoaded = true;
                    this.message = '';

                } else {
                    this.message = 'No data found, please contact support';
                }
            },
            error => {
                const subscribe: Subscription = interval(10000).subscribe(
                    () => {
                        if (this.loadDataCounter <= 5) {
                            this.loadData();
                        } else {
                            subscribe.unsubscribe();
                            this.updateSubscription.unsubscribe();
                            this.message = 'Can\'t connect to server. Please contact support';
                        }
                    }
                );
            }
        );
    }

    subscribeOnUrlParams() {
        this.route.queryParams.subscribe(
            // tslint:disable-next-line: no-string-literal
            params => this.action = params['action']
        );
    }

    refresh() {
        this.loadData();
        this.router.navigate([appRoutes.layout]);
    }

    showDetails(id: number) {
        this.router.navigate([appRoutes.layout], {queryParams: {id, action: 'view'}});
        this.selectedParkingLot = this.parkingLots.find(pl => pl.id === id);
        this.subscribeOnUrlParams();
    }

}
