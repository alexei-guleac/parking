import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {appRoutes} from '../../services/navigation/app.endpoints';


@Component({
    selector: 'app-no-connection',
    templateUrl: './no-connection.component.html',
    styleUrls: ['./no-connection.component.css']
})
export class NoConnectionComponent implements OnInit {
    constructor(private router: Router) {
    }

    ngOnInit() {
    }

    goBack() {
        this.router.navigate([appRoutes.main]);
    }
}
