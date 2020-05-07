import { ascendingOrderSortGeneralComparator } from "@app/services/data/objects-sort.service";


/**
 * Parking lot model
 */
export class ParkingLot {
    id: number;

    number: number;

    status: string;

    updatedAt: Date;

    /**
     * Forms parking lot object from HTTP response
     * @param pl - HTTP response parking lot
     */
    static fromHttp(pl: ParkingLot): ParkingLot {
        const newParkingLot = new ParkingLot();
        newParkingLot.id = pl.id;
        newParkingLot.number = pl.number;
        newParkingLot.status = pl.status;
        newParkingLot.updatedAt = new Date(pl.updatedAt);
        return newParkingLot;
    }
}

/**
 * Parking lots sort by number compare function
 */
export function getParkingLotsComparator() {
    return ascendingOrderSortGeneralComparator("number");
}

/**
 * Parking lots get by id predicate function
 */
export function getParkingLotByIdPredicate(id: number) {
    return (parkingLot) => parkingLot.id === id;
}

/**
 * Parking lots get by id predicate function
 */
export function getParkingLotByNumberPredicate(lotNumber: number) {
    return (parkingLot) => parkingLot.number === lotNumber;
}
