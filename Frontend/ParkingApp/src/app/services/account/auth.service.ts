import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {AuthenticationRequest} from '@app/models/payload/AuthenticationRequest';
import {ResetPasswordRequest} from '@app/models/payload/ResetPasswordRequest';
import {User} from '@app/models/User';
import {
    AccountStorageService,
    getUsername,
    roleAdmin,
    SessionStorageService,
    setUsername,
} from '@app/services/account/session-storage.service';
import {HttpClientService} from '@app/services/helpers/http-client.service';
import {api} from '@app/services/navigation/app.endpoints';
import {Observable} from 'rxjs';
import 'rxjs/add/observable/empty';
import {environment} from '../../../environments/environment';


@Injectable({
    providedIn: 'root',
})
export class AuthenticationService {
    public username: string;

    public password: string;

    constructor(
        private http: HttpClientService,
        private router: Router,
        private sessionStorageService: SessionStorageService,
        private storageType: AccountStorageService
    ) {
    }

    processAuthentification(
        credentials: AuthenticationRequest,
        deviceInfo: any
    ): Observable<any> {
        const url = environment.restUrl + api.auth;
        // console.log('Auth login');
        // console.log('Login ' + username + '  ' + password);

        return this.http.postJsonRequest<any>(url, {
            credentials,
            deviceInfo,
        });
    }

    processRegistration(user: User, deviceInfo: any): Observable<any> {
        const url = environment.restUrl + api.registration;
        // console.log('Registration');
        // console.log('User goes to server ' + user.lastname);

        return this.http.postJsonRequest<any>(url, {
            user,
            deviceInfo,
        });
    }

    processUserConfirmation(confirmationToken: string): Observable<any> {
        const url = environment.restUrl + api.confirmReg;
        // console.log('confirm ' + confirmationToken);

        return this.http.postJsonRequest<any>(url, {
            confirmationToken,
        });
    }

    processForgotPasswordRequest(
        email: string,
        deviceInfo: any
    ): Observable<any> {
        const url = environment.restUrl + api.forgotPass;
        // console.log('forgot pass ' + email);

        return this.http.postJsonRequest<any>(url, {
            email,
            deviceInfo,
        });
    }

    processResetPasswordRequest(
        resetRequest: ResetPasswordRequest,
        deviceInfo: any
    ): Observable<any> {
        const url = environment.restUrl + api.resetPass;
        // console.log('reset pass ' + resetRequest.password);

        return this.http.postJsonRequest<any>(url, {
            resetRequest,
            deviceInfo,
        });
    }

    registerSuccessfulLogin(username: string) {
        this.sessionStorageService.sessionStoreCredentials(
            username,
            this.password
        );
    }

    registerSuccessfulAuth(
        username: string,
        token: string,
        rememberUser: boolean
    ) {
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
        this.sessionStorageService.clearCacheStorage();

        this.username = null;
        this.password = null;
    }

    isUserLoggedIn() {
        const user = getUsername(this.storageType.getType());

        return !(user === null || user === '' || user === undefined);
    }

    isAdminLoggedIn() {
        return this.sessionStorageService
            .getUserRolesFromJwt()
            .includes(roleAdmin);
    }

    getLoggedInUserName() {
        const user = getUsername(this.storageType.getType());
        if (user === null) {
            return '';
        }
        return user;
    }

    setLoggedInUserName(newUsername: string) {
        setUsername(newUsername, this.storageType.getType());
    }
}
