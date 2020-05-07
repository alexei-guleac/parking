import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from "@angular/router";
import { api, appRoutes } from "@app/services/navigation/app.endpoints";
import { containsString } from "@app/utils/string-utils";
import { Observable } from "rxjs";


/**
 * Guard that does not allow access to reset password page
 */
@Injectable({
    providedIn: "root"
})
export class ResetGuard implements CanActivate {

    constructor(private router: Router) {
    }

    /**
     * Check if it is reset page
     * @param next - activated route
     * @param state - activated route state
     */
    canActivate(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<boolean> | Promise<boolean> | boolean {
        const navigatedFrom: string = this.router.url;

        if (
            containsString(navigatedFrom, api.confirmReset) &&
            containsString(navigatedFrom, "confirmation_token")
        ) {
            const targetUrl: string = state.url;
            if (targetUrl.includes(appRoutes.reset)) {
                return true;
            }
        }
        this.router.navigate([appRoutes.main]); // Navigate away to some other page
        return false;
    }
}
