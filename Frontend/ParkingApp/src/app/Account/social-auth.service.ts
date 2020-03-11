import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class SocialAuthService {

    constructor(private http: HttpClient) {
    }

    socialServiceLogin(id: string, social: string) {
        const url = environment.restUrl + '/login/' + social;
        // console.log('Social login');
        // console.log('Login ' + id);

        return this.http.post<Observable<boolean>>(url, {
            /*headers: {
                Accept: 'application/json',
                Authorization: this.createBasicAuthToken(username, password)
            },*/
            id
        });
    }
}
