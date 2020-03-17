import {Component, OnInit} from '@angular/core';
import {routes} from '../../services/navigation/app.endpoints';
import {Router} from '@angular/router';


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
        this.router.navigate([routes.main]);
    }
}
