import {Component, OnInit} from '@angular/core';
import {fadeInOut} from '@app/components/animations/animations';
import {ServerErrorMessage} from '@app/models/ServerErrorMessage';
import {SocialUserService} from '@app/services/account/social/social-user.service';
import {actions} from '@app/services/navigation/app.endpoints';
import {NavigationService} from '@app/services/navigation/navigation.service';
import {DeviceInfoStorage} from '@app/utils/device-fingerprint';
import {containsString} from '@app/utils/string-utils';


@Component({
    selector: 'app-account-form',
    animations: [fadeInOut],
    templateUrl: './account-form.component.html',
    styleUrls: ['./account-form.component.scss'],
})
export class AccountFormComponent implements OnInit {
    constructor(
        private navigationService: NavigationService,
        private socialUserService: SocialUserService
    ) {
        this.handleRedirectFromErrorInterceptor();
        this.subscribeUrlParams();
        DeviceInfoStorage._getComponentsInfo();
    }

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

    ngOnInit() {
    }

    private subscribeUrlParams() {
        // console.log('Called Constructor');
        this.navigationService.subscribeUrlParams(
            this.getQueryParamsCallback()
        );
    }

    private getQueryParamsCallback() {
        return (params) => {
            if (params.code) {
                // console.log(' gitCode = ' + params.code);
                // console.log(' this.socialUserService.gitAuth.action = ' + this.socialUserService.getGitOauthAction());
                this.socialUserService.setGitOauthCode(params.code);

                if (
                    this.socialUserService.getGitOauthAction() === actions.login
                ) {
                    this.setLoginLayout();
                }
                if (
                    this.socialUserService.getGitOauthAction() ===
                    actions.registration
                ) {
                    this.setRegistrationLayout();
                }
            } else {
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
    private handleRedirectFromErrorInterceptor() {
        const state = this.navigationService.getRouterState();

        if (state != null) {
            if (state.errors) {
                const error = state.errors[0] as ServerErrorMessage;
                const errorMsg = error.message;
                console.log('ErrorMessage: ', errorMsg);

                this.login = true;
                this.isLoginFailed = true;
                if (containsString(errorMsg, 'token')) {
                    this.errorMessage = errorMsg + '. \nLog in again.';
                } else {
                    this.errorMessage = errorMsg;
                }

                // window.location.reload();
            }

            if (state.message) {
                this.login = true;
                this.isConfirmSuccess = true;
                this.successMessage = state.message;
                console.log('for child ' + this.successMessage);
            }
        }
    }

    // Show/hide form hint
    private toogleHint() {
        this.showHint = !this.showHint;

        setTimeout(() => {
            this.showHint = false;
            console.log(this.showHint);
        }, 60000);
    }

    private navigateToMain() {
        this.navigationService.navigateToMain();
    }

    private navigateToLogin() {
        this.socialUserService.cleanGitAuth();
        this.setLoginLayout();
    }

    private navigateToRegistration() {
        this.socialUserService.cleanGitAuth();
        this.setRegistrationLayout();
    }

    private setLoginLayout() {
        this.switchLayout(true, false, false, false);
    }

    private setRegistrationLayout() {
        this.switchLayout(false, true, false, false);
    }

    private setForgotPassLayout() {
        this.switchLayout(false, false, true, false);
    }

    private setResetPassLayout() {
        this.switchLayout(false, false, false, true);
    }

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

export interface ComponentWithErrorMsg {
    errorMessage: string;
}
