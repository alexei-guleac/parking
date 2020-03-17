import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {Observable} from 'rxjs';
import 'rxjs/add/observable/empty';
import {AccountStorageTypeService, getUsername, roleAdmin, SessionStorageService} from './session-storage.service';
import {setAcceptJsonHeaders} from '../data/data.service';
import {User} from '../../models/User';
import {environment} from '../../../environments/environment';
import {api} from '../navigation/app.endpoints';


@Injectable({
    providedIn: 'root'
})
export class AuthenticationService {

    public username: string;
    public password: string;

    constructor(private http: HttpClient,
                private router: Router,
                private sessionStorageService: SessionStorageService,
                private storageType: AccountStorageTypeService) {
    }

    processAuthentification(username: string, password: string): Observable<any> {
        const url = environment.restUrl + api.auth;
        // console.log('Auth login');
        // console.log('Login ' + username + '  ' + password);

        return this.http.post<any>(url, {
            username,
            password

        }, {
            headers: setAcceptJsonHeaders()
        });
    }

    processRegistration(user: User): Observable<any> {
        const url = environment.restUrl + api.registration;
        console.log('Registration');
        console.log('User goes to server ' + user.lastname);

        return this.http.post<any>(url, {
            username: user.username,
            email: user.email,
            fullname: user.fullname,
            lastname: user.lastname,
            password: user.password

        }, {
            headers: setAcceptJsonHeaders()
        });
    }

    processUserConfirmation(confirmationToken: string): Observable<any> {
        const url = environment.restUrl + api.confirmReg;
        console.log('confirm ' + confirmationToken);

        return this.http.post<any>(url, {
            confirmationToken

        }, {
            headers: setAcceptJsonHeaders()
        });
    }

    processForgotPasswordRequest(email: string): Observable<any> {
        const url = environment.restUrl + api.forgotPass;
        console.log('forgot pass ' + email);

        return this.http.post<any>(url, {
            email

        }, {
            headers: setAcceptJsonHeaders()
        });
    }

    processResetPasswordRequest(username: string, password: string): Observable<any> {
        const url = environment.restUrl + api.resetPass;
        console.log('reset pass ' + password);

        return this.http.post<any>(url, {
            username,
            password

        }, {
            headers: setAcceptJsonHeaders()
        });
    }

    createBasicAuthToken(username: string, password: string) {
        return 'Basic ' + window.btoa(username + ':' + password);
    }

    registerSuccessfulLogin(username: string) {
        this.sessionStorageService.sessionStoreCredentials(username, this.password);
    }

    registerSuccessfulAuth(username: string, token: string, rememberUser: boolean) {
        /*console.log(username);
        console.log(token);*/

        if (rememberUser) {
            this.sessionStorageService.localStoreToken(username, token);
        } else {
            this.sessionStorageService.sessionStoreToken(username, token);
        }
    }

    processLogout() {
        this.sessionStorageService.clearAccountStorage();

        this.username = null;
        this.password = null;
    }

    isUserLoggedIn() {
        /*console.log('isUserLoggedIn null ' + getUsername(this.storageType.getType()) !== null);
        console.log('isUserLoggedIn undefined ' + getUsername(this.storageType.getType()) !== undefined);
        console.log('storageType ' + this.storageType.getType());
        console.log('isUserLoggedIn ' + getUsername(this.storageType.getType()));*/
        const user = getUsername(this.storageType.getType());

        return !(user === null || user === '' || user === undefined);
    }

    isAdminLoggedIn() {
        // return this.isUserLoggedIn() && this.getLoggedInUserName() === credentials.admin;
        return this.sessionStorageService.getUserRolesFromJwt().includes(roleAdmin);
    }

    getLoggedInUserName() {
        const user = getUsername(this.storageType.getType());
        if (user === null) {
            return '';
        }
        return user;
    }
}
