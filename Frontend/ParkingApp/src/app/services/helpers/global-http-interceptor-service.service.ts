import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable, NgZone } from "@angular/core";
import { NavigationExtras, Router } from "@angular/router";
import { ComponentWithErrorMsg } from "@app/components/account/forms/account-form/account-form.component";
import { trimChars } from "@app/utils/string-utils";
import { Observable, throwError } from "rxjs";
import { catchError } from "rxjs/operators";
import { AccountSessionStorageService } from "../account/account-session-storage.service";
import { AuthenticationService } from "../account/auth.service";
import { actions } from "../navigation/app.endpoints";
import { NavigationService } from "../navigation/navigation.service";


/**
 * Global server error handler
 */
@Injectable({
    providedIn: "root"
})
export class GlobalHttpErrorInterceptorService implements HttpInterceptor {

    constructor(
        public router: Router,
        private authService: AuthenticationService,
        private sessionService: AccountSessionStorageService,
        private navigationService: NavigationService,
        public zone: NgZone
    ) {
    }

    /**
     * Intercepts server errors
     * @param req - request
     * @param next - next handler
     */
    intercept(
        req: HttpRequest<any>,
        next: HttpHandler
    ): Observable<HttpEvent<any>> {
        return next.handle(req).pipe(
            catchError((errorResponse) => {
                // handle the error
                this.handleError(errorResponse);

                // ...optionally return a default fallback value so app can continue (pick one)
                // which could be a default value
                // return Observable.of<any>({my: "default value..."});
                // or simply an empty observable
                // return EMPTY;
                return throwError(errorResponse);
            })
        );
    }

    /**
     * Handle HTTP server error
     * @param errorResponse - HTTP server error respnse
     */
    private handleError(errorResponse: any) {
        if (errorResponse instanceof HttpErrorResponse) {
            // A client-side or network error occurred. Handle it accordingly.
            const errorStatus = errorResponse.status;
            const errorMessage = errorResponse.message;
            const errorStatusText = errorResponse.statusText;
            const error = errorResponse.error;
            const errors = errorResponse.error.errors;

            console.error('An error occurred:', errorMessage);
            console.log(`Error status : ${errorStatus} ${errorStatusText}`);

            const navigationExtras: NavigationExtras = {
                state: { errors: errors ? errors : error, from: "globalError" },
                queryParams: { action: actions.login }
            };
            console.log('Error navigationExtras:', navigationExtras);

            switch (errorStatus) {
                case 401: // login, unauthorized
                    if (this.authService.isUserLoggedIn() ||
                        this.sessionService.isJwtTokenExpired()) {
                        this.fullLogout();
                    }

                    this.navigationService.navigateToUnauthorized();
                    setTimeout(() => {
                        this.navigateToLogin(navigationExtras);
                    }, 7000);

                    break;
                case 403: // forbidden
                    this.navigationService.navigateToForbidden();
                    setTimeout(() => {
                        this.navigationService.navigateToMain();
                    }, 7000);
                    break;
                case 500:
                    console.log(`Server error`);
                    break;
                case 0:
                    if (errorStatusText === 'Unknown Error') {
                        this.navigationService.navigateToServerNotRunning();
                    }
                    console.log(`Check if server is running`);
                    break;
            }
            if (error instanceof ErrorEvent) {
                console.error('Error Event');
            }
            if (error instanceof Error) {
                console.error('Error ' + error);
            } else {
                // The backend returned an unsuccessful response code.
                // The response body may contain clues as to what went wrong,
                console.error(
                    `Backend returned code ${errorStatus}, body was: ${error}`
                );
            }
        } else {
            console.error('Other Errors');
        }
    }

    /**
     * Full user logout
     */
    private fullLogout() {
        this.authService.fullLogout();
    }

    /**
     * Navigate to login page
     * @param navigationExtras - addiritional information
     */
    private navigateToLogin(navigationExtras: NavigationExtras) {
        this.zone.run(() =>
            this.navigationService.navigateToLoginWithExtras(navigationExtras)
        );
        console.log(`redirect to login`);
    }
}

/**
 * Parse error from LDAP HTTP server response
 * @param ldapError - LDAP HTTP server response
 */
function parseLdapError(ldapError: string) {
    if (ldapError.toLowerCase().includes("ldap")) {
        return trimChars("[.,:;]", ldapError
            .split(";")[0]
            .split(":")[1]
            .split("-")[1]);
    } else {
        return ldapError;
    }
}

/**
 * Handle HTTP server error response conform error object received
 * with provided next callback step
 */
export function handleHttpErrorResponse(error, component: ComponentWithErrorMsg, nextCallback?) {
    if (error instanceof HttpErrorResponse) {

        let tmpMsg = error.error.message
            ? error.error.message
            : error.error;
        tmpMsg = parseLdapError(tmpMsg);
        component.errorMessage = tmpMsg;

        if (nextCallback) nextCallback();
    }
}

/**
 * Handle HTTP server error response conform error object received
 * with provided next callback step
 */
export function handleErrorResponse(component: ComponentWithErrorMsg, nextCallback?) {
    return (error) => {
        handleHttpErrorResponse(error, component, nextCallback);
    };
}
