import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {credentials} from './credentials';
import {User} from '../Model/User';

@Injectable({
    providedIn: 'root'
})
export class AuthenticationService {

    USER_NAME_SESSION_ATTRIBUTE_NAME = 'authenticatedUser';

    public username: string;
    public password: string;

    constructor(private http: HttpClient) {
    }

    authenticationServiceLogin(username: string, password: string) {
        const url = environment.restUrl + '/login';
        // console.log('Auth login');
        // console.log('Login ' + username + '  ' + password);

        return this.http.post<Observable<boolean>>(url, {
            headers: {
                Accept: 'application/json',
                Authorization: this.createBasicAuthToken(username, password)
            },

            username,
            password
        });
    }

    authenticationServiceRegistration(user: User) {
        const url = environment.restUrl + `/registration`;
        console.log('Registration');
        console.log('User goes to server ' + user.lastname);

        return this.http.post<Observable<boolean>>(url, {
            headers: {
                Accept: 'application/json',
            },

            username: user.username,
            email: user.email,
            fullname: user.fullname,
            lastname: user.lastname,
            password: user.password
        });
    }

    createBasicAuthToken(username: string, password: string) {
        return 'Basic ' + window.btoa(username + ':' + password);
    }

    registerSuccessfulLogin(username) {
        sessionStorage.setItem(this.USER_NAME_SESSION_ATTRIBUTE_NAME, username);
        sessionStorage.setItem(
            'token',
            btoa(this.username + ':' + this.password)
        );
    }

    authenticationServiceLogout() {
        sessionStorage.removeItem(this.USER_NAME_SESSION_ATTRIBUTE_NAME);
        sessionStorage.removeItem('token');

        this.username = null;
        this.password = null;
    }

    isUserLoggedIn() {
        const user = sessionStorage.getItem(this.USER_NAME_SESSION_ATTRIBUTE_NAME);
        return user !== null;
    }

    isAdminLoggedIn() {
        return this.isUserLoggedIn() && this.getLoggedInUserName() === credentials.admin;
    }

    getLoggedInUserName() {
        const user = sessionStorage.getItem(this.USER_NAME_SESSION_ATTRIBUTE_NAME);
        if (user === null) {
            return '';
        }
        return user;
    }
}
