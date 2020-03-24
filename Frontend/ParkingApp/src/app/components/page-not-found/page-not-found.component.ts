import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {appRoutes} from '../../services/navigation/app.endpoints';


@Component({
    selector: 'app-page-not-found',
    templateUrl: './page-not-found.component.html',
    styleUrls: ['./page-not-found.component.css']
})
export class PageNotFoundComponent implements OnInit {

    constructor(private router: Router) {
    }

    ngOnInit() {
    }

    goBack() {
        this.router.navigate([appRoutes.main]);
    }
}
