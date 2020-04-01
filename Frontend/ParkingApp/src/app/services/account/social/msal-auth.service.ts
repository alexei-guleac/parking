import {Injectable} from '@angular/core';
import {BroadcastService, MsalService} from '@azure/msal-angular';
import {Subscription} from 'rxjs';
import {HttpClientService} from '../../helpers/http-client.service';


const GRAPH_ENDPOINT = 'https://graph.microsoft.com/v1.0/me';

@Injectable({
    providedIn: 'root'
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

    getProfile() {
        return this.http.getJsonRequest<any>(GRAPH_ENDPOINT);
    }

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

    msalLogout() {
        this.msalService.logout();
    }

    msalCheckoutAccount() {
        this.loggedIn = !!this.msalService.getAccount();
    }

    private msalInit() {
        this.isIframe = window !== window.parent && !window.opener;

        this.msalCheckoutAccount();

        this.socialAuthSubscription = this.broadcastService.subscribe(
            'msal:loginSuccess',
            accessToken => {
                this.msalCheckoutAccount();
                // console.log('ACCOUNT  ' + JSON.stringify(accessToken));
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
