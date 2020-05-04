import { Injectable } from "@angular/core";
import { ActivatedRoute, NavigationExtras, Router } from "@angular/router";
import { actions, appRoutes } from "@app/services/navigation/app.endpoints";
import { containsString } from "@app/utils/string-utils";


/**
 * Service for application routes navigation
 */
@Injectable({providedIn: 'root'})
export class NavigationService {

    constructor(private router: Router, private route: ActivatedRoute) {
    }

    /**
     * Navigate to main page
     */
    navigateToMain() {
        this.router.navigate([appRoutes.main]);
    }

    /**
     * Navigate to parking lot page
     */
    navigateToParkingLotDetail(id: number, page) {
        this.router.navigate([page], {
            queryParams: {id, action: actions.view},
        });
    }

    /**
     * Navigate to statistics page
     */
    navigateToStatistics() {
        this.router.navigate([appRoutes.statistics]);
    }

    /**
     * Navigate to parking layout page
     */
    navigateToParkingLayout() {
        this.router.navigate([appRoutes.layout]);
    }

    /**
     * Navigate to parking lot detail page with parking lot id
     * @param id - parking lot id
     */
    navigateToParkingLayoutWithExtras(id: number) {
        this.router.navigate([appRoutes.layout], {
            queryParams: {id, action: actions.show},
        });
    }

    /**
     * Navigate to login page
     */
    navigateToLogin() {
        this.router.navigate([appRoutes.account], {
            queryParams: {action: actions.login},
        });
    }

    /**
     * Navigate to login page with additional data
     * @param navigationExtras - additional login data
     */
    navigateToLoginWithExtras(navigationExtras: NavigationExtras) {
        this.router.navigate([appRoutes.account], navigationExtras);
    }

    /**
     * Navigate to reset password page with additional data
     * @param navigationExtras - additional login data
     */
    navigateToResetWithExtras(navigationExtras: NavigationExtras) {
        this.router.navigate([appRoutes.reset], navigationExtras);
    }

    /**
     * Navigate to user profile page
     */
    navigateToUserProfile() {
        this.router.navigate([appRoutes.profile]);
    }

    /**
     * Navigate to unauthorized error page
     */
    navigateToUnauthorized() {
        this.router.navigate([appRoutes.accessDenied], {
            queryParams: {action: actions.unauthorized},
        });
    }

    /**
     * Navigate to forbidden error page
     */
    navigateToForbidden() {
        this.router.navigate([appRoutes.accessDenied], {
            queryParams: {action: actions.forbidden},
        });
    }

    /**
     * Navigate to not found error page
     */
    navigateToPageNotFound() {
        this.router.navigate([appRoutes.notFound]);
    }

    /**
     * Navigate to server not running error page
     */
    navigateToServerNotRunning() {
        this.router.navigate([appRoutes.main], {
            queryParams: {action: actions.serverError},
        });
    }

    /**
     * Subscribe to URL query params
     * @param queryParamsCallback - subscribing callback
     */
    subscribeUrlParams(queryParamsCallback: (args: any) => void) {
        this.route.queryParams.subscribe(queryParamsCallback);
    }

    /**
     * Assert that URL path matches the specified route
     * @param route - target route
     */
    assertUrlPath(route: string) {
        const urlPath = this.router.url;

        return containsString(urlPath, route);
    }

    /**
     * Get target navigation router state from navigation extras accompanying object
     * with all extras like errors etc.
     */
    getRouterState() {
        const navigation = this.router.getCurrentNavigation();
        return navigation.extras.state as any;
    }
}
