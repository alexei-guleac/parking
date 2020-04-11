import {formatDate} from '@angular/common';
import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ParkingLot} from '@app/models/ParkingLot';
import {parkingStatuses} from '@app/models/ParkingLotStatus';
import {Statistics} from '@app/models/Statistics';
import {DataService} from '@app/services/data/data.service';
import {appRoutes} from '@app/services/navigation/app.endpoints';


@Component({
    selector: 'app-statistics',
    templateUrl: './statistics.component.html',
    styleUrls: ['./statistics.component.scss'],
})
export class StatisticsComponent implements OnInit {
    private p = 1; // declaration of page index used for pagination

    // set the table row color depending on status
    private colors = [
        {status: parkingStatuses.FREE, background: '#28a745'},
        {status: parkingStatuses.OCCUPIED, background: '#dc3545'},
        {status: parkingStatuses.UNKNOWN, background: 'gray'},
        {status: parkingStatuses.RESERVED, background: '#ffbf0f'},
    ];

    private statistics: Array<Statistics>;

    private filteredStatistics: Array<Statistics>;

    private parkingLots: Array<ParkingLot>;

    private lotNumber = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];

    private selectedLotNumber: string = null;

    private startDate: string;

    private endDate: string;

    private lotSortedAsc = false;

    private lotSortedDesc = false;

    private dateSortedAsc = false;

    private dateSortedDesc = false;

    private timeSortedAsc = false;

    private timeSortedDesc = false;

    constructor(
        private dataService: DataService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.loadData();
    }

    private loadData() {
        // tslint:disable: no-string-literal
        // this.parkingLots = this.route.snapshot.data['parkingLots'];
        // this.parkingLots.sort((a, b) => (a.number > b.number) ? 1 : (a.number < b.number ? -1 : 0));

        this.statistics = this.route.snapshot.data[appRoutes.statistics];
        this.statistics.sort((a, b) =>
            a.updatedAt > b.updatedAt ? 1 : a.updatedAt < b.updatedAt ? -1 : 0
        );

        this.filteredStatistics = this.statistics;
        this.selectedLotNumber = undefined;

        this.startDate = this.endDate = formatDate(
            new Date(),
            'yyyy-MM-dd',
            'en-UK'
        );
        this.filterData();
    }

    private filterData() {
        let tempStats = new Array<Statistics>();

        if (this.selectedLotNumber === 'All') {
            this.selectedLotNumber = null;
        }

        if (
            new Date(this.startDate).getDate() >
            new Date(this.endDate).getDate()
        ) {
            alert('The start date you entered is higher that the end date');
        } else if (this.startDate != null && this.endDate != null) {
            tempStats = this.statistics.filter(
                (st) =>
                    st.updatedAt.getDate() >=
                    new Date(this.startDate).getDate() &&
                    st.updatedAt.getDate() <= new Date(this.endDate).getDate()
            );

            if (this.selectedLotNumber != null) {
                tempStats = tempStats.filter(
                    (st) => st.lotNumber === +this.selectedLotNumber
                );
            }
        }
        this.filteredStatistics = tempStats;
    }

    private sortTableByLotNumber() {
        this.sortTable(
            this.lotSortedAsc,
            this.lotSortedDesc,
            this.filteredStatistics,
            'lotNumber',
            (this.dateSortedAsc = false),
            (this.dateSortedDesc = false),
            (this.timeSortedDesc = false),
            (this.timeSortedAsc = false)
        );
    }

    private sortTableByDate() {
        this.sortTable(
            this.dateSortedAsc,
            this.dateSortedDesc,
            this.filteredStatistics,
            'updatedAt',
            (this.lotSortedAsc = false),
            (this.lotSortedDesc = false),
            (this.timeSortedDesc = false),
            (this.timeSortedAsc = false)
        );
    }

    private sortTable(
        targetSortedAsc: boolean,
        targetSortedDesc: boolean,
        statistics: Array<Statistics>,
        sortByField: string,
        ...otherFields: boolean[]
    ) {
        targetSortedAsc = true;
        targetSortedDesc = true;

        for (let i = 0; i < statistics.length - 1; i++) {
            if (statistics[i][sortByField] > statistics[i + 1][sortByField]) {
                targetSortedAsc = false;
            }
            if (statistics[i][sortByField] < statistics[i + 1][sortByField]) {
                targetSortedDesc = false;
            }
        }

        if (targetSortedAsc) {
            statistics.sort((a, b) =>
                a[sortByField] < b[sortByField]
                    ? 1
                    : a[sortByField] > b[sortByField]
                    ? -1
                    : 0
            );
            targetSortedAsc = false;
            targetSortedDesc = true;
        } else {
            statistics.sort((a, b) =>
                a[sortByField] > b[sortByField]
                    ? 1
                    : a[sortByField] < b[sortByField]
                    ? -1
                    : 0
            );
            targetSortedDesc = false;
            targetSortedAsc = true;
        }

        for (let i = 0; i < otherFields.length; i++) {
            otherFields[i] = false;
        }
    }

    private sortTableByTime() {
        this.timeSortedDesc = true;
        this.timeSortedAsc = true;

        for (let i = 0; i < this.filteredStatistics.length - 1; i++) {
            if (
                this.filteredStatistics[i].updatedAt.getTime() >
                this.filteredStatistics[i + 1].updatedAt.getTime()
            ) {
                this.timeSortedAsc = false;
            }

            if (
                this.filteredStatistics[i].updatedAt.getTime() <
                this.filteredStatistics[i + 1].updatedAt.getTime()
            ) {
                this.timeSortedDesc = false;
            }
        }

        if (this.timeSortedAsc) {
            this.filteredStatistics.sort((a, b) =>
                a.updatedAt.getTime() < b.updatedAt.getTime()
                    ? 1
                    : a.updatedAt.getTime() > b.updatedAt.getTime()
                    ? -1
                    : 0
            );

            this.timeSortedAsc = false;
            this.timeSortedDesc = true;
        } else {
            this.filteredStatistics.sort((a, b) =>
                a.updatedAt.getTime() > b.updatedAt.getTime()
                    ? 1
                    : a.updatedAt.getTime() < b.updatedAt.getTime()
                    ? -1
                    : 0
            );

            this.timeSortedDesc = false;
            this.timeSortedAsc = true;
        }

        this.lotSortedAsc = false;
        this.lotSortedDesc = false;

        this.dateSortedDesc = false;
        this.dateSortedAsc = false;
    }

    private getColor(parkingLotStatus: string) {
        return this.colors.filter((item) => item.status === parkingLotStatus)[0]
            .background;
    }
}
