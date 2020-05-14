import { Injectable } from '@angular/core';
import { ascendingStatisticsSortByTime, descendingStatisticsSortByTime } from '@app/models/Statistics';
import { StatsDateSortable } from '@app/services/data/data.service';


@Injectable({
    providedIn: 'root'
})
export class StatisticsService {

    constructor() {
    }

    /**
     * Sort statistics data by parking lot updated time
     */
    public sortStatisticsTableByTime(componentWithStats: StatsDateSortable) {

        for (let i = 0; i < componentWithStats.filteredStatistics.length - 1; i++) {
            if (componentWithStats.filteredStatistics[i].updatedAt.getTime() >
                componentWithStats.filteredStatistics[i + 1].updatedAt.getTime()) {
                componentWithStats.timeSortedDesc = true;
                componentWithStats.timeSortedAsc = false;
            }

            if (componentWithStats.filteredStatistics[i].updatedAt.getTime() <
                componentWithStats.filteredStatistics[i + 1].updatedAt.getTime()) {
                componentWithStats.timeSortedAsc = true;
                componentWithStats.timeSortedDesc = false;
            }
        }

        if (componentWithStats.timeSortedAsc) {
            componentWithStats.filteredStatistics.sort(descendingStatisticsSortByTime());

            componentWithStats.timeSortedAsc = false;
            componentWithStats.timeSortedDesc = true;
        } else {
            componentWithStats.filteredStatistics.sort(ascendingStatisticsSortByTime());

            componentWithStats.timeSortedDesc = false;
            componentWithStats.timeSortedAsc = true;
        }

        componentWithStats.lotSortedAsc = false;
        componentWithStats.lotSortedDesc = false;

        componentWithStats.dateSortedDesc = false;
        componentWithStats.dateSortedAsc = false;
    }
}
