import {HttpErrorResponse} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {environment} from '../../../../environments/environment';
import {HttpClientService} from '../../helpers/http-client.service';
import {api, app, appRoutes} from '../../navigation/app.endpoints';
import {SocialUserService} from './social-user.service';


@Injectable({
    providedIn: 'root'
})
export class GithubOauthService {
    private AUTHORIZE_URL = 'https://github.com/login/oauth/authorize';

    private REDIRECT_URI = app.frontURL + appRoutes.accountLogin;

    private ENCODED_REDIRECT_URI = encodeURIComponent(this.REDIRECT_URI);

    private CLIENT_ID = '9454ba3084a75c484cbe'; // al.guleac

    private CLIENT_SECRET = '5783f0a98aadd4260ed061fa9b6a019f91e70f9f';

    private TOKEN_URL = 'https://github.com/login/oauth/access_token';

    private API_URL = 'https://api.github.com/user';

    constructor(
        private http: HttpClientService,
        private socialUserService: SocialUserService
    ) {
    }

    handleGithubOauthRequest(code: string): any {
        this.processGithubOauthRequest(code).subscribe(
            (response: any) => {
                console.log('processGithubOauthRequest' + response);
                if (response.access_token) {
                    console.log(response.access_token);
                }
                this.getGitUserData(response.access_token);
            },
            error => {
                console.log(error);

                if (error instanceof HttpErrorResponse) {
                    console.log('this.HttpErrorResponse');
                    console.log(
                        error
                    ); /*
                if (isNonEmptyString(error.error.message)) {
                    this.errorMessage = error.error.message;
                } else {
                    this.errorMessage = error.error;
                }*/
                }
            }
        );
    }

    getGitUserData(accessToken: string): any {
        this.processGithubUserDataRequest(accessToken).subscribe(
            (response: any) => {
                console.log(response);
                if (response.id) {
                    console.log(response.id);
                    this.socialUserService.gitUser.next(response);
                }
            },
            error => {
                console.log(error);

                if (error instanceof HttpErrorResponse) {
                    console.log('this.HttpErrorResponse');
                    console.log(
                        error
                    ); /*
                if (isNonEmptyString(error.error.message)) {
                    this.errorMessage = error.error.message;
                } else {
                    this.errorMessage = error.error;
                }*/
                }
            }
        );
    }

    // https://andreybleme.com/2018-02-24/oauth-github-web-flow-cors-problem/

    processGithubOauthRequest(code: string): Observable<any> {
        const url = environment.restUrl + api.gitOAuth;
        console.log('code ' + code);

        return this.http.postJsonRequest<any>(url, {
            code
        });
    }

    processGithubUserDataRequest(accessToken: string): Observable<any> {
        const url = `${this.API_URL}?access_token=${accessToken}`;
        console.log('accessToken ' + accessToken);

        return this.http.getJsonRequest<any>(url);
    }

    // function that checks if there is any data in the session
    loginWithGithub = () => {
        // we have to redirect to the authorize url provided by github
        window.location.href = `${this.AUTHORIZE_URL}?scope=user%3Aemail&client_id=${this.CLIENT_ID}
         &redirect_uri=${this.ENCODED_REDIRECT_URI}`;

        /*window.open(`${this.AUTHORIZE_URL}?scope=user%3Aemail&client_id=${this.CLIENT_ID}
        &redirect_uri=${this.ENCODED_REDIRECT_URI}`, '_blank');*/
    };
}
