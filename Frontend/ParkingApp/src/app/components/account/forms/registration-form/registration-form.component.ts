import { DOCUMENT } from "@angular/common";
import { HttpErrorResponse } from "@angular/common/http";
import { Component, EventEmitter, Inject, OnDestroy, OnInit, Output } from "@angular/core";
import { FormControl, FormGroup } from "@angular/forms";
import { User } from "@app/models/User";
import { AuthenticationService } from "@app/services/account/auth.service";
import {
    FormControlService,
    togglePassConfirmTextType,
    togglePassTextType
} from "@app/services/account/form-control.service";
import { SocialAccountService } from "@app/services/account/social/social-account.service";
import { SocialProviderService } from "@app/services/account/social/social-provider.service";
import {
    socialProviderNames,
    SocialUserStorageService
} from "@app/services/account/social/social-user-storage.service";
import { ModalService } from "@app/services/modals/modal.service";
import { actions } from "@app/services/navigation/app.endpoints";
import { NavigationService } from "@app/services/navigation/navigation.service";
import { DeviceInfoStorage } from "@app/utils/device-fingerprint";
import { capitalize, isNonEmptyStrings } from "@app/utils/string-utils";
import { AuthService } from "angularx-social-login-vk";
import { NgxUiLoaderService } from "ngx-ui-loader";
import { Subscription } from "rxjs";


/**
 * Registration form component
 */
@Component({
    selector: 'app-reg-form',
    templateUrl: './registration-form.component.html',
    styleUrls: ["./registration-form.component.scss"]
})
export class RegFormComponent implements OnInit, OnDestroy {

    @Output()
    userLoginEvent: EventEmitter<any> = new EventEmitter<any>();

    private username: string;

    private email: string;

    private firstname: string;

    private lastname: string;

    private password: string;

    private passConfirm: string;

    private errorMessage = "Invalid Credentials";

    private successMessage: string;

    private invalidReg = false;

    private regSuccess = false;

    private regForm: FormGroup;

    private submitted = false;

    private acceptTerms = false;

    private gRecaptcha: any;

    private socialProviders = socialProviderNames;

    private registrationServerReqSubscription: Subscription;

    // Switching method
    private togglePassVisible = togglePassTextType;

    private togglePassConfirmVisible = togglePassConfirmTextType;

    private redirectMessage =
        'After 5 seconds, you will be redirected to the main page';

    private showRegForm = false;

    private fieldTextTypePass: boolean;

    private fieldTextTypePassConfirm: boolean;

    constructor(
        @Inject(DOCUMENT) private document: any,
        private authenticationService: AuthenticationService,
        private OAuthService: AuthService,
        private ngxService: NgxUiLoaderService,
        private navigationService: NavigationService,
        private socialService: SocialProviderService,
        private socialAccountService: SocialAccountService,
        private socialUserStorageService: SocialUserStorageService,
        private formControlService: FormControlService,
        private modalService: ModalService
    ) {
        this.gRecaptcha = this.document.gRecaptcha;
    }

    get lname() {
        return this.regForm.get("lastname");
    }

    get pass() {
        return this.regForm.get("password");
    }

