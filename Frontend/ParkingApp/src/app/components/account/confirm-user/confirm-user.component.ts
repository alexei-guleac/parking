import { Component } from '@angular/core';
import { NavigationExtras } from '@angular/router';
import { ComponentWithErrorMsg } from '@app/components/account/forms/account-form/account-form.component';
import { AuthenticationService } from '@app/services/account/auth.service';
import { handleErrorResponse } from '@app/services/helpers/global-http-interceptor-service.service';
import { actions, api } from '@app/services/navigation/app.endpoints';
import { NavigationService } from '@app/services/navigation/navigation.service';
import { TranslateService } from '@ngx-translate/core';


/**
 * Component for handling user confirmation redirection from email
 */
@Component({
    selector: 'app-confirm-registration',
    templateUrl: './confirm-user.component.html',
    styleUrls: ['./confirm-user.component.scss']
})
export class ConfirmUserComponent implements ComponentWithErrorMsg {

    errorMessage: string;

    private confirmationToken: string;

    private confirmationSuccess: boolean;

    private confirmationMessage: string;

    private countdownCounter = 11;

    private formAction = actions.login;

    private formActions = actions;

    constructor(
        private navigationService: NavigationService,
        private authenticationService: AuthenticationService,
        private translate: TranslateService
    ) {
        this.processUrlPath();
        this.subscribeUrlParams();
        this.verifyConfirmation();
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
            // if confirmation token come, save it for later account confirmation processing
            if (params.confirmation_token != null) {
                this.confirmationToken = params.confirmation_token;
            }
        };
    }

    /**
     * Set from type in order to display the corresponding user form component
     * (login by default, reset form by after confirmation)
     */
    private processUrlPath() {
        if (this.navigationService.assertUrlPath(api.confirmReset)) {
            this.formAction = actions.reset;
        }
    }

    /**
     * Initiate account confirmation process and server response handle result
     */
    private verifyConfirmation() {
        this.authenticationService
            .processUserConfirmation(this.confirmationToken)
            .subscribe(
                this.handleConfirmationResponse(),
                handleErrorResponse(this, this.navigateToMain)
            );
    }

    /**
     * Callback function handler for account confirmation server response
     */
    private handleConfirmationResponse() {
        return (response: any) => {
            // if response contains success field
            if (response.success) {
                this.confirmationSuccess = true;
                this.confirmationMessage = this.translate.instant('confirm-user.success-msg');
                // navigate to corresponding user form (login or reset password)
                this.navigateToAccountForm(this.formAction);
            } else {
                this.confirmationSuccess = false;
                this.confirmationMessage = this.translate.instant('confirm-user.err-msg');
                this.navigateToMain();
            }
        };
    }

    /**
     * Switch to specified account form component with message from server and confirmation token
     * after 7 seconds countdown
     *
     * @param action - corresponding user form (login or reset password)
     */
    private navigateToAccountForm(action: string) {
        const navigationExtras: NavigationExtras = {
            state: { message: this.confirmationMessage },
            queryParams: { action, confirmation_token: this.confirmationToken }
        };

        const interval = setInterval(() => {
            this.countdownCounter--;
        }, 1000);

        setTimeout(() => {
            clearInterval(interval);
            this.countdownCounter = 7;

            if (this.formAction === actions.login) {
                this.navigationService.navigateToLoginWithExtras(
                    navigationExtras
                );
            }
            if (this.formAction === actions.reset) {
                this.navigationService.navigateToResetWithExtras(
                    navigationExtras
                );
            }
        }, 7000);
    }

    /**
     * Navigate application to main page after 10 seconds countdown
     */
    private navigateToMain = () => {
        const interval = setInterval(() => {
            this.countdownCounter--;
        }, 1000);

        setTimeout(() => {
            clearInterval(interval);
            this.countdownCounter = 10;

            this.navigationService.navigateToMain();
        }, 10000);
    };
}
