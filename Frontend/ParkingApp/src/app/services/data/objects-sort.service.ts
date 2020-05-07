import { Injectable } from "@angular/core";


/**
 * Provides common sorting methods
 */
@Injectable({
    providedIn: "root"
})
export class ObjectsSortService {

    constructor() {
    }

    /**
     * General data sort method by specified parameters and target object field
     * @param componentObj - object reference
     * @param targetSortedAsc - sort target field ascending marker
     * @param targetSortedDesc - sort target field descending marker
     * @param dataArray - dataArray data array
     * @param sortByField - target field for sorting
     * @param otherFields - other dataArray field state
     */
    sortTable(
        componentObj,
        targetSortedAsc: string,
        targetSortedDesc: string,
        dataArray: Array<object>,
        sortByField: string,
        ...otherFields: boolean[]
    ) {
        // define how dataArray data is sorted
        if (targetSortedAsc === targetSortedDesc && componentObj[targetSortedAsc] === false) {
            for (let i = 0; i < dataArray.length - 1; i++) {
                if (dataArray[i][sortByField] > dataArray[i + 1][sortByField]) {
                    switchTargetSortOrder(false, true);
                }
                if (dataArray[i][sortByField] < dataArray[i + 1][sortByField]) {
                    switchTargetSortOrder(true, false);
                }
            }
        }
        // sort data dependent on user choice
        if (componentObj[targetSortedAsc]) {
            dataArray.sort(descendingOrderSortGeneralComparator(sortByField));
            switchTargetSortOrder(false, true);
        } else {
            dataArray.sort(ascendingOrderSortGeneralComparator(sortByField));
            switchTargetSortOrder(true, false);
        }

        // reset other fields sort marker to false
        for (let i = 0; i < otherFields.length; i++) {
            otherFields[i] = false;
        }

        function switchTargetSortOrder(targetSortedAscState, targetSortedDescState) {
            componentObj[targetSortedAsc] = targetSortedAscState;
            componentObj[targetSortedDesc] = targetSortedDescState;
        }
    }

}

/**
 * Common descending order sort comparator
 */
export function descendingOrderSortGeneralComparator(sortByField: string) {
    return (a, b) => a[sortByField] < b[sortByField] ? 1
        : a[sortByField] > b[sortByField] ? -1 : 0;
}

/**
 * Common ascending order sort comparator
 */
export function ascendingOrderSortGeneralComparator(sortByField: string) {
    return (a, b) => a[sortByField] > b[sortByField] ? 1
        : a[sortByField] < b[sortByField] ? -1 : 0;
}
