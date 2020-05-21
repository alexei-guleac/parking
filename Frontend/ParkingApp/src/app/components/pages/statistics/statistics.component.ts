import { formatDate } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { parking } from '@app/constants/app-constants';
import { parkingColors } from '@app/models/ParkingLotStatus';
import {
    filterStatisticsByDatePeriod,
    filterStatisticsByNumber,
    getStatisticsByUpdatedAtAscSortComparator, getStatisticsByUpdatedAtDescSortComparator,
    Statistics
} from '@app/models/Statistics';
import { DataService, StatsDateSortable } from '@app/services/data/data.service';
import { ObjectsSortService } from '@app/services/data/objects-sort.service';
import { StatisticsService } from '@app/services/data/statistics.service';
import { appRoutes } from '@app/services/navigation/app.endpoints';


/**
 * Parking lots usage statistics page
 */
@Component({
    selector: 'app-statistics',
    templateUrl: './statistics.component.html',
    styleUrls: ['./statistics.component.scss']
})
export class StatisticsComponent implements OnInit, StatsDateSortable {

    private statistics: Array<Statistics>;

    filteredStatistics: Array<Statistics>;

    lotSortedAsc = false;

    private p = 1;              // declaration of page index used for pagination

    lotSortedDesc = false;

    private selectedLotNumber: string = null;

    private startDate: string;

    private endDate: string;

    dateSortedAsc = false;

    dateSortedDesc = false;

    timeSortedAsc = false;

    timeSortedDesc = false;

    private lotNumber = [];

    // set the table row color depending on status
    private colors = parkingColors;

    constructor(
        private dataService: DataService,
        private route: ActivatedRoute,
        private objectsSortService: ObjectsSortService,
        private statisticsService: StatisticsService
    ) {
        for (let i = 1; i <= parking.lotsNumber; i++) {
            this.lotNumber.push(i);
        }
    }

    /**
     * Initialize the directive/component after Angular first displays the data-bound properties
     * and sets the directive/component's input properties.
     * Called once, after the first ngOnChanges()
     */
    ngOnInit() {
        this.loadData();
    }

    /**
     * Load parking lots statistics data
     */
    private loadData() {
        this.statistics = this.route.snapshot.data[appRoutes.statistics];

        // preliminarily sort statistics by date parking lot status updated in ascending order
        this.statistics.sort(getStatisticsByUpdatedAtDescSortComparator());
        // reset other fields sort order by default
        this.resetFieldsSort();

        this.filteredStatistics = this.statistics;
        this.selectedLotNumber = undefined;

        // @ts-ignore
        Date.prototype.subtractDays = function(d) {
            this.setTime(this.getTime() - (d * 24 * 60 * 60 * 1000));
            return this;
        };

        const dateFormat = 'yyyy-MM-dd';
        const dateLocale = 'en-UK';
        const dateStart = new Date();
        this.startDate = formatDate(
            dateStart.setDate(dateStart.getDate() - parking.statisticsDaysScheduleDelete),
            dateFormat,
            dateLocale
        );
        const dateEnd = new Date();
        this.endDate = formatDate(
            dateEnd.setDate(dateEnd.getDate() + 1),
            dateFormat,
            dateLocale
        );
        // console.log(this.endDate);
        this.filterData();
    }

    /**
     * Filter statistics data by parameters selected by user
     */
    private filterData() {
        let tempStats = new Array<Statistics>();

        // reset to first page after lot number option selected
        this.p = 1;

        // if all selected
        if (this.selectedLotNumber === 'All') {
            this.selectedLotNumber = null;
        }

        // if start date is greater than end date
        if (
            new Date(this.startDate) >
            new Date(this.endDate)
        ) {
            alert('The start date you entered is higher that the end date');
        } else if (this.startDate != null && this.endDate != null) {
            tempStats = this.statistics.filter(
                filterStatisticsByDatePeriod(this.startDate, this.endDate)
            );

            if (this.selectedLotNumber != null) {
                tempStats = tempStats.filter(
                    filterStatisticsByNumber(this.selectedLotNumber)
                );
            }
        }
        this.filteredStatistics = tempStats;
    }

    /**
     * Sort statistics data by parking lot number
     */
    private sortStatisticsTableByLotNumber() {
        this.objectsSortService.sortTable(
            this,
            'lotSortedAsc',
            'lotSortedDesc',
            this.filteredStatistics,
            'lotNumber',
            (this.dateSortedAsc = false),
            (this.dateSortedDesc = false),
            (this.timeSortedDesc = false),
            (this.timeSortedAsc = false)
        );
    }

    /**
     * Sort statistics data by parking lot updated date
     */
    private sortStatisticsTableByDate() {
        this.objectsSortService.sortTable(
            this,
            'dateSortedAsc',
            'dateSortedDesc',
            this.filteredStatistics,
            'updatedAt',
            (this.lotSortedAsc = false),
            (this.lotSortedDesc = false),
            (this.timeSortedDesc = false),
            (this.timeSortedAsc = false)
        );
    }

    /**
     * Sort statistics data by parking lot updated time
     */
    private sortStatisticsTableByTime() {
        this.statisticsService.sortStatisticsTableByTime(this);
    }

    /**
     * Define statistics table row color based on parking lot status
     * @param parkingLotStatus - target parking lot status
     */
    private getColor(parkingLotStatus: string) {
        return this.colors.filter((item) => item.status === parkingLotStatus)[0]
            .background;
    }

    private resetFieldsSort() {

        this.dateSortedAsc = false;
        this.dateSortedDesc = true;

        this.lotSortedAsc = false;
        this.lotSortedDesc = false;

        this.timeSortedAsc = false;
        this.timeSortedDesc = false;
    }
}
