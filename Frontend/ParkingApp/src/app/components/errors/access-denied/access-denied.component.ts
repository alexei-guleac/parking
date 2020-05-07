import { Component } from "@angular/core";
import { actions } from "@app/services/navigation/app.endpoints";
import { NavigationService } from "@app/services/navigation/navigation.service";


/**
 * Access denied error page (401 unauthorized or 403 forbidden)
 */
@Component({
    selector: "app-access-denied",
    templateUrl: "./access-denied.component.html",
    styleUrls: ["./access-denied.component.scss"]
})
export class AccessDeniedComponent {

    private unauthorized = false;

    private forbidden = false;

    constructor(private navigationService: NavigationService) {
        this.subscribeUrlParams();
    }

    /**
     * Subscribing to query string URL parameters
     */
    private subscribeUrlParams() {
        this.navigationService.subscribeUrlParams(this.getQueryParamsCallback());
    }

    /**
     * Callback function for processing query string URL parameters
     */
    private getQueryParamsCallback() {
        return params => {
            // switch page messages and representation
            if (params.action === actions.unauthorized) {
                this.unauthorized = true;
            }
            if (params.action === actions.forbidden) {
                this.forbidden = true;
            }
        };
    }
}
