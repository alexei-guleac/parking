import { formatDate } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { parkingStatuses } from "@app/models/ParkingLotStatus";
import {
    ascendingStatisticsSortByTime,
    descendingStatisticsSortByTime,
    filterStatisticsByDatePeriod,
    filterStatisticsByNumber,
    getStatisticsByUpdatedAtAscSortComparator,
    Statistics
} from "@app/models/Statistics";
import { DataService } from "@app/services/data/data.service";
import { ObjectsSortService } from "@app/services/data/objects-sort.service";
import { appRoutes } from "@app/services/navigation/app.endpoints";


/**
 * Parking lots usage statistics page
 */
@Component({
    selector: "app-statistics",
    templateUrl: "./statistics.component.html",
    styleUrls: ["./statistics.component.scss"]
})
export class StatisticsComponent implements OnInit {

    private statistics: Array<Statistics>;

    private filteredStatistics: Array<Statistics>;

    private lotNumber = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];

    private p = 1;              // declaration of page index used for pagination

    // set the table row color depending on status
    private colors = [
        { status: parkingStatuses.FREE, background: "#28a745" },
        { status: parkingStatuses.OCCUPIED, background: "#dc3545" },
        { status: parkingStatuses.UNKNOWN, background: "gray" },
        { status: parkingStatuses.RESERVED, background: "#ffbf0f" }
    ];

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
        private route: ActivatedRoute,
        private objectsSortService: ObjectsSortService
    ) {
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
        this.statistics.sort(getStatisticsByUpdatedAtAscSortComparator());
        // reset other fields sort order by default
        this.resetFieldsSort();

        this.filteredStatistics = this.statistics;
        this.selectedLotNumber = undefined;

        this.startDate = this.endDate = formatDate(
            new Date(),
            "yyyy-MM-dd",
            "en-UK"
        );
        this.filterData();
    }

    /**
     * Filter statistics data by parameters selected by user
     */
    private filterData() {
        let tempStats = new Array<Statistics>();

        console.log("new Date(this.startDate).getDate()" + (new Date(this.startDate).setHours(0, 0, 0)));
        console.log("new Date(this.endDate).getDate()" + new Date(this.endDate));

        // if all selected
        if (this.selectedLotNumber === "All") {
            this.selectedLotNumber = null;
        }

        // if start date is greater than end date
        if (
            new Date(this.startDate) >
            new Date(this.endDate)
        ) {
            alert("The start date you entered is higher that the end date");
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
        console.log("this.filteredStatistics");
        console.log(this.filteredStatistics);
        console.log(tempStats);
    }

    /**
     * Sort statistics data by parking lot number
     */
    private sortStatisticsTableByLotNumber() {
        this.objectsSortService.sortTable(
            this,
            "lotSortedAsc",
            "lotSortedDesc",
            this.filteredStatistics,
            "lotNumber",
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
            "dateSortedAsc",
            "dateSortedDesc",
            this.filteredStatistics,
            "updatedAt",
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

        for (let i = 0; i < this.filteredStatistics.length - 1; i++) {
            if (this.filteredStatistics[i].updatedAt.getTime() >
                this.filteredStatistics[i + 1].updatedAt.getTime()) {
                this.timeSortedDesc = true;
                this.timeSortedAsc = false;
            }

            if (this.filteredStatistics[i].updatedAt.getTime() <
                this.filteredStatistics[i + 1].updatedAt.getTime()) {
                this.timeSortedAsc = true;
                this.timeSortedDesc = false;
            }
        }

        if (this.timeSortedAsc) {
            this.filteredStatistics.sort(descendingStatisticsSortByTime());

            this.timeSortedAsc = false;
            this.timeSortedDesc = true;
        } else {
            this.filteredStatistics.sort(ascendingStatisticsSortByTime());

            this.timeSortedDesc = false;
            this.timeSortedAsc = true;
        }

        this.lotSortedAsc = false;
        this.lotSortedDesc = false;

        this.dateSortedDesc = false;
        this.dateSortedAsc = false;
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

        this.dateSortedAsc = true;
        this.dateSortedDesc = false;

        this.lotSortedAsc = false;
        this.lotSortedDesc = false;

        this.timeSortedAsc = false;
        this.timeSortedDesc = false;
    }
}
