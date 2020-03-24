import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {AuthServiceConfig, FacebookLoginProvider, GoogleLoginProvider, LoginOpt} from 'angularx-social-login-vk';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';
import {setAcceptJsonHeaders} from '../../config/http-config';
import {api} from '../navigation/app.endpoints';


const fbLoginOptions: LoginOpt = {
    scope: 'pages_messaging,pages_messaging_subscriptions,email,pages_show_list,manage_pages',
    return_scopes: true,
    enable_profile_selector: true
}; // https://developers.facebook.com/docs/reference/javascript/FB.login/v2.11

const googleLoginOptions: LoginOpt = {
    scope: 'profile email'
}; // https://developers.google.com/api-client-library/javascript/reference/referencedocs#gapiauth2clientconfig


export function provideConfig() {
    return new AuthServiceConfig([
        {
            id: FacebookLoginProvider.PROVIDER_ID,
            provider: new FacebookLoginProvider('509861456396823')   // nata.nemuza
        },
        {
            id: GoogleLoginProvider.PROVIDER_ID,
            provider: new GoogleLoginProvider('878973947322-drnv3qvmpvs3kaie7s699g99t2odi4fq.apps.googleusercontent.com')   // aleks7900
        }
    ]);
}

export const providerNames = {
    facebook: {name: 'facebook', short: 'fb'},
    google: {name: 'google', short: 'g'},
    github: {name: 'github', short: 'git'},
    microsoft: {name: 'microsoft', short: 'ms'},
};


@Injectable({
    providedIn: 'root'
})
export class SocialAuthService {

    constructor(private http: HttpClient) {
    }

    socialServiceLogin(id: string, socialProvider: string): Observable<any> {
        const url = environment.restUrl + api.social;
        // console.log('Social login');
        // console.log('Login ' + id);

        return this.http.post<any>(url, {
            id,
            socialProvider

        }, {
            headers: setAcceptJsonHeaders()
        });
    }
}
