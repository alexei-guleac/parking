import { Injectable } from "@angular/core";
import { ParkingLot } from "@app/models/ParkingLot";
import { parkingStatuses } from "@app/models/ParkingLotStatus";
import { Statistics } from "@app/models/Statistics";
import { User } from "@app/models/User";


/**
 * Mockup service for retrieving parking lots from memory without backend
 */
@Injectable({
    providedIn: "root"
})
export class DataService {

    users: Array<User>;

    private readonly parkingLots: Array<ParkingLot>;

    private readonly statistics: Array<Statistics>;

    private readonly getRandomNum: any;

    private readonly date: Date;

    constructor() {
        this.parkingLots = new Array<ParkingLot>();

        this.getRandomNum = () => {
            return Math.floor(Math.random() * 3 + 1);
        };

        for (let i = 0; i < 10; i++) {
            const parkingLot = new ParkingLot();
            parkingLot.id = i;
            parkingLot.number = i;
            parkingLot.status =
                this.getRandomNum === 0
                    ? parkingStatuses.FREE
                    : this.getRandomNum === 1
                    ? parkingStatuses.OCCUPIED
                    : parkingStatuses.UNKNOWN;
            parkingLot.updatedAt = new Date();

            this.parkingLots.push(parkingLot);
        }

        this.users = new Array<User>();

        const user1 = new User("u001", "Mircea", "mircea");
        const user2 = new User("u002", "Alex", "alex");
        const user3 = new User("u003", "Pavel", "pavel");
        const user4 = new User("u004", "Victor", "victor");

        this.users.push(user1);
        this.users.push(user2);
        this.users.push(user3);
        this.users.push(user4);

        this.statistics = new Array<Statistics>();

        for (let i = 0; i < 10; i++) {
            this.date = new Date("2020-01-05 9:00");
            this.date.setHours(this.date.getHours() + 10);
            const parkingLotStatus =
                this.getRandomNum === 0
                    ? parkingStatuses.FREE
                    : this.getRandomNum === 1
                    ? parkingStatuses.OCCUPIED
                    : parkingStatuses.UNKNOWN;

            const stats = new Statistics(
                i + 1,
                i + 1,
                parkingLotStatus,
                this.date
            );
            this.statistics.push(stats);
        }
    }
}
