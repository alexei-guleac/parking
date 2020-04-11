import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ParkingLot} from '@app/models/ParkingLot';
import {parkingStatuses} from '@app/models/ParkingLotStatus';
import {AuthenticationService} from '@app/services/account/auth.service';
import {DataService} from '@app/services/data/data.service';
import {appRoutes} from '@app/services/navigation/app.endpoints';
import {NavigationService} from '@app/services/navigation/navigation.service';
import {delay} from 'rxjs/operators';


@Component({
    selector: 'app-main',
    templateUrl: './main.component.html',
    styleUrls: ['./main.component.scss'],
})
export class MainComponent implements OnInit {
    private parkingLots: Array<ParkingLot>;

    private selectedParkingLot: ParkingLot;

    private action: string;

    parkingLotStatus = parkingStatuses;

    private noData: Array<number>;

    constructor(
        private dataService: DataService,
        private router: Router,
        private route: ActivatedRoute,
        private navigationService: NavigationService,
        private authenticationService: AuthenticationService
    ) {
    }

    ngOnInit() {
        this.noData = new Array<number>();
        for (let i = 1; i <= 10; i++) {
            this.noData.push(i);
        }
        this.loadData();
        this.processUrlParams();
    }

    async refresh() {
        this.loadData();
        await delay(500);
        await this.router.navigate([appRoutes.main]);
        await delay(1500);
        this.loadData();
    }

    trackByMethod(index: number, el: any): number {
        return el.id;
    }

    private loadData() {
        this.dataService.getAllParkingLots().subscribe(
            (data) => {
                if (data.length !== 0) {
                    this.parkingLots = data.sort((a, b) =>
                        a.number > b.number ? 1 : a.number < b.number ? -1 : 0
                    );
                } else {
                    this.parkingLots = null;
                }
            },
            (error) => {
                this.parkingLots = null;
            }
        );
    }

    private processUrlParams() {
        this.route.queryParams.subscribe(
            // tslint:disable-next-line: no-string-literal
            (params) => (this.action = params['action'])
        );
    }

    private showDetails(id: number) {
        this.navigationService.navigateToParkingLotDetail(id, appRoutes.main);
        this.selectedParkingLot = this.parkingLots.find((pl) => pl.id === id);
        this.processUrlParams();
    }

    private isUserLoggedIn() {
        return this.authenticationService.isUserLoggedIn();
    }

    private showParkingLotOnMap(id: number) {
        this.navigationService.navigateToParkingLayoutWithExtras(id);
    }
}
