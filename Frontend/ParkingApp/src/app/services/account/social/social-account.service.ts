import { Injectable } from '@angular/core';
import {
    SocialConnectRequest,
    SocialDisconnectRequest,
    SocialSignInRequest,
    SocialSignUpRequest
} from '@app/models/payload/social-sign-in-request.payload';
import { HttpClientService } from '@app/services/helpers/http-client.service';
import { api } from '@app/services/navigation/app.endpoints';
import { environment } from '@env';
import { Observable } from 'rxjs';


/**
 * Service for providing social related requests
 */
@Injectable({
    providedIn: 'root'
})
export class SocialAccountService {

    constructor(
        private http: HttpClientService
    ) {
    }

    /**
     * Perform social service login with specified provider id
     * @param socialRequest - contains social id and short provider name
     * @param deviceInfo - user device information for region language targeting purposes
     */
    socialServiceLogin(
        socialRequest: SocialSignInRequest,
        deviceInfo: any
    ): Observable<any> {
        const url = environment.restUrl + api.authSocial;

        return this.http.postJsonRequest<any>(url, {
            id: socialRequest.id,
            socialProvider: socialRequest.socialProvider,
            deviceInfo
        });
    }

    /**
     * Perform social service registration with specified provider id
     * @param socialRequest - contains social id and short provider name
     * @param deviceInfo - user device information for region language targeting purposes
     */
    socialServiceSignUp(
        socialRequest: SocialSignUpRequest,
        deviceInfo: any
    ): Observable<any> {
        const url = environment.restUrl + api.regSocial;

        return this.http.postJsonRequest<any>(url, {
            id: socialRequest.id,
            user: socialRequest.user,
            socialProvider: socialRequest.socialProvider,
            deviceInfo
        });
    }

    /**
     * Perform social service account connection with specified provider id
     * @param socialRequest - contains username, social id and short provider name
     * @param deviceInfo - user device information for region language targeting purposes
     */
    socialServiceConnect(
        socialRequest: SocialConnectRequest,
        deviceInfo: any
    ): Observable<any> {
        const url = environment.restUrl + api.connectSocial;

        return this.http.postJsonRequest<any>(url, {
            id: socialRequest.id,
            user: socialRequest.user,
            username: socialRequest.username,
            socialProvider: socialRequest.socialProvider,
            deviceInfo
        });
    }

    /**
     * Perform social service account disconnection with specified provider id
     * @param socialRequest - contains username and short provider name
     */
    socialServiceDisconnect(socialRequest: SocialDisconnectRequest)
        : Observable<any> {
        const url = environment.restUrl + api.disconnectSocial;

        return this.http.postJsonRequest<any>(url, {
            username: socialRequest.username,
            socialProvider: socialRequest.socialProvider
        });
    }
}
