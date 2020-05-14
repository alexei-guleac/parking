import { ascendingOrderSortGeneralComparator } from '@app/services/data/objects-sort.service';


/**
 * Parking lot usage statistics record
 */
export class Statistics {
    id: number;

    lotNumber: number;

    updatedAt: Date;

    status: string;

    constructor(
        id?: number,
        parkingLotNumber?: number,
        status?: string,
        updatedAt?: Date
    ) {
        if (id) {
            this.id = id;
            this.lotNumber = parkingLotNumber;
            this.status = status;
            this.updatedAt = updatedAt;
        }
    }

    /**
     * Forms parking lot usage statistics record object from HTTP response
     * @param stats - HTTP response parking lot usage record
     */
    static fromHttp(stats: Statistics): Statistics {
        const newStats = new Statistics();
        newStats.id = stats.id;
        newStats.lotNumber = stats.lotNumber;
        newStats.status = stats.status;
        newStats.updatedAt = new Date(stats.updatedAt);
        return newStats;
    }
}

/**
 * Statistics entries sort by lot updated at date compare function
 */
export function getStatisticsByUpdatedAtAscSortComparator() {
    return ascendingOrderSortGeneralComparator('updatedAt');
}

/**
 * Statistics entries target calendar period filter function
 * @param startDate - specified start date
 * @param endDate - specified end date
 */
export function filterStatisticsByDatePeriod(startDate, endDate) {
    return (st) =>
        st.updatedAt >=
        new Date(startDate) &&
        st.updatedAt <= new Date(endDate);
}

/**
 * Statistics entries target lot number filter function
 * @param selectedLotNumber - target lot number
 */
export function filterStatisticsByNumber(selectedLotNumber) {
    return (st) => st.lotNumber === +selectedLotNumber;
}

/**
 * Statistics entries descending sort by lot updated at time compare function
 */
export function descendingStatisticsSortByTime() {
    return (a, b) => a.updatedAt.getTime() < b.updatedAt.getTime() ? 1
        : a.updatedAt.getTime() > b.updatedAt.getTime() ? -1 : 0;
}

/**
 * Statistics entries ascending sort by lot updated at time compare function
 */
export function ascendingStatisticsSortByTime() {
    return (a, b) => a.updatedAt.getTime() > b.updatedAt.getTime() ? 1
        : a.updatedAt.getTime() < b.updatedAt.getTime() ? -1 : 0;
}
