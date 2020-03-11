import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {credentials} from './credentials';
import {User} from '../Model/User';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/of';
import 'rxjs/add/observable/empty';
import 'rxjs/add/operator/retry';

@Injectable({
    providedIn: 'root'
})
export class AuthenticationService {

    USER_NAME_SESSION_ATTRIBUTE_NAME = 'authenticatedUser';
    TOKEN_NAME = 'token';

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

    authenticationServiceAuth<T>(username: string, password: string) {
        const url = environment.restUrl + '/auth';
        // console.log('Auth login');
        // console.log('Login ' + username + '  ' + password);

        return this.http.post<Observable<T>>(url, {
            username,
            password
        }, {
            headers: {
                Accept: 'application/json'
            }
        }).retry(2)         // optionally add the retry
            /*.catch((err: HttpErrorResponse) => {

                if (err.error instanceof Error) {
                    // A client-side or network error occurred. Handle it accordingly.
                    console.error('An error occurred:', err.error.message);
                } else {
                    // The backend returned an unsuccessful response code.
                    // The response body may contain clues as to what went wrong,
                    console.error(`Backend returned code ${err.status}, body was: ${err.error}`);
                }

                // ...optionally return a default fallback value so app can continue (pick one)
                // which could be a default value
                // return Observable.of<any>({my: "default value..."});
                // or simply an empty observable
                return Observable.empty<>();
            })*/;
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
            this.TOKEN_NAME,
            btoa(this.username + ':' + this.password)
        );
    }

    registerSuccessfulAuth(username, token) {
        /*console.log(username);
        console.log(token);*/

        sessionStorage.setItem(this.USER_NAME_SESSION_ATTRIBUTE_NAME, username);
        sessionStorage.setItem(
            this.TOKEN_NAME,
            token
        );
    }

    authenticationServiceLogout() {
        sessionStorage.removeItem(this.USER_NAME_SESSION_ATTRIBUTE_NAME);
        sessionStorage.removeItem(this.TOKEN_NAME);

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
