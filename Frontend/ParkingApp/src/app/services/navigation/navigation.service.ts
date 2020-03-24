import {Injectable} from '@angular/core';
import {NavigationExtras, Router} from '@angular/router';
import {actions, appRoutes} from './app.endpoints';


@Injectable({providedIn: 'root'})
export class NavigationService {

    constructor(
        private router: Router,
    ) {
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
        this.router.navigate([appRoutes.account], {queryParams: {action: actions.login}});
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
        this.router.navigate([appRoutes.main], {queryParams: {action: actions.serverError}});
    }
}
