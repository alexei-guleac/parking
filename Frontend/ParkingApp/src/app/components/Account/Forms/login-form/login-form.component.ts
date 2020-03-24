import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {BroadcastService, MsalService} from '@azure/msal-angular';
import {AuthService, FacebookLoginProvider, GoogleLoginProvider, SocialUser} from 'angularx-social-login-vk';
import {AuthenticationService} from '../../../../services/account/auth.service';
import {GithubOauthService} from '../../../../services/account/github-oauth.service';
import {providerNames, SocialAuthService} from '../../../../services/account/social-auth.service';
import {LoggerService} from '../../../../services/helpers/logger.service';
import {isNonEmptyString} from '../../../../utils/string-utils';
import {credentials} from '../../../../validation/credentials';
import {RegularExpressions} from '../../../../validation/reg-exp-patterns';
import {regexpTestValidator} from '../../../../validation/regexp-name-validator';


const GRAPH_ENDPOINT = 'https://graph.microsoft.com/v1.0/me';

@Component({
    selector: 'app-login-form',
    templateUrl: './login-form.component.html',
    styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent implements OnInit {

    @Input() isLogFailed: boolean;

    @Input() errMessage: string;

    @Input() isConfirmSuccess: boolean;

    @Input() confMessage: string;

    @Output()
    userLoginEvent: EventEmitter<any> = new EventEmitter<any>();

    @Output()
    userForgotEvent: EventEmitter<any> = new EventEmitter<any>();

    private user: SocialUser;

    private username: string;

    private password: string;

    private errorMessage = 'Invalid Credentials';

    private successMessage: string;

    private isLoginSuccess = false;

    private isLoginFailed = false;

    private fieldTextTypePass: boolean;

    private loggedIn: boolean;

    private submitted = false;

    private rememberUser = false;

    private loginForm: FormGroup;

    isIframe = false;

    profile;

    private code: string;

    private socialProviders = providerNames;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private authenticationService: AuthenticationService,
        private OAuth: AuthService,
        private githubAuthService: GithubOauthService,
        private socialService: SocialAuthService,
        private broadcastService: BroadcastService,
        private msalAuthService: MsalService,
        private log: LoggerService,
        private http: HttpClient) {
    }

    getProfile() {
        return this.http.get(GRAPH_ENDPOINT);
    }

    get name() {
        return this.loginForm.get('username');
    }

    get pass() {
        return this.loginForm.get('password');
    }

    ngOnInit(): void {
        this.loginForm = new FormGroup({
            username: new FormControl(this.username, [
                Validators.required,
                Validators.minLength(5),
                Validators.maxLength(15),

                // <-- Here's how you pass in the custom validator.
                regexpTestValidator(RegularExpressions.usernamePattern)
            ]),

            password: new FormControl(this.password, [
                Validators.required,
                Validators.minLength(6),
                Validators.maxLength(10),

                Validators.pattern(RegularExpressions.passwordPatternStr)
            ]),
            rememberMe: new FormControl()
        });

        this.msalInit();
        this.processGithubOauthCallback();
        this.initSocialLogin();
        this.initMsgFromParent();
        this.log.logMethod('Hellloooo!!!');


        this.getProfile().subscribe(
            profile => {
                this.profile = profile;
                console.log(profile);
            });
    }

    msalCheckoutAccount() {
        this.loggedIn = !!this.msalAuthService.getAccount();
    }

    onSubmit() {
        this.submitted = true;
        if (this.loginForm.hasError('invalid')) {
            this.submitted = false;
        }

        if (isNonEmptyString(this.username) && isNonEmptyString(this.password)) {

            console.log(this.username + '  ' + this.password);
            this.handleLogin();

            // for local login test
            // this.localLoginTest();
        }
    }

    msalLogin() {
        const isIE = window.navigator.userAgent.indexOf('MSIE ') > -1 || window.navigator.userAgent.indexOf('Trident/') > -1;

        if (isIE) {
            this.msalAuthService.loginRedirect();
        } else {
            this.msalAuthService.loginPopup();
        }
    }

    msalLogout() {
        this.msalAuthService.logout();
    }

    // Switching method
    private togglePassTextType() {
        this.fieldTextTypePass = !this.fieldTextTypePass;
    }

    private handleLogin() {
        this.authenticationService.processAuthentification(this.username, this.password).subscribe(
            (response: any) => {

                if (response.token) {
                    this.isLoginFailed = false;
                    this.isLoginSuccess = true;
                    this.successMessage = 'Login Successful.';
                    console.log(this.successMessage);
                    // console.log(response.token);

                    this.authenticationService.registerSuccessfulAuth(this.username, response.token, this.rememberUser);

                    /*console.log(sessionStorage.getItem('authenticatedUser'));
                    console.log(sessionStorage.getItem('token'));*/

                    this.router.navigate(['']);
                }
            }, error => {
                this.isLoginSuccess = false;
                this.isLoginFailed = true;
                /*console.log('log ');
                console.log(error);
                console.log('Authentication failed.');*/
                alert('Authentication failed.');

                if (error instanceof HttpErrorResponse) {
                    this.errorMessage = error.error.message ? error.error.message : error.error;
                }
            });
    }

    private socialSignIn(socialProvider: string): void {

        let socialPlatformProvider: string;
        let social: string;

        if (socialProvider === this.socialProviders.facebook.name) {
            socialPlatformProvider = FacebookLoginProvider.PROVIDER_ID;
            social = this.socialProviders.facebook.short;
        }
        if (socialProvider === this.socialProviders.google.name) {
            socialPlatformProvider = GoogleLoginProvider.PROVIDER_ID;
            social = this.socialProviders.google.short;
        }

        this.OAuth.signIn(socialPlatformProvider)
            .then((socialUser) => {
                console.log('then ', socialProvider, socialUser);

                if (socialUser !== null) {
                    console.log(this.user);
                    console.log(socialUser.provider);
                    console.log(this.OAuth.readyState);

                    this.sendSocialLoginRequest(socialUser.id, social);
                }
            });
    }

    /*Savesresponse(socialusers: SocialUser) {

        this.SocialloginService.Savesresponse(socialusers).subscribe((res: any) => {
            debugger;
            console.log(res);
            this.socialusers=res;
            this.response = res.userDetail;
            localStorage.setItem('socialusers', JSON.stringify( this.socialusers));
            console.log(localStorage.setItem('socialusers', JSON.stringify(this.socialusers)));
            this.router.navigate([`/Dashboard`]);
        })
    }*/

    private socialSignInOther(socialProvider: string) {

        if (socialProvider === this.socialProviders.github.name) {
            const social = this.socialProviders.github.short;

            this.githubAuthService.loginWithGithub();
        }

        if (socialProvider === this.socialProviders.microsoft.name) {
            const social = this.socialProviders.microsoft.short;

            this.msalLogin();
        }
    }

    private sendSocialLoginRequest(id: string, social: string) {
        // console.log('Social login');
        // console.log('Login ' + id);

        this.socialService.socialServiceLogin(id, social).subscribe(
            (response: any) => {

                if (response.token) {
                    this.isLoginFailed = false;
                    this.isLoginSuccess = true;
                    this.successMessage = 'Social Login Successful.';
                    console.log(this.successMessage);
                    // console.log(response.token);

                    this.authenticationService.registerSuccessfulAuth(response.username, response.token, this.rememberUser);

                    /*console.log(sessionStorage.getItem('authenticatedUser'));
                    console.log(sessionStorage.getItem('token'));*/

                    this.router.navigate(['']);
                }
            }, error => {
                this.isLoginSuccess = false;
                this.isLoginFailed = true;
                this.successMessage = 'Login failed.';
                console.log(this.successMessage);
                /*console.log('log ');
                console.log(error);
                console.log('Authentication failed.');*/
                alert('Authentication failed.');

                if (error instanceof HttpErrorResponse) {
                    console.log('this.HttpErrorResponse');
                    console.log(error);
                    if (isNonEmptyString(error.error.message)) {
                        this.errorMessage = error.error.message;
                    } else {
                        this.errorMessage = error.error;
                    }
                }
            });
    }

    private localLoginTest() {
        // only for testing purpose
        if ((this.username === credentials.admin && this.password === credentials.adminPassword)
            || (this.username === credentials.user && this.password === credentials.userPassword)
        ) {
            // credentials error handle
            this.isLoginFailed = false;
            this.isLoginSuccess = true;

            this.authenticationService.registerSuccessfulLogin(this.username);
            /*console.log(sessionStorage.getItem(storageKeys.USER_NAME_SESSION_ATTRIBUTE_NAME));*/

            this.router.navigate(['']);
            this.userLoginEvent.emit();
        } else {
            this.isLoginFailed = true;
            this.isLoginSuccess = false;
        }
    }

    private initSocialLogin() {
        this.OAuth.authState.subscribe((user) => {
            this.user = user;
            this.loggedIn = (user != null);

            if (user !== null) {
                console.log(this.user);
            }
        });
    }

    // GITHUB

    private initMsgFromParent() {
        if (this.isConfirmSuccess) {
            this.isLoginSuccess = true;
            this.successMessage = this.confMessage;
        }
        if (this.isLogFailed) {
            this.isLoginFailed = this.isLogFailed;
            this.errorMessage = this.errMessage;
        }
    }

    private signOut(): void {
        this.OAuth.signOut();
    }

    private navigateToForgotPass() {
        this.userForgotEvent.emit();
    }

    private processGithubOauthCallback() {
        this.subscribeUrlParams();

        if (this.code != null) {
            this.githubAuthService.handleGithubOauthRequest(this.code);
        }

        this.githubAuthService.gitUserChange.subscribe((socialUser: any) => {
            if (socialUser.id) {
                this.sendSocialLoginRequest(socialUser.id, 'git');
            }
        });
    }

    private subscribeUrlParams() {
        console.log('Called Constructor');
        this.route.queryParams.subscribe(params => {
            this.code = params.code;
        });
        console.log('CODEEEEEEE' + this.code);
    }

    private msalInit() {
        this.isIframe = window !== window.parent && !window.opener;

        this.msalCheckoutAccount();

        this.broadcastService.subscribe('msal:loginSuccess', (account) => {
            this.msalCheckoutAccount();
            console.log('ACCOUNT  ' + JSON.stringify(account));
        });

        this.msalAuthService.handleRedirectCallback((authError, response) => {
            if (authError) {
                console.error('Redirect Error: ', authError.errorMessage);
                return;
            }

            console.log('Redirect Success: ', response.accessToken);
        });

        // this.msalAuthService.setLogger(new Logger((logLevel, message, piiEnabled) => {
        //     console.log('MSAL Logging: ', message);
        // }, {
        //     correlationId: CryptoUtils.createNewGuid(),
        //     piiLoggingEnabled: false
        // }));
    }
}
