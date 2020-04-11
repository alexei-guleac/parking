import {DOCUMENT} from '@angular/common';
import {HttpErrorResponse} from '@angular/common/http';
import {Component, EventEmitter, Inject, OnDestroy, OnInit, Output,} from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {
    AmazonSignUpRequest,
    CommonSocialSignUpRequest,
    GithubSignUpRequest,
    MicrosoftSignUpRequest,
    SocialSignUpRequest,
} from '@app/models/payload/SocialSignInRequest';
import {User} from '@app/models/User';
import {AuthenticationService} from '@app/services/account/auth.service';
import {
    FormControlService,
    togglePassConfirmTextType,
    togglePassTextType,
} from '@app/services/account/form-control.service';
import {providerNames, SocialAccountService,} from '@app/services/account/social/social-account.service';
import {SocialUserService} from '@app/services/account/social/social-user.service';
import {ModalService} from '@app/services/modals/modal.service';
import {actions} from '@app/services/navigation/app.endpoints';
import {NavigationService} from '@app/services/navigation/navigation.service';
import {DeviceInfoStorage} from '@app/utils/device-fingerprint';
import {capitalize, isNonEmptyStrings} from '@app/utils/string-utils';
import {AuthService} from 'angularx-social-login-vk';
import {NgxUiLoaderService} from 'ngx-ui-loader';
import {Subscription} from 'rxjs';


@Component({
    selector: 'app-reg-form',
    templateUrl: './registration-form.component.html',
    styleUrls: ['./registration-form.component.scss'],
})
export class RegFormComponent implements OnInit, OnDestroy {
    private registrationSubscription: Subscription;

    // Switching method
    private togglePassVisible = togglePassTextType;

    private togglePassConfirmVisible = togglePassConfirmTextType;

    private redirectMessage =
        'After 5 seconds, you will be redirected to the main page';

    constructor(
        @Inject(DOCUMENT) private document: any,
        private authenticationService: AuthenticationService,
        private OAuthService: AuthService,
        private ngxService: NgxUiLoaderService,
        private navigationService: NavigationService,
        private socialService: SocialAccountService,
        private socialUserService: SocialUserService,
        private formControlService: FormControlService,
        private modalService: ModalService
    ) {
        this.grecaptcha = this.document.grecaptcha;
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

    get lname() {
        return this.regForm.get('lastname');
    }

    get pass() {
        return this.regForm.get('password');
    }

    get passConf() {
        return this.regForm.get('passConfirm');
    }

    ngOnInit(): void {
        this.processGithubOauthCallback();
        this.createForm();
    }

    ngOnDestroy(): void {
        if (
            this.registrationSubscription &&
            !this.registrationSubscription.closed
        ) {
            this.registrationSubscription.unsubscribe();
        }
    }

    onSubmit(valid: boolean) {
        this.submitted = true;
        if (this.regForm.hasError('invalid')) {
            this.submitted = false;
        }

        const username = this.username;
        const email = this.email;
        const lastname = capitalize(this.lastname);
        const firstname = capitalize(this.firstname);
        const pass = this.password;

        if (isNonEmptyStrings(username, pass)) {
            // console.log(username + '  ' + pass);
            const newUser = new User(
                null,
                username,
                email,
                pass,
                firstname,
                lastname
            );
            if (!valid) {
                return;
            }
            this.handleRegistration(newUser);
        }
    }

    openTermsOfService(longContent) {
        this.modalService.openScrollModal(longContent);
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

    private createForm() {
        this.regForm = new FormGroup(
            {
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
                captcha: new FormControl(),
            },
            this.formControlService
                .checkPasswordConfirm('password', 'passConfirm')
                .bind(this)
        );
    }

    private handleRegistration(user: User) {
        /*this.ngxService.startLoader('loader-01'); // start foreground spinner of the loader "loader-01" with 'default' taskId
        // Stop the foreground loading after 5s
        setTimeout(() => {
            this.ngxService.stopLoader('loader-01'); // stop foreground spinner of the loader "loader-01" with 'default' taskId
        }, 5000);*/

        this.registrationSubscription = this.authenticationService
            .processRegistration(user, DeviceInfoStorage.deviceInfo)
            .subscribe(
                this.handleRegistrationResponse(),
                this.handleRegistrationError()
            );
    }

    private handleRegistrationResponse() {
        return (response: any) => {
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
        };
    }

    private socialSignUp(socialProvider: string): void {
        this.socialUserService.clearSocialUser();
        this.socialUserService.socialUser.subscribe(
            this.processSocialSignUp(socialProvider)
        );

        this.socialService.socialSingIn(socialProvider);
    }

    private processSocialSignUp(socialProvider: string) {
        return (socialUser: any) => {
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
        };
    }

    private socialSignUpOther(socialProvider: string) {
        if (socialProvider === this.socialProviders.git) {
            this.socialUserService.setGitOauthAction(actions.registration);
            // console.log('ACTION git ' + this.socialUserService.getGitOauthAction());
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
            // console.log('Amazon works ' + this.socialUserService.socialUser);
            this.socialUserService.socialUser.subscribe((socialUser: any) => {
                if (socialUser != null) {
                    const user = this.socialUserService.createAmazonUser(
                        socialUser
                    );

                    this.sendSocialSignUpRequest(
                        new AmazonSignUpRequest(
                            user.id,
                            user,
                            providerNames.amazon
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

    private sendSocialSignUpRequest(socialRequest: SocialSignUpRequest) {
        // console.log('Social login');
        // console.log('Login ' + id);

        this.socialService
            .socialServiceSignUp(socialRequest, DeviceInfoStorage.deviceInfo)
            .subscribe(
                this.handleSocialRegistrationResponse(),
                this.handleRegistrationError()
            );
    }

    private handleSocialRegistrationResponse() {
        return (response: any) => {
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
                // console.log(this.successMessage);
            } else {
                this.invalidReg = true;
                this.regSuccess = false;
            }
        };
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
                                providerNames.microsoft
                            )
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
                                providerNames.github
                            )
                        );
                    }
                }
            });
        }
    }

    private handleRegistrationError() {
        return (error) => {
            this.invalidReg = true;
            this.regSuccess = false;
            // console.log(error);
            this.errorMessage = 'Registration failed. ';
            // console.log('Registration failed.');

            this.socialUserService.cleanGitAuth();
            this.fullLogout();

            if (error instanceof HttpErrorResponse) {
                this.errorMessage += error.error.message
                    ? error.error.message
                    : error.error;
            }
        };
    }

    private fullLogout() {
        this.OAuthService.signOut();
        this.authenticationService.processLogout();
        if (this.msAuthSubscription && !this.msAuthSubscription.closed) {
            this.msAuthSubscription.unsubscribe();
        }
        this.socialUserService.cleanGitAuth();
    }
}
