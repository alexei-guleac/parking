import { Injectable } from "@angular/core";
import { HttpClientService } from "@app/services/helpers/http-client.service";
import { BroadcastService, MsalService } from "@azure/msal-angular";
import { Subscription } from "rxjs";


const GRAPH_ENDPOINT = 'https://graph.microsoft.com/v1.0/me';

/**
 * Microsoft Authentication Library (MSAL) service
 */
@Injectable({
    providedIn: 'root',
})
export class MsalAuthService {

    isIframe = false;

    profile;

    private loggedIn: boolean;

    private socialAuthSubscription: Subscription;

    constructor(
        private msalService: MsalService,
        private broadcastService: BroadcastService,
        private http: HttpClientService
    ) {
    }

    /**
     * Get user profile
     */
    getProfile() {
        return this.http.getJsonRequest<any>(GRAPH_ENDPOINT);
    }

    /**
     * Initiates MSAL login
     */
    msalLogin() {
        this.msalInit();

        const isIE =
            window.navigator.userAgent.indexOf('MSIE ') > -1 ||
            window.navigator.userAgent.indexOf('Trident/') > -1;

        if (isIE) {
            this.msalService.loginRedirect();
        } else {
            this.msalService.loginPopup();
        }
    }

    /**
     * Initiates MSAL logout
     */
    msalLogout() {
        this.msalService.logout();
    }

    /**
     * Initiates MSAL account check
     */
    msalCheckoutAccount() {
        this.loggedIn = !!this.msalService.getAccount();
    }

    /**
     * Initiates MSAL
     */
    private msalInit() {
        this.isIframe = window !== window.parent && !window.opener;

        this.msalCheckoutAccount();

        this.socialAuthSubscription = this.broadcastService.subscribe(
            'msal:loginSuccess',
            (accessToken) => {
                this.msalCheckoutAccount();
            }
        );

        this.msalService.handleRedirectCallback((authError, response) => {
            if (authError) {
                console.error('Redirect Error: ', authError.errorMessage);
                return;
            }
            console.log('Redirect Success: ', response.accessToken);
        });
    }
}
