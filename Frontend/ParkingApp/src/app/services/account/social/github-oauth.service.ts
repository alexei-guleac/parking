import { HttpErrorResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { SocialUserStorageService } from "@app/services/account/social/social-user-storage.service";
import { HttpClientService } from "@app/services/helpers/http-client.service";
import { api, app, appRoutes } from "@app/services/navigation/app.endpoints";
import { environment } from "@env";
import { Observable } from "rxjs";


/**
 * Github OAuth 2.0 flow service
 */
@Injectable({
    providedIn: "root"
})
export class GithubOauthService {

    private AUTHORIZE_URL = "https://github.com/login/oauth/authorize";

    /*
    * Target redirect URL (localhost allowed).
    * After authorizing the application users will be redirected to the provided URL and port.
    * The redirect_uri parameter is optional. If left out,
    * GitHub will redirect users to the callback URL configured in the OAuth Application settings.
    * If provided, the redirect URL's host and port must exactly match the callback URL.
    * The redirect URL's path must reference a subdirectory of the callback URL.
    * */
    private REDIRECT_URI = app.frontURL + appRoutes.accountLogin;

    private ENCODED_REDIRECT_URI = encodeURIComponent(this.REDIRECT_URI);

    /*
    * Required. The client ID received from GitHub when API client app is registered.
    * */
    private CLIENT_ID = "9454ba3084a75c484cbe";

    private API_URL = "https://api.github.com/user";

    constructor(
        private http: HttpClientService,
        private socialUserStorageService: SocialUserStorageService
    ) {
    }

    /**
     * Handles Github OAuth access token request via server help
     * @param code - given temporary code from the previous step.
     * The temporary code will expire after 10 minutes.
     * This code is exchanged for an access token
     */
    handleGithubOauthRequest(code: string): any {
        this.processGithubOauthRequest(code).subscribe((response: any) => {
            if (response.access_token) {
                this.getGitUserData(response.access_token);
            }
        }, this.handleError);
    }

    /**
     * Handles user data with given access token.
     * The access token allows you to make requests to the API on a behalf of a user.
     * @param accessToken - Github API access token.
     */
    getGitUserData(accessToken: string): any {
        this.processGithubUserDataRequest(accessToken).subscribe(
            (response: any) => {
                console.log(response);
                if (response.id) {
                    console.log(response.id);
                    this.socialUserStorageService.gitUser.next(response);
                }
            },
            this.handleError
        );
    }

    /**
     * Perform Github OAuth 2.0 flow for getting Github API access token
     * @param code - - given temporary code from the previous step.
     * The temporary code will expire after 10 minutes.
     * This code is exchanged for an access token
     * */
    processGithubOauthRequest(code: string): Observable<any> {
        const url = environment.restUrl + api.gitOAuth;
        console.log("code " + code);

        return this.http.postJsonRequest<any>(url, {
            code
        });
    }

    /**
     * Get user data with given access token.
     * The access token allows you to make requests to the API on a behalf of a user.
     * @param accessToken - Github API access token.
     */
    processGithubUserDataRequest(accessToken: string): Observable<any> {
        const url = `${this.API_URL}?access_token=${accessToken}`;
        console.log("accessToken " + accessToken);

        return this.http.getJsonRequest<any>(url);
    }

    /**
     * Function that checks if there is any data in the session
     */
    loginWithGithub = () => {
        // we have to redirect to the authorize url provided by github
        window.location.href = `${this.AUTHORIZE_URL}?scope=user%3Aemail&client_id=${this.CLIENT_ID}
         &redirect_uri=${this.ENCODED_REDIRECT_URI}`;
    };

    /**
     * handle server flow error
     */
    private handleError() {
        return (error) => {
            if (error instanceof HttpErrorResponse) {
                console.log(
                    error
                );
            }
        };
    }
}
