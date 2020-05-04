import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from "@angular/core";
import { FormControl, FormGroup } from "@angular/forms";
import { ComponentWithErrorMsg } from "@app/components/account/forms/account-form/account-form.component";
import { AuthenticationRequest } from "@app/models/payload/AuthenticationRequest";
import { AuthenticationService } from "@app/services/account/auth.service";
import { FormControlService, togglePassTextType } from "@app/services/account/form-control.service";
import { SocialProviderService } from "@app/services/account/social/social-provider.service";
import {
    socialProviderNames,
    SocialUserStorageService
} from "@app/services/account/social/social-user-storage.service";
import { handleHttpErrorResponse } from "@app/services/helpers/global-http-interceptor-service.service";
import { actions } from "@app/services/navigation/app.endpoints";
import { NavigationService } from "@app/services/navigation/navigation.service";
import { DeviceInfoStorage } from "@app/utils/device-fingerprint";
import { isNonEmptyStrings } from "@app/utils/string-utils";
import { Subscription } from "rxjs";


/**
 * Login form component
 */
@Component({
    selector: 'app-login-form',
    templateUrl: './login-form.component.html',
    styleUrls: ["./login-form.component.scss"]
})
export class LoginFormComponent
    implements OnInit, OnDestroy, ComponentWithErrorMsg {

    @Input() isLogFailed: boolean;

    @Input() errMessage: string;

    @Input() isConfirmSuccess: boolean;

    @Input() confMessage: string;

    @Output()
    userLoginEvent: EventEmitter<any> = new EventEmitter<any>();

    @Output()
    userForgotEvent: EventEmitter<any> = new EventEmitter<any>();

    errorMessage: string;

    private loginServerReqSubscription: Subscription;

    private togglePassVisible = togglePassTextType;

    private showLoginForm = false;

    private username: string;

    private password: string;

    private successMessage: string;

    private isLoginSuccess = false;

    private isLoginFailed = false;

    private fieldTextTypePass: boolean;

    private submitted = false;

    private rememberUser = false;

    private loginForm: FormGroup;

    private socialProviders = socialProviderNames;

    constructor(
        private authenticationService: AuthenticationService,
        private navigationService: NavigationService,
        private socialService: SocialProviderService,
        private socialUserStorageService: SocialUserStorageService,
        private formControlService: FormControlService
    ) {
    }

    get name() {
        return this.loginForm.get("username");
    }

    get pass() {
        return this.loginForm.get('password');
    }

    /**
     * Initialize the directive/component after Angular first displays the data-bound properties
     * and sets the directive/component's input properties.
     * Called once, after the first ngOnChanges()
     */
    ngOnInit(): void {
        this.createForm();
        this.processGithubOauthCallback();
        this.initMsgFromParent();
    }

    /**
     * Cleanup just before Angular destroys the directive/component.
     * Unsubscribe Observables and detach event handlers to avoid memory leaks.
     * Called just before Angular destroys the directive/component
     */
    ngOnDestroy(): void {
        if (this.loginServerReqSubscription && !this.loginServerReqSubscription.closed) {
            this.loginServerReqSubscription.unsubscribe();
        }
    }

    /**
     * Submit form handler
     * @param valid - form validation state
     */
    onSubmit(valid: boolean) {
        this.submitted = true;
        if (this.loginForm.hasError('invalid')) {
            this.submitted = false;
            return;
        }

        if (isNonEmptyStrings(this.username, this.password)) {
            if (!valid) {
                return;
            }
            this.handleLogin();
        }
    }

    /**
     * Get operation message from parent component
     */
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

    /**
     * Show/hide login form
     */
    private showForm() {
        this.showLoginForm = !this.showLoginForm;
    }

    /**
     * Initiate login form group with validation
     */
    private createForm() {
        this.loginForm = new FormGroup({
            username: this.formControlService.getUsernameFormControl(this.username),
            password: this.formControlService.getPasswordFormControl(this.password),
            rememberMe: new FormControl()
        });
    }

    /**
     * Handle user profile login request
     */
    private handleLogin() {
        this.loginServerReqSubscription = this.authenticationService
            .processAuthentification(
                new AuthenticationRequest(this.username, this.password),
                DeviceInfoStorage.deviceInfo
            )
            .subscribe(this.handleLoginResponse(), this.handleLoginError()
            );
    }

    /**
     * Handle login server response
     */
    private handleLoginResponse() {
        return (response: any) => {
            if (response.token) {
                this.isLoginFailed = false;
                this.isLoginSuccess = true;
                this.successMessage = 'Login Successful.';

                this.authenticationService.registerSuccessfulAuth(
                    this.username,
                    response.token,
                    this.rememberUser
                );

                this.socialService.cleanGitAuth();
                this.navigationService.navigateToMain();
            }
        };
    }

    /**
     * Handle server response error
     */
    private handleLoginError() {
        return (error) => {
            this.handleSocialLoginError(error);
        };
    }

    /**
     * Sign in with specified social provider
     * (simplified way with common library 'angularx-social-login-vk')
     * and with other specified social providers
     * (full Oauth 2.0 flow or other separate social provider specific libraries)
     */
    private socialSignIn(socialProvider: string): void {
        // in advance subscribes to subject when server authentication response arrives
        this.subscribeToServerResponse();
        this.socialService.processSocialLogin(socialProvider, actions.login);
    }

    private handleSocialResponse() {
        return (response: any) => {
            console.log("RESPONSE " + JSON.stringify(response));
            if (response.token) {
                this.handleSocialLoginResponse(response);
            }
            if (response.error) {
                this.handleSocialLoginError(response);
            }
        };
    }

    /**
     * Handle social login server response
     */
    private handleSocialLoginResponse(response: any) {
        // if JWT is provided
        if (response.token) {
            this.isLoginFailed = false;
            this.isLoginSuccess = true;
            this.successMessage = "Social Login Successful.";

            // save user in browser storage
            this.authenticationService.registerSuccessfulAuth(
                response.username,
                response.token,
                this.rememberUser
            );

            // remove temporary Github auth data
            this.socialService.cleanGitAuth();
            this.navigationService.navigateToMain();
        }
    }

    /**
     * Handle server response error
     */
    private handleSocialLoginError(error) {
        this.isLoginSuccess = false;
        this.isLoginFailed = true;
        alert("Authentication failed.");
        this.socialService.cleanGitAuth();
        this.fullLogout();

        handleHttpErrorResponse(error, this);
    }

    /**
     * Handle redirect from Github Oauth 2.0 flow and prepare social user login request
     */
    private processGithubOauthCallback() {
        const code = this.socialService.getGitOauthCode();
        if (code != null) {
            this.subscribeToServerResponse();
            // if code is provided perform access token request
            this.socialService.handleGithubOauthRequest(code);
        } else {
            this.fullLogout();
        }
    }

    private subscribeToServerResponse() {
        this.socialUserStorageService.clearSocialResponse();
        this.socialUserStorageService.socialServerResponse.subscribe(
            this.handleSocialResponse()
        );
    }

    /**
     * Navigate to forgot password component layout
     */
    private navigateToForgotPass() {
        this.userForgotEvent.emit();
    }

    /**
     * Full logout from all profiles and services with cleaning all additional login information
     */
    private fullLogout() {
        this.authenticationService.fullLogout();
        /*if (this.msAuthSubscription && !this.msAuthSubscription.closed) {
            this.msAuthSubscription.unsubscribe();
        }*/
    }
}