    get passConf() {
        return this.regForm.get("passConfirm");
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

    /**
     * Initialize the directive/component after Angular first displays the data-bound properties
     * and sets the directive/component's input properties.
     * Called once, after the first ngOnChanges()
     */
    ngOnInit(): void {
        this.createForm();
        this.processGithubOauthCallback();
    }

    /**
     * Cleanup just before Angular destroys the directive/component.
     * Unsubscribe Observables and detach event handlers to avoid memory leaks.
     * Called just before Angular destroys the directive/component
     */
    ngOnDestroy(): void {
        if (this.registrationServerReqSubscription &&
            !this.registrationServerReqSubscription.closed) {
            this.registrationServerReqSubscription.unsubscribe();
        }
    }

    /**
     * Submit form handler
     * @param valid - form validation state
     */
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
            // prepare user for request
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

    /**
     * Open terms of service modal window
     * @param windowContent - terms of service modal window content
     */
    openTermsOfService(windowContent) {
        this.modalService.openScrollModal(windowContent);
    }

    /**
     * Navigate to login
     */
    navigateToLogin() {
        setTimeout(() => {
            this.userLoginEvent.emit();
        }, 5000);
    }

    /**
     * Navigate to main after 5 seconds delay
     */
    navigateToMain() {
        setTimeout(() => {
            this.navigationService.navigateToMain();
        }, 5000);
    }

    /**
     * Initiate registration form group with validation
     */
    private createForm() {
        this.regForm = new FormGroup(
            {
                username: this.formControlService.getUsernameFormControl(this.username),
                email: this.formControlService.getEmailFormControl(this.email),
                firstname: this.formControlService.getFirstnameFormControl(this.firstname),
                lastname: this.formControlService.getLastnameFormControl(this.lastname),
                password: this.formControlService.getPasswordFormControl(this.password),
                passConfirm: this.formControlService.getConfirmPasswordFormControl(this.passConfirm),
                acceptTerms: new FormControl(),
                captcha: new FormControl()
            },
            this.formControlService
                .checkPasswordConfirm('password', 'passConfirm')
                .bind(this)
        );
    }


    /**
     * Show/hide login form
     */
    private showForm() {
        this.showRegForm = !this.showRegForm;
    }

    /**
     * Handle user profile registration request
     */
    private handleRegistration(user: User) {
        /*this.ngxService.startLoader('loader-01'); // start foreground spinner of the loader "loader-01" with 'default' taskId
        // Stop the foreground loading after 5s
        setTimeout(() => {
            this.ngxService.stopLoader('loader-01'); // stop foreground spinner of the loader "loader-01" with 'default' taskId
        }, 5000);*/

        this.registrationServerReqSubscription = this.authenticationService
            .processRegistration(user, DeviceInfoStorage.deviceInfo)
            .subscribe(
                this.handleRegistrationResponse(),
                this.handleRegistrationError()
            );
    }

    /**
     * Handle registration server response
     */
    private handleRegistrationResponse() {
        return (response: any) => {
            if (response.success && response.confirmationSent) {
                this.invalidReg = false;
                this.regSuccess = true;
                this.successMessage =
                    'Registration Successful. We sent you mail to confirm your profile. ' +
                    this.redirectMessage;
                this.socialService.cleanGitAuth();
                this.navigateToMain();
            } else {
                this.invalidReg = true;
                this.regSuccess = false;
            }
        };
    }

    /**
     * Handle server response error
     */
    private handleRegistrationError() {
        return (error) => {
            this.handleSocialRegistrationError(error);
        };
    }

    /**
     * Sign up with specified social provider
     * (simplified way with common library 'angularx-social-login-vk')
     * and with other specified social providers
     * (full Oauth 2.0 flow or other separate social provider specific libraries)
     */
    private socialSignUp(socialProvider: string): void {
        // in advance subscribes to subject when server authentication response arrives
        this.subscribeToServerResponse();
        this.socialService.processSocialLogin(socialProvider, actions.registration);
    }

    private subscribeToServerResponse() {
        this.socialUserStorageService.clearSocialResponse();
        this.socialUserStorageService.socialServerResponse.subscribe(
            this.handleSocialResponse()
        );
    }

    private handleSocialResponse() {
        return (response: any) => {
            console.log("RESPONSE " + JSON.stringify(response));
            if (response.success) {
                this.handleSocialRegistrationResponse(response);
            }
            if (response.error) {
                this.handleSocialRegistrationError(response);
            }
        };
    }

    /**
     * Handle server response error
     */
    private handleSocialRegistrationError(error) {
        this.invalidReg = true;
        this.regSuccess = false;
        this.errorMessage = "Registration failed. ";

        // clean all user data and full logout
        this.socialService.cleanGitAuth();
        this.fullLogout();

        if (error instanceof HttpErrorResponse) {
            this.errorMessage += error.error.message
                ? error.error.message
                : error.error;
        }
    }

    /**
     * Handle social registration server response
     */
    private handleSocialRegistrationResponse(response: any) {
        if (response.success) {
            this.invalidReg = false;
            this.regSuccess = true;

            // remove temporary Github auth data
            this.socialService.cleanGitAuth();
            if (response.confirmationSent) {
                this.successMessage =
                    "Registration Successful. We sent you mail to confirm your social profile. " +
                    this.redirectMessage;
                this.navigateToMain();
            } else {
                this.successMessage =
                    "Registration Successful. You can login with your social profile.";
                this.navigateToLogin();
            }
        } else {
            this.invalidReg = true;
            this.regSuccess = false;
        }
    }

    /**
     * Handle redirect from Github Oauth 2.0 flow and prepare social user registration request
     */
    private processGithubOauthCallback() {
        const code = this.socialService.getGitOauthCode();
        if (code != null) {
            this.subscribeToServerResponse();
            // if code is provided perform access token request
            this.socialService.handleGithubOauthRequest(code);
        }
    }

    /**
     * Initiate user logout from current account and all social service providers
     * with removing all related data
     */
    private fullLogout() {
        this.authenticationService.fullLogout();
    }
}
