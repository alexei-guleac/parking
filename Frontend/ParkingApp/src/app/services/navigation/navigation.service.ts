import {Injectable} from '@angular/core';
import {ActivatedRoute, NavigationExtras, Router} from '@angular/router';
import {containsString} from '../../utils/string-utils';
import {actions, appRoutes} from './app.endpoints';


@Injectable({providedIn: 'root'})
export class NavigationService {
    constructor(private router: Router, private route: ActivatedRoute) {
    }

    navigateToMain() {
        this.router.navigate([appRoutes.main]);
    }

    navigateToStatistics() {
        this.router.navigate([appRoutes.statistics]);
    }

    navigateToLayout() {
        this.router.navigate([appRoutes.layout]);
    }

    navigateToLogin() {
        this.router.navigate([appRoutes.account], {
            queryParams: {action: actions.login}
        });
    }

    navigateToLoginWithExtras(navigationExtras: NavigationExtras) {
        this.router.navigate([appRoutes.account], navigationExtras);
    }

    navigateToResetWithExtras(navigationExtras: NavigationExtras) {
        this.router.navigate([appRoutes.reset], navigationExtras);
    }

    navigateTo404() {
        this.router.navigate([appRoutes.notFound]);
    }

    navigateToServerNotRunning() {
        this.router.navigate([appRoutes.main], {
            queryParams: {action: actions.serverError}
        });
    }

    subscribeUrlParams(queryParamsCallback: (args: any) => void) {
        // console.log('Called subscribeUrlParams');
        this.route.queryParams.subscribe(queryParamsCallback);
    }

    assertUrlPath(route: string) {
        const urlPath = this.router.url;
        console.log(this.router.url);

        return containsString(urlPath, route);
    }

    getRouterState() {
        const navigation = this.router.getCurrentNavigation();
        return navigation.extras.state as any;
    }
}
