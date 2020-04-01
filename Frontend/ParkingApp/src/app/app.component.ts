import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {routerTransition} from './components/animations/animations';
import {actions} from './services/navigation/app.endpoints';


@Component({
    selector: 'app-root',
    animations: [routerTransition],
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
    private action: string;
    actions = actions;

    constructor(private route: ActivatedRoute) {
    }

    ngOnInit() {
        this.processUrlParams();
    }

    processUrlParams() {
        this.route.queryParams.subscribe(
            // tslint:disable-next-line: no-string-literal
            params => (this.action = params['action'])
        );
    }

    getState(outlet) {
        return outlet.activatedRouteData.state;
    }
}
