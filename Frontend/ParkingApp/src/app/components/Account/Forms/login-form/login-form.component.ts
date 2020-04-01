import {HttpErrorResponse} from '@angular/common/http';
import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {AuthService} from 'angularx-social-login-vk';
import {Subscription} from 'rxjs';
import {AuthenticationRequest} from '../../../../models/payload/AuthenticationRequest';
import {
    AmazonAuthRequest,
    CommonSocialAuthRequest,
    GithubAuthRequest,
    MicrosoftAuthRequest,
    SocialSignInRequest
} from '../../../../models/payload/SocialSignInRequest';
import {AuthenticationService} from '../../../../services/account/auth.service';
import {providerNames, SocialAccountService} from '../../../../services/account/social/social-account.service';
import {SocialUserService} from '../../../../services/account/social/social-user.service';
import {LoggerService} from '../../../../services/helpers/logger.service';
import {actions} from "../../../../services/navigation/app.endpoints";
import {NavigationService} from '../../../../services/navigation/navigation.service';
import {DeviceInfoStorage} from '../../../../utils/device-fingerprint';
import {isNonEmptyString} from '../../../../utils/string-utils';
import {FormControlService} from '../../../../validation/form-control.service';


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

    private username: string;

    private password: string;

    private errorMessage = 'Invalid Credentials';

    private successMessage: string;

    private isLoginSuccess = false;

    private isLoginFailed = false;

    private fieldTextTypePass: boolean;

    private submitted = false;

    private rememberUser = false;

    private loginForm: FormGroup;

    private code: string;

    private msAuthSubscription: Subscription;

    private msLoginAllowed = false;

    private socialProviders = providerNames;

    constructor(
        private authenticationService: AuthenticationService,
        private OAuth: AuthService,
        private navigationService: NavigationService,
        private socialService: SocialAccountService,
        private socialUserService: SocialUserService,
        private log: LoggerService,
        /*private ngxLinkedinService: NgxLinkedinService,*/
        private formControlService: FormControlService
    ) {
    }

    get name() {
        return this.loginForm.get('username');
    }

    get pass() {
        return this.loginForm.get('password');
    }

    ngOnInit(): void {
        this.fullLogout();

        this.loginForm = new FormGroup({
            username: this.formControlService.getUsernameFormControl(
                this.username
            ),
            password: this.formControlService.getPasswordFormControl(
                this.password
            ),
            rememberMe: new FormControl()
        });

        // this.initSocialLogin();
        this.processGithubOauthCallback();
        // this.processMSCallback();

        this.initMsgFromParent();
        this.log.logMethod('Hellloooo!!!');
    }

    onSubmit(valid: boolean) {

        console.log('DEVICE in login' + JSON.stringify(DeviceInfoStorage.deviceInfo));

        this.submitted = true;
        if (this.loginForm.hasError('invalid')) {
            this.submitted = false;
            return;
        }

        if (isNonEmptyString(this.username) && isNonEmptyString(this.password)) {
            console.log(this.loginForm.hasError('invalid'));
            console.log(this.username + '  ' + this.password);
            console.log('VALID' + valid);
            if (!valid) {
                return;
            }
            this.handleLogin();
        }
    }

    // Switching method
    private togglePassTextType() {
        this.fieldTextTypePass = !this.fieldTextTypePass;
    }

    private handleLogin() {
        this.authenticationService
            .processAuthentification(
                new AuthenticationRequest(this.username, this.password),
                DeviceInfoStorage.deviceInfo
            )
            .subscribe(
                (response: any) => {
                    if (response.token) {
                        this.isLoginFailed = false;
                        this.isLoginSuccess = true;
                        this.successMessage = 'Login Successful.';
                        console.log(this.successMessage);
                        // console.log(response.token);

                        this.authenticationService.registerSuccessfulAuth(
                            this.username,
                            response.token,
                            this.rememberUser
                        );

                        this.socialUserService.cleanGitAuth();
                        this.navigationService.navigateToMain();

                    }
                },
                error => {
                    this.isLoginSuccess = false;
                    this.isLoginFailed = true;
                    /*console.log('log ');
                console.log(error);
                console.log('Authentication failed.');*/
                    alert('Authentication failed.');

                    this.fullLogout();

                    if (error instanceof HttpErrorResponse) {
                        this.errorMessage = error.error.message
                            ? error.error.message
                            : error.error;
                    }
                }
            );
    }

    private fullLogout() {
        this.signOut();
        this.authenticationService.processLogout();
        /*if (this.msAuthSubscription && !this.msAuthSubscription.closed) {
            this.msAuthSubscription.unsubscribe();
        }*/
        this.socialUserService.cleanGitAuth();
    }

    private socialSignIn(socialProvider: string): void {
        this.socialUserService.clearSocialUser();
        this.socialUserService.socialUser.subscribe((socialUser: any) => {
            if (socialUser != null) {
                if (socialUser.id) {
                    this.sendSocialLoginRequest(
                        new CommonSocialAuthRequest(
                            socialUser.id,
                            providerNames[socialProvider]
                        )
                    );
                }
            }
        });
        this.socialService.socialSingIn(socialProvider);
    }

    private socialSignInOther(socialProvider: string) {
        if (socialProvider === this.socialProviders.git) {
            this.socialUserService.setGitOauthAction(actions.login);
            this.socialService.githubLogin();
        }
        if (socialProvider === this.socialProviders.ms) {
            this.socialService.microsoftLogin();
            this.msLoginAllowed = true;
            this.processMSCallback();
        }
        if (socialProvider === this.socialProviders.a) {
            this.socialUserService.clearSocialUser();
            console.log('Amazon works ' + this.socialUserService.socialUser);
            this.socialUserService.socialUser.subscribe((socialUser: any) => {
                if (socialUser != null) {
                    const user = this.socialUserService.createAmazonUser(socialUser);

                    this.sendSocialLoginRequest(
                        new AmazonAuthRequest(
                            user.id,
                        )
                    );
                }
            });
        }

        /*if (socialProvider === this.socialProviders.linkedin.name) {
            const social = this.socialProviders.linkedin.short;

            this.linkedInLogin();
        }*/
    }

    private sendSocialLoginRequest(socialRequest: SocialSignInRequest) {
        // console.log('Social login');
        // console.log('Login ' + id);

        this.socialService
            .socialServiceLogin(socialRequest, DeviceInfoStorage.deviceInfo)
            .subscribe(
                (response: any) => {
                    if (response.token) {
                        this.isLoginFailed = false;
                        this.isLoginSuccess = true;
                        this.successMessage = 'Social Login Successful.';
                        console.log(this.successMessage);
                        // console.log(response.token);

                        this.authenticationService.registerSuccessfulAuth(
                            response.username,
                            response.token,
                            this.rememberUser
                        );

                        this.socialUserService.cleanGitAuth();
                        this.navigationService.navigateToMain();
                    }
                },
                error => {
                    this.isLoginSuccess = false;
                    this.isLoginFailed = true;
                    this.successMessage = 'Login failed.';
                    console.log(this.successMessage);
                    /*console.log('log ');
                console.log(error);*/
                    console.log('Authentication failed.');
                    alert('Authentication failed.');

                    this.socialUserService.cleanGitAuth();
                    this.fullLogout();

                    if (error instanceof HttpErrorResponse) {
                        this.errorMessage = error.error.message
                            ? error.error.message
                            : error.error;
                    }
                }
            );
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

    private signOut(): void {
        this.OAuth.signOut();
    }

    private navigateToForgotPass() {
        this.userForgotEvent.emit();
    }

    private processGithubOauthCallback() {
        const code = this.socialUserService.getGitOauthCode();
        if (code != null) {
            console.log('CODE ' + code);
            this.socialService.handleGithubOauthRequest(code);

            this.socialUserService.gitUser.subscribe((gitUser: any) => {
                if (gitUser != null) {
                    if (gitUser.id) {
                        this.sendSocialLoginRequest(
                            new GithubAuthRequest(gitUser.id)
                        );
                    }
                }
            });
        }
    }

    private processMSCallback() {
        this.msAuthSubscription = this.socialService
            .getMsProfile()
            .subscribe((profile: any) => {
                console.log('PROFILE ' + JSON.stringify(profile));

                if (this.msLoginAllowed) {
                    if (profile.id) {
                        this.sendSocialLoginRequest(
                            new MicrosoftAuthRequest(profile.id)
                        );
                    }
                }
            });
    }

    /*private linkedInLogin() {
        this.ngxLinkedinService.signIn().subscribe((user: any) => {
            console.log('signIn', user);

            if (user.id) {
                this.sendSocialLoginRequest(new LinkedInAuthRequest(user.id));
            }
        });
    }*/
}
