import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from "@angular/router";
import { Observable } from "rxjs";
import { appRoutes } from "../app.endpoints";


/**
 * Guard that does not allow direct manual page access from browser window
 */
@Injectable()
export class DirectAccessGuard implements CanActivate {
    constructor(private router: Router) {
    }

    /**
     * Check if this page can be accessed directly
     * @param next - activated route
     * @param state - activated route state
     */
    canActivate(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<boolean> | Promise<boolean> | boolean {
        // If the previous URL was blank, then the user is directly accessing this page
        if (this.router.url === "/") {
            this.router.navigate([appRoutes.main]); // Navigate away to some other page
            return false;
        }
        return true;
    }
}
