import {Component, OnInit} from '@angular/core';
import {actions} from '@app/services/navigation/app.endpoints';
import {NavigationService} from '@app/services/navigation/navigation.service';


@Component({
    selector: 'app-access-denied',
    templateUrl: './access-denied.component.html',
    styleUrls: ['./access-denied.component.scss']
})
export class AccessDeniedComponent implements OnInit {

    private unauthorized = false;

    private forbidden = false;

    constructor(private navigationService: NavigationService) {
        this.subscribeUrlParams();
    }

    ngOnInit() {
    }

    private subscribeUrlParams() {
        // console.log('Called Constructor');
        this.navigationService.subscribeUrlParams(this.getQueryParamsCallback());
    }

    private getQueryParamsCallback() {
        return params => {
            if (params.action === actions.unauthorized) {
                this.unauthorized = true;
            }
            if (params.action === actions.forbidden) {
                this.forbidden = true;
            }
        };
    }
}
