import {Injectable} from '@angular/core';
import {NavigationExtras, Router} from '@angular/router';
import {actions, routes} from './app.endpoints';


@Injectable({providedIn: 'root'})
export class NavigationService {

    constructor(
        private router: Router,
    ) {
    }

    navigateToMain() {
        this.router.navigate([routes.main]);
    }

    navigateToStatistics() {
        this.router.navigate([routes.statistics]);
    }

    navigateToLayout() {
        this.router.navigate([routes.layout]);
    }

    navigateToLogin() {
        this.router.navigate([routes.account], {queryParams: {action: actions.login}});
    }

    navigateToLoginWithExtras(navigationExtras: NavigationExtras) {
        this.router.navigate([routes.account], navigationExtras);
    }

    navigateToResetWithExtras(navigationExtras: NavigationExtras) {
        this.router.navigate([routes.reset], navigationExtras);
    }

    navigateTo404() {
        this.router.navigate([routes.notFound]);
    }

    navigateToServerNotRunning() {
        this.router.navigate([routes.main], {queryParams: {action: actions.serverError}});
    }
}
