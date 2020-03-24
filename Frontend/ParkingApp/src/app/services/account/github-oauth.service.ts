import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable, Subject} from 'rxjs';
import {environment} from '../../../environments/environment';
import {setAcceptJsonHeaders} from '../../config/http-config';
import {api, app, appRoutes} from '../navigation/app.endpoints';


@Injectable({
    providedIn: 'root'
})
export class GithubOauthService {

    // ===========================================
    // DEFINING CONSTANTS NECESSARY FOR OAUTH
    public socialGitUser: any;

    gitUserChange: Subject<boolean> = new Subject<boolean>();

    // ===========================================
    private AUTHORIZE_URL = 'https://github.com/login/oauth/authorize';

    private REDIRECT_URI = app.frontURL + appRoutes.accountLogin;

    private ENCODED_REDIRECT_URI = encodeURIComponent(this.REDIRECT_URI);

    // ===========================================
    // CREATE CONSTANTS NECESSARY FOR OAUTH IN SERVER

    private CLIENT_ID = '9454ba3084a75c484cbe';         // al.guleac

    private CLIENT_SECRET = '5783f0a98aadd4260ed061fa9b6a019f91e70f9f';


    // ===========================================
    // FUNCTION THAT IS CALLED WHEN USER CLICKS
    // SIGN IN WITH GITHUB

    // ===========================================
    private TOKEN_URL = 'https://github.com/login/oauth/access_token';

    private API_URL = 'https://api.github.com/user';

    constructor(private http: HttpClient) {
    }

    handleGithubOauthRequest(code: string): any {
        this.processGithubOauthRequest(code).subscribe(
            (response: any) => {
                console.log(response);
                if (response.access_token) {
                    console.log(response.access_token);
                }
                this.getGitUserData(response.access_token);
            }, error => {
                console.log(error);

                if (error instanceof HttpErrorResponse) {
                    console.log('this.HttpErrorResponse');
                    console.log(error); /*
                if (isNonEmptyString(error.error.message)) {
                    this.errorMessage = error.error.message;
                } else {
                    this.errorMessage = error.error;
                }*/
                }
            });
    }

    getGitUserData(accessToken: string): any {
        this.processGithubUserDataRequest(accessToken).subscribe(
            (response: any) => {
                console.log(response);
                if (response.id) {
                    console.log(response.id);
                    this.gitUserChange.next(response);
                }
            }, error => {
                console.log(error);

                if (error instanceof HttpErrorResponse) {
                    console.log('this.HttpErrorResponse');
                    console.log(error); /*
                if (isNonEmptyString(error.error.message)) {
                    this.errorMessage = error.error.message;
                } else {
                    this.errorMessage = error.error;
                }*/
                }
            });
    }

    // https://andreybleme.com/2018-02-24/oauth-github-web-flow-cors-problem/

    processGithubOauthRequest(code: string): Observable<any> {
        const url = environment.restUrl + api.gitOAuth;
        console.log('code ' + code);

        return this.http.post<any>(url, {
            code

        }, {
            headers: setAcceptJsonHeaders()
        });
    }

    processGithubUserDataRequest(accessToken: string): Observable<any> {
        const url = `${this.API_URL}?access_token=${accessToken}`;
        console.log('accessToken ' + accessToken);

        return this.http.get<any>(url,
            {headers: setAcceptJsonHeaders()}
        );
    }

    // ===========================================
    // FUNCTION THAT CHECKS IF THERE IS ANY DATA IN THE SESSION

    // ===========================================
    loginWithGithub = () => {
        // ===========================================
        // WE HAVE TO REDIRECT TO THE AUTHORIZE URL PROVIDED BY GITHUB
        // ===========================================
        window.location.href = `${this.AUTHORIZE_URL}?scope=user%3Aemail&client_id=${this.CLIENT_ID}
        &redirect_uri=${this.ENCODED_REDIRECT_URI}`;
        // ===========================================
        // HERE WE PASS ENCODED URI AS A PARAMETER IN A URI
        // CANNOT HAVE '/', IN THE WAY URI'S HAVE
        // ===========================================
    }
}
