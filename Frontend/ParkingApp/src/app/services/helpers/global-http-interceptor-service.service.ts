import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable, NgZone} from '@angular/core';
import {NavigationExtras, Router} from '@angular/router';
import {AuthService} from 'angularx-social-login-vk';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {AuthenticationService} from '../account/auth.service';
import {SessionStorageService} from '../account/session-storage.service';
import {actions} from '../navigation/app.endpoints';
import {NavigationService} from '../navigation/navigation.service';


@Injectable({
    providedIn: 'root'
})
export class GlobalHttpErrorInterceptorService implements HttpInterceptor {

    constructor(public router: Router,
                private authService: AuthenticationService,
                private sessionService: SessionStorageService,
                private OAuth: AuthService,
                private navigation: NavigationService,
                public zone: NgZone) {
    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        return next.handle(req).pipe(
            catchError((errorResponse) => {
                console.log('error is intercept');
                console.error(errorResponse);
                // handle the error
                this.handleError(errorResponse);
                console.log('error intercept finish');

                // ...optionally return a default fallback value so app can continue (pick one)
                // which could be a default value
                // return Observable.of<any>({my: "default value..."});
                // or simply an empty observable
                // return EMPTY;

                return throwError(errorResponse);
            })
        );
    }

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
                state: {errors: errors ? errors : error, from: 'globalError'},
                queryParams: {action: actions.login}
            };

            console.log('Error navigationExtras:', navigationExtras);

            switch (errorStatus) {
                case 401:       // login, unauthorized

                    if (this.authService.isUserLoggedIn() || this.sessionService.isJwtTokenExpired()) {
                        this.authService.processLogout();
                        this.OAuth.signOut();
                    }

                    this.navigateToLogin(navigationExtras);
                    break;
                case 403:       // forbidden
                    this.navigateToLogin(navigationExtras);
                    break;
                case 500:
                    console.log(`Server error`);
                    break;
                case 0:
                    if (errorStatusText === 'Unknown Error') {
                        this.navigation.navigateToServerNotRunning();
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
                console.error(`Backend returned code ${errorStatus}, body was: ${error}`);
            }
        } else {
            console.error('Other Errors');
        }
    }

    private navigateToLogin(navigationExtras: NavigationExtras) {
        this.zone.run(() => this.navigation.navigateToLoginWithExtras(navigationExtras));
        // this.router.navigateByUrl('/login');
        console.log(`redirect to login`);
        // handled = true;
    }
}

