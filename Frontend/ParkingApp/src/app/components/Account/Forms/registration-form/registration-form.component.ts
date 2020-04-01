import {DOCUMENT} from '@angular/common';
import {HttpErrorResponse} from '@angular/common/http';
import {Component, EventEmitter, Inject, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {AuthService} from 'angularx-social-login-vk';
import {NgxUiLoaderService} from 'ngx-ui-loader';
import {Subscription} from 'rxjs';
import {
    AmazonSignUpRequest,
    CommonSocialSignUpRequest,
    GithubSignUpRequest,
    MicrosoftSignUpRequest,
    SocialSignUpRequest
} from '../../../../models/payload/SocialSignInRequest';
import {User} from '../../../../models/User';
import {AuthenticationService} from '../../../../services/account/auth.service';
import {providerNames, SocialAccountService} from '../../../../services/account/social/social-account.service';
import {SocialUserService} from '../../../../services/account/social/social-user.service';
import {actions} from '../../../../services/navigation/app.endpoints';
import {NavigationService} from '../../../../services/navigation/navigation.service';
import {DeviceInfoStorage} from '../../../../utils/device-fingerprint';
import {capitalize, isNonEmptyString} from '../../../../utils/string-utils';
import {FormControlService} from '../../../../validation/form-control.service';


@Component({
    selector: 'app-reg-form',
    templateUrl: './registration-form.component.html',
    styleUrls: ['./registration-form.component.css']
})
export class RegFormComponent implements OnInit {
    private username: string;

    private email: string;

    private firstname: string;

    private lastname: string;

    private password: string;

    private passConfirm: string;

    private errorMessage = 'Invalid Credentials';

    private successMessage: string;

    private invalidReg = false;

    private regSuccess = false;

    private fieldTextTypePass: boolean;

    private fieldTextTypePassConfirm: boolean;

    @Output()
    userLoginEvent: EventEmitter<any> = new EventEmitter<any>();

    private regForm: FormGroup;

    private submitted = false;

    private acceptTerms = false;

    private grecaptcha: any;

    private msAuthSubscription: Subscription;

    private msLoginAllowed = false;

    private socialProviders = providerNames;

    private redirectMessage = 'After 5 seconds, you will be redirected to the main page';


    constructor(
        @Inject(DOCUMENT) private document: any,
        private authenticationService: AuthenticationService,
        private OAuth: AuthService,
        private ngxService: NgxUiLoaderService,
        private navigationService: NavigationService,
        private socialService: SocialAccountService,
        private socialUserService: SocialUserService,
        private formControlService: FormControlService
    ) {
        this.grecaptcha = this.document.grecaptcha;
    }

    ngOnInit(): void {

        this.processGithubOauthCallback();

        this.regForm = new FormGroup({
            username: this.formControlService.getUsernameFormControl(
                this.username
            ),
            email: this.formControlService.getEmailFormControl(this.email),
            firstname: this.formControlService.getFirstnameFormControl(
                this.firstname
            ),
            lastname: this.formControlService.getLastnameFormControl(
                this.lastname
            ),
            password: this.formControlService.getPasswordFormControl(
                this.password
            ),
            passConfirm: this.formControlService.getConfirmPasswordFormControl(
                this.passConfirm
            ),
            acceptTerms: new FormControl(),
            captcha: new FormControl()
        }, this.formControlService.pwdConfirming('password', 'passConfirm').bind(this));


    }


    onSubmit(valid: boolean) {
        this.submitted = true;
        if (this.regForm.hasError('invalid')) {
            this.submitted = false;
        }

        const username = this.username;
        const email = this.email;
        const lastname = capitalize(this.lastname);
        const fullname = capitalize(this.firstname) + ' ' + lastname;
        const pass = this.password;

        if (isNonEmptyString(username) && isNonEmptyString(pass)) {
            console.log(username + '  ' + pass);
            const newUser = new User(
                null,
                username,
                email,
                pass,
                fullname,
                lastname
            );
            console.log(newUser + '  ');
            console.log('VALID' + valid);
            if (!valid) {
                return;
            }
            this.handleRegistration(newUser);
        }
    }

    navigateToLogin() {
        setTimeout(() => {
            this.userLoginEvent.emit();
        }, 5000);
    }

    navigateToMain() {
        setTimeout(() => {
            this.navigationService.navigateToMain();
        }, 5000);
    }

    // Switching method
    private togglePassTextType() {
        this.fieldTextTypePass = !this.fieldTextTypePass;
    }

    private togglePassConfirmTextType() {
        this.fieldTextTypePassConfirm = !this.fieldTextTypePassConfirm;
    }

    get name() {
        return this.regForm.get('username');
    }

    get mail() {
        return this.regForm.get('email');
    }

    get fname() {
        return this.regForm.get('firstname');
    }

    get lname() {
        return this.regForm.get('lastname');
    }

    get pass() {
        return this.regForm.get('password');
    }

    get passConf() {
        return this.regForm.get('passConfirm');
    }

    private handleRegistration(user: User) {
        /*this.ngxService.startLoader('loader-01'); // start foreground spinner of the loader "loader-01" with 'default' taskId
        // Stop the foreground loading after 5s
        setTimeout(() => {
            this.ngxService.stopLoader('loader-01'); // stop foreground spinner of the loader "loader-01" with 'default' taskId
        }, 5000);*/

        this.authenticationService
            .processRegistration(user, DeviceInfoStorage.deviceInfo)
            .subscribe(
                (response: any) => {
                    if (response.success && response.confirmationSent) {
                        this.invalidReg = false;
                        this.regSuccess = true;
                        this.successMessage =
                            'Registration Successful. We sent you mail to confirm your profile. ' +
                            this.redirectMessage;
                        console.log(this.successMessage);

                        this.socialUserService.cleanGitAuth();
                        this.navigateToMain();
                    } else {
                        this.invalidReg = true;
                        this.regSuccess = false;
                    }
                },
                error => {
                    this.invalidReg = true;
                    this.regSuccess = false;
                    console.log(error);
                    this.errorMessage = 'Registration failed. ';
                    console.log('Registration failed.');

                    this.socialUserService.cleanGitAuth();
                    this.fullLogout();

                    if (error instanceof HttpErrorResponse) {
                        this.errorMessage += error.error.message
                            ? error.error.message
                            : error.error;
                    }
                }
            );
    }

    private fullLogout() {
        this.signOut();
        this.authenticationService.processLogout();
        if (this.msAuthSubscription && !this.msAuthSubscription.closed) {
            this.msAuthSubscription.unsubscribe();
        }
        this.socialUserService.cleanGitAuth();
    }

    private socialSignUp(socialProvider: string): void {
        this.socialUserService.clearSocialUser();
        this.socialUserService.socialUser.subscribe((socialUser: any) => {
            if (socialUser != null) {
                const user = this.socialService.createUser(
                    socialUser,
                    socialProvider
                );
                if (user.id) {
                    this.sendSocialSignUpRequest(
                        new CommonSocialSignUpRequest(
                            user.id,
                            user,
                            providerNames[socialProvider]
                        )
                    );
                }
            }
        });

        this.socialService.socialSingIn(socialProvider);
    }

    private socialSignUpOther(socialProvider: string) {
        if (socialProvider === this.socialProviders.git) {
            this.socialUserService.setGitOauthAction(actions.registration);
            console.log('ACTION git ' + this.socialUserService.getGitOauthAction());
            setTimeout(null, 1000);
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

                    this.sendSocialSignUpRequest(
                        new AmazonSignUpRequest(
                            user.id,
                            user,
                            providerNames.amazon)
                    );
                }
            });
        }
        /*if (socialProvider === this.socialProviders.linkedin.name) {
            const social = this.socialProviders.linkedin.short;

            this.linkedInLogin();
        }*/
    }

    private sendSocialSignUpRequest(socialRequest: SocialSignUpRequest) {
        // console.log('Social login');
        // console.log('Login ' + id);

        this.socialService
            .socialServiceSignUp(socialRequest, DeviceInfoStorage.deviceInfo)
            .subscribe(
                (response: any) => {
                    if (response.success) {
                        this.invalidReg = false;
                        this.regSuccess = true;

                        this.socialUserService.cleanGitAuth();
                        if (response.confirmationSent) {
                            this.successMessage =
                                'Registration Successful. We sent you mail to confirm your social profile. ' +
                                this.redirectMessage;
                            this.navigateToMain();
                        } else {
                            this.successMessage =
                                'Registration Successful. You can login with your social profile.';
                            this.navigateToLogin();
                        }
                        console.log(this.successMessage);
                    } else {
                        this.invalidReg = true;
                        this.regSuccess = false;
                    }
                },
                error => {
                    this.invalidReg = true;
                    this.regSuccess = false;
                    console.log(error);
                    this.errorMessage = 'Registration failed. ';
                    console.log('Registration failed.');

                    this.socialUserService.cleanGitAuth();
                    this.fullLogout();

                    if (error instanceof HttpErrorResponse) {
                        this.errorMessage += error.error.message
                            ? error.error.message
                            : error.error;
                    }
                }
            );
    }

    private signOut(): void {
        this.OAuth.signOut();
    }

    private processMSCallback() {
        this.msAuthSubscription = this.socialService
            .getMsProfile()
            .subscribe((profile: any) => {
                console.log('PROFILE ' + JSON.stringify(profile));

                if (this.msLoginAllowed) {
                    if (profile.id) {
                        const user = this.socialService.createUser(
                            profile,
                            providerNames.ms
                        );

                        this.sendSocialSignUpRequest(
                            new MicrosoftSignUpRequest(
                                user.id,
                                user,
                                providerNames.microsoft)
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

    private processGithubOauthCallback() {

        const code = this.socialUserService.getGitOauthCode();
        if (code != null) {
            console.log('CODE ' + code);
            this.socialService.handleGithubOauthRequest(code);

            this.socialUserService.gitUser.subscribe((gitUser: any) => {
                if (gitUser != null) {
                    if (gitUser.id) {
                        const user = this.socialService.createUser(
                            gitUser,
                            providerNames.git
                        );

                        this.sendSocialSignUpRequest(
                            new GithubSignUpRequest(
                                user.id,
                                user,
                                providerNames.github)
                        );
                    }
                }
            });
        }
    }
}
