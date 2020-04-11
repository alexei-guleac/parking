import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot,} from '@angular/router';
import {api, appRoutes} from '@app/services/navigation/app.endpoints';
import {containsString} from '@app/utils/string-utils';
import {Observable} from 'rxjs';


@Injectable({
    providedIn: 'root',
})
export class ResetGuard implements CanActivate {
    constructor(private router: Router) {
    }

    canActivate(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<boolean> | Promise<boolean> | boolean {
        const navigatedFrom: string = this.router.url;
        console.log('from ' + navigatedFrom);

        if (
            containsString(navigatedFrom, api.confirmReset) &&
            containsString(navigatedFrom, 'confirmation_token')
        ) {
            const targetUrl: string = state.url;
            console.log('state' + targetUrl);
            if (targetUrl.includes(appRoutes.reset)) {
                return true;
            }
        }
        this.router.navigate([appRoutes.main]); // Navigate away to some other page
        return false;
    }
}
