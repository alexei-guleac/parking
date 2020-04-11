import {Injectable} from '@angular/core';
import {ActivatedRoute, NavigationExtras, Router} from '@angular/router';
import {actions, appRoutes} from '@app/services/navigation/app.endpoints';
import {containsString} from '@app/utils/string-utils';


@Injectable({providedIn: 'root'})
export class NavigationService {
    constructor(private router: Router, private route: ActivatedRoute) {
    }

    navigateToMain() {
        this.router.navigate([appRoutes.main]);
    }

    navigateToParkingLotDetail(id: number, page) {
        this.router.navigate([page], {
            queryParams: {id, action: actions.view},
        });
    }

    navigateToStatistics() {
        this.router.navigate([appRoutes.statistics]);
    }

    navigateToParkingLayout() {
        this.router.navigate([appRoutes.layout]);
    }

    navigateToParkingLayoutWithExtras(id: number) {
        this.router.navigate([appRoutes.layout], {
            queryParams: {id, action: actions.show},
        });
    }

    navigateToLogin() {
        this.router.navigate([appRoutes.account], {
            queryParams: {action: actions.login},
        });
    }

    navigateToLoginWithExtras(navigationExtras: NavigationExtras) {
        this.router.navigate([appRoutes.account], navigationExtras);
    }

    navigateToResetWithExtras(navigationExtras: NavigationExtras) {
        this.router.navigate([appRoutes.reset], navigationExtras);
    }

    navigateToUserProfile() {
        this.router.navigate([appRoutes.profile]);
    }

    navigateToUnauthorized() {
        this.router.navigate([appRoutes.accessDenied], {
            queryParams: {action: actions.unauthorized},
        });
    }

    navigateToForbidden() {
        this.router.navigate([appRoutes.accessDenied], {
            queryParams: {action: actions.forbidden},
        });
    }

    navigateToPageNotFound() {
        this.router.navigate([appRoutes.notFound]);
    }

    navigateToServerNotRunning() {
        this.router.navigate([appRoutes.main], {
            queryParams: {action: actions.serverError},
        });
    }

    subscribeUrlParams(queryParamsCallback: (args: any) => void) {
        // console.log('Called subscribeUrlParams');
        this.route.queryParams.subscribe(queryParamsCallback);
    }

    assertUrlPath(route: string) {
        const urlPath = this.router.url;
        // console.log(this.router.url);

        return containsString(urlPath, route);
    }

    getRouterState() {
        const navigation = this.router.getCurrentNavigation();
        return navigation.extras.state as any;
    }
}
