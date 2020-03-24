import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {delay} from 'rxjs/operators';
import {ParkingLot} from '../../models/ParkingLot';
import {status} from "../../models/ParkingLotStatus";
import {DataService} from '../../services/data/data.service';
import {actions, appRoutes} from '../../services/navigation/app.endpoints';


@Component({
    selector: 'app-main',
    templateUrl: './main.component.html',
    styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {

    private parkingLots: Array<ParkingLot>;
    private selectedParkingLot: ParkingLot;
    private action: string;

    parkingLotStatus = status;

    private noData: Array<number>;

    constructor(private dataService: DataService,
                private router: Router,
                private route: ActivatedRoute) {
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

    private loadData() {
        this.dataService.getAllParkingLots().subscribe(
            data => {
                if (data.length !== 0) {
                    this.parkingLots = data.sort((a, b) => (a.number > b.number) ? 1 : (a.number < b.number ? -1 : 0));
                } else {
                    this.parkingLots = null;
                }

            },
            error => {
                this.parkingLots = null;
            }
        );
    }

    private processUrlParams() {
        this.route.queryParams.subscribe(
            // tslint:disable-next-line: no-string-literal
            params => this.action = params['action']
        );
    }

    private showDetails(id: number) {
        this.router.navigate([appRoutes.main], {queryParams: {id, action: actions.view}});
        this.selectedParkingLot = this.parkingLots.find(pl => pl.id === id);
        this.processUrlParams();
    }
}
