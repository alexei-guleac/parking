import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationRequest } from '@app/models/payload/AuthenticationRequest';
import { ResetPasswordRequest } from '@app/models/payload/ResetPasswordRequest';
import { User } from '@app/models/User';
import {
    AccountSessionStorageService,
    AccountStorageService,
    getUsername,
    roleAdmin,
    setUsername
} from '@app/services/account/account-session-storage.service';
import { SocialUserStorageService } from '@app/services/account/social/social-user-storage.service';
import { HttpClientService } from '@app/services/helpers/http-client.service';
import { api } from '@app/services/navigation/app.endpoints';
import { environment } from '@env';
import { AuthService } from 'angularx-social-login-vk';
import { Observable } from 'rxjs';
import 'rxjs/add/observable/empty';


/**
 * Provides methods for server API requests performing
 */
@Injectable({
    providedIn: 'root'
})
export class AuthenticationService {
    public username: string;

    public password: string;

    constructor(
        private http: HttpClientService,
        private router: Router,
        private sessionStorageService: AccountSessionStorageService,
        private storageType: AccountStorageService,
        private OAuthService: AuthService,
        private socialUserService: SocialUserStorageService
    ) {
    }

    /**
     * Send server user login request
     * @param credentials - username and password
     * @param deviceInfo - user device common information
     */
    processAuthentification(
        credentials: AuthenticationRequest,
        deviceInfo: any
    ): Observable<any> {
        const url = environment.restUrl + api.auth;

        return this.http.postJsonRequest<any>(url, {
            credentials,
            deviceInfo
        });
    }

    /**
     * Send server user registration request
     * @param user - being registered user
     * @param deviceInfo - user device common information
     */
    processRegistration(user: User, deviceInfo: any): Observable<any> {
        const url = environment.restUrl + api.registration;

        return this.http.postJsonRequest<any>(url, {
            user,
            deviceInfo
        });
    }

    /**
     * Send server user confirmation request
     * @param confirmationToken - target confirmation token
     */
    processUserConfirmation(confirmationToken: string): Observable<any> {
        const url = environment.restUrl + api.confirmReg;

        return this.http.postJsonRequest<any>(url, {
            confirmationToken
        });
    }

    /**
     * Send server forgot password request
     * @param email - user email
     * @param deviceInfo - user device common information
     */
    processForgotPasswordRequest(
        email: string,
        deviceInfo: any
    ): Observable<any> {
        const url = environment.restUrl + api.forgotPass;

        return this.http.postJsonRequest<any>(url, {
            email,
            deviceInfo
        });
    }

    /**
     * Send server reset password request
     * @param resetRequest - reset password request
     * @param deviceInfo - user device common information
     */
    processResetPasswordRequest(
        resetRequest: ResetPasswordRequest,
        deviceInfo: any
    ): Observable<any> {
        const url = environment.restUrl + api.resetPass;

        return this.http.postJsonRequest<any>(url, {
            resetRequest,
            deviceInfo
        });
    }

    /**
     * Save user login information in corresponding storage depends on 'remember me' checkbox
     * @param username - target user name
     * @param token - received JWT from server
     * @param rememberUser - remember user action
     */
    registerSuccessfulAuth(
        username: string,
        token: string,
        rememberUser: boolean
    ) {

        if (rememberUser) {
            this.sessionStorageService.localStoreToken(username, token);
        } else {
            this.sessionStorageService.sessionStoreToken(username, token);
        }
    }

    /**
     * Logout user by cleaning user information from browser storage
     */
    processLogout() {
        this.sessionStorageService.clearAccountStorage();
        this.sessionStorageService.clearCacheStorage();

        this.username = null;
        this.password = null;
    }

    /**
     * Vhecl if user logged in by get username from current browser storage
     */
    isUserLoggedIn() {
        const user = getUsername(this.storageType.getType());

        return !(user === null || user === '' || user === undefined);
    }

    /**
     * Assert that logged in user have ADMIN role
     */
    isAdminLoggedIn() {
        return this.sessionStorageService
            .getUserRolesFromJwt()
            .includes(roleAdmin);
    }

    /**
     * Get logged in user name
     */
    getLoggedInUserName() {
        const user = getUsername(this.storageType.getType());
        if (user === null) {
            return '';
        }
        return user;
    }

    /**
     * Set logged in user name
     * @param newUsername - new logged in user name
     */
    setLoggedInUserName(newUsername: string) {
        setUsername(newUsername, this.storageType.getType());
    }

    /**
     * Full logout from all profiles and services with cleaning all additional login information
     */
    fullLogout() {
        this.processLogout();
        this.OAuthService.signOut();
        this.socialUserService.cleanGitAuth();
    }
}
