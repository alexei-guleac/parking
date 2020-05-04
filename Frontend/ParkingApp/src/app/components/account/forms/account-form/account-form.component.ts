import { Component } from "@angular/core";
import { fadeInOut } from "@app/components/animations/animations";
import { ServerErrorMessage } from "@app/models/ServerErrorMessage";
import { SocialUserStorageService } from "@app/services/account/social/social-user-storage.service";
import { ModalService } from "@app/services/modals/modal.service";
import { actions } from "@app/services/navigation/app.endpoints";
import { NavigationService } from "@app/services/navigation/navigation.service";
import { DeviceInfoStorage } from "@app/utils/device-fingerprint";
import { containsString } from "@app/utils/string-utils";


/**
 * Root component for user account forms (login, registration, forgot password, password reset)
 */
@Component({
    selector: 'app-account-form',
    animations: [fadeInOut],
    templateUrl: './account-form.component.html',
    styleUrls: ["./account-form.component.scss"]
})
export class AccountFormComponent {

    private login = true;

    private register = false;

    private forgotPass = false;

    private resetPass = false;

    private showHint = false;

    private isLoginFailed = false;

    private isConfirmSuccess = false;

    private errorMessage: string;

    private successMessage: string;

    private confirmationToken: string;

    constructor(
        private navigationService: NavigationService,
        private socialUserStorageService: SocialUserStorageService,
        private modalService: ModalService
    ) {
        this.handleRedirectFromErrorInterceptor();
        this.subscribeUrlParams();
        DeviceInfoStorage._getComponentsInfo();
    }

    /**
     * Subscribing to query string URL parameters
     */
    private subscribeUrlParams() {
        this.navigationService.subscribeUrlParams(
            this.getQueryParamsCallback()
        );
    }

    /**
     * Callback function for processing query string URL parameters
     */
    private getQueryParamsCallback() {
        return (params) => {
            // if params contains code field, mean it's redirect from GitHub auth flow
            // save it for later GitHub auth processing
            if (params.code) {
                this.socialUserStorageService.setGitOauthCode(params.code);

                // depends on what action was initiated (login with GitHub social service or registration)
                // set corresponding form layout
                if (this.socialUserStorageService.getGitOauthAction() === actions.login) {
                    this.setLoginLayout();
                }
                if (this.socialUserStorageService.getGitOauthAction() === actions.registration) {
                    this.setRegistrationLayout();
                }
            } else {
                // if it'profile password reset and query contains confirmation token
                // switch to reset password layout
                if (params.action === actions.reset) {
                    if (params.confirmation_token) {
                        this.confirmationToken = params.confirmation_token;
                        this.setResetPassLayout();
                    }
                }
                if (params.action === actions.login) {
                    this.setLoginLayout();
                }
            }
        };
    }

    // Show error message from global error interceptor
    // or registration confirmation service
    /**
     * Handles redirect from global application error interceptor
     * (when server returns 401 error (unauthorized))
     */
    private handleRedirectFromErrorInterceptor() {

        const state = this.navigationService.getRouterState();

        if (state != null) {
            if (state.errors) {
                // get error corresponding it gets from server
                const error = state.errors[0] as ServerErrorMessage;
                const errorMsg = error.message;

                this.login = true;
                this.isLoginFailed = true;
                // if message contains 'token', implied that token was expired or malformed
                if (containsString(errorMsg, 'token')) {
                    this.errorMessage = errorMsg + '. \nLog in again.';
                } else {
                    this.errorMessage = errorMsg;
                }
            }

            // if some message is present mean login success
            if (state.message) {
                this.login = true;
                this.isConfirmSuccess = true;
                this.successMessage = state.message;
            }
        }
    }

    /**
     * Show form fields hint
     */
    private toggleHint() {
        this.modalService.openFormHintModal();

        setTimeout(() => {
            this.showHint = false;
        }, 60000);
    }

    /**
     * Navigate to main page
     */
    private navigateToMain() {
        this.navigationService.navigateToMain();
    }

    /**
     * Navigate to login page
     */
    private navigateToLogin() {
        // clean Github auth information for avoid conflicts
        // when switching from registration to login
        this.socialUserStorageService.cleanGitAuth();
        this.setLoginLayout();
    }

    /**
     * Navigate to registration page
     */
    private navigateToRegistration() {
        // clean Github auth information for avoid conflicts
        // when switching from login to registration
        this.socialUserStorageService.cleanGitAuth();
        this.setRegistrationLayout();
    }

    /**
     * Set login form component layout
     */
    private setLoginLayout() {
        this.switchLayout(true, false, false, false);
    }

    /**
     * Set registration form component layout
     */
    private setRegistrationLayout() {
        this.switchLayout(false, true, false, false);
    }

    /**
     * Set forgot password form component layout
     */
    private setForgotPassLayout() {
        this.switchLayout(false, false, true, false);
    }

    /**
     * Set reset password form component layout
     */
    private setResetPassLayout() {
        this.switchLayout(false, false, false, true);
    }

    /**
     * Switch to specified layout
     * @param stateLogin - login layout state
     * @param stateRegister - registration layout state
     * @param stateForgotPassword - forgot password layout state
     * @param stateResetPassword - reset password layout state
     */
    private switchLayout(
        stateLogin: boolean,
        stateRegister: boolean,
        stateForgotPassword: boolean,
        stateResetPassword: boolean
    ) {
        this.login = stateLogin;
        this.register = stateRegister;
        this.forgotPass = stateForgotPassword;
        this.resetPass = stateResetPassword;
    }
}

/**
 * Common object with error message
 */
export interface ComponentWithErrorMsg {
    errorMessage: string;
}
