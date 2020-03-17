import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';
import {AuthService, FacebookLoginProvider, GoogleLoginProvider, SocialUser} from 'angularx-social-login';
import {AuthenticationService} from '../../../../services/account/auth.service';
import {RegularExpressions} from '../../../../validation/reg-exp-patterns';
import {regexpTestValidator} from '../../../../validation/regexp-name-validator';
import {isNonEmptyString} from '../../../../utils/string-utils';
import {providerNames, SocialAuthService} from '../../../../services/account/social-auth.service';
import {credentials} from '../../../../validation/credentials';


@Component({
    selector: 'app-login-form',
    templateUrl: './login-form.component.html',
    styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent implements OnInit {

    private user: SocialUser;

    private username: string;
    private password: string;

    private errorMessage = 'Invalid Credentials';
    private successMessage: string;

    private isLoginSuccess = false;
    private isLoginFailed = false;

    @Input() isLogFailed: boolean;
    @Input() errMessage: string;
    @Input() isConfirmSuccess: boolean;
    @Input() confMessage: string;

    private fieldTextTypePass: boolean;

    private loggedIn: boolean;

    private submitted = false;

    private rememberUser = false;

    @Output()
    userLoginEvent: EventEmitter<any> = new EventEmitter<any>();
    @Output()
    userForgotEvent: EventEmitter<any> = new EventEmitter<any>();

    private loginForm: FormGroup;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private authenticationService: AuthenticationService,
        private OAuth: AuthService,
        private socialService: SocialAuthService) {
    }

    get name() {
        return this.loginForm.get('username');
    }

    get pass() {
        return this.loginForm.get('password');
    }

    // Switching method
    togglePassTextType() {
        this.fieldTextTypePass = !this.fieldTextTypePass;
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

        this.initSocialLogin();
        this.initMsgFromParent();
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

    handleLogin() {
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

    socialSignIn(socialProvider: string): void {

        let socialPlatformProvider: string;
        let social: string;

        if (socialProvider === providerNames.facebook.name) {
            socialPlatformProvider = FacebookLoginProvider.PROVIDER_ID;
            social = providerNames.facebook.short;
        }
        if (socialProvider === providerNames.google.name) {
            socialPlatformProvider = GoogleLoginProvider.PROVIDER_ID;
            social = providerNames.google.short;
        }

        this.OAuth.signIn(socialPlatformProvider)
            .then((socialuser) => {
                console.log('then ', socialProvider, socialuser);

                if (socialuser !== null) {
                    console.log(this.user);
                    console.log(socialuser.provider);
                    console.log(this.OAuth.readyState);

                    this.sendSocialLoginRequest(socialuser.id, social);
                }
            });
    }

    sendSocialLoginRequest(id: string, social: string) {
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

    signOut(): void {
        this.OAuth.signOut();
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

    navigateToForgotPass() {
        this.userForgotEvent.emit();
    }
}
