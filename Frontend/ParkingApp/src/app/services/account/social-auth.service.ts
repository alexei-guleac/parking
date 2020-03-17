import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AuthServiceConfig, FacebookLoginProvider, GoogleLoginProvider} from 'angularx-social-login';
import {Observable} from 'rxjs';
import {setAcceptJsonHeaders} from '../data/data.service';
import {environment} from '../../../environments/environment';
import {api} from '../navigation/app.endpoints';


const config = new AuthServiceConfig([
    {
        id: FacebookLoginProvider.PROVIDER_ID,
        provider: new FacebookLoginProvider('509861456396823')   // nata.nemuza
    },
    {
        id: GoogleLoginProvider.PROVIDER_ID,
        provider: new GoogleLoginProvider('878973947322-drnv3qvmpvs3kaie7s699g99t2odi4fq.apps.googleusercontent.com')   // aleks7900
    }
]);

export function provideConfig() {
    return config;
}

export const providerNames = {
    facebook: {name: 'facebook', short: 'fb'},
    google: {name: 'google', short: 'g'},
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
