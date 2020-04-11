import {Component, OnInit} from '@angular/core';
import {NavigationExtras} from '@angular/router';
import {AuthenticationService} from '@app/services/account/auth.service';
import {handleErrorResponse} from '@app/services/helpers/global-http-interceptor-service.service';
import {actions, api} from '@app/services/navigation/app.endpoints';
import {NavigationService} from '@app/services/navigation/navigation.service';


@Component({
    selector: 'app-confirm-registration',
    templateUrl: './confirm-user.component.html',
    styleUrls: ['./confirm-user.component.scss'],
})
export class ConfirmUserComponent implements OnInit {
    constructor(
        private navigationService: NavigationService,
        private authenticationService: AuthenticationService
    ) {
        this.processUrlPath();
        this.subscribeUrlParams();
        this.verifyConfirmation();
    }

    private confirmationToken: string;

    private confirmationSuccess: boolean;

    private confirmationMessage: string;

    private errorMessage: string;

    counter = 11;

    private formAction = actions.login;

    private formActions = actions;

    ngOnInit() {
    }

    private subscribeUrlParams() {
        this.navigationService.subscribeUrlParams(
            this.getQueryParamsCallback()
        );
    }

    private getQueryParamsCallback() {
        return (params) => {
            if (params.confirmation_token != null) {
                this.confirmationToken = params.confirmation_token;
                // console.log(this.confirmationToken);
            }
        };
    }

    private processUrlPath() {
        if (this.navigationService.assertUrlPath(api.confirmReset)) {
            this.formAction = actions.reset;
        }
    }

    private verifyConfirmation() {
        this.authenticationService
            .processUserConfirmation(this.confirmationToken)
            .subscribe(
                this.handleConfirmationResponse(),
                handleErrorResponse(this, this.navigateToMain)
            );
    }

    private handleConfirmationResponse() {
        return (response: any) => {
            if (response.success) {
                this.confirmationSuccess = true;
                this.confirmationMessage = 'Confirmation Successful.';
                /*console.log(this.confirmationMessage);
                console.log('formAction' + this.formAction);*/
                this.navigateToAccountForm(this.formAction);
            } else {
                this.confirmationSuccess = false;
                this.confirmationMessage = 'Confirmation failed. Submit again';
                this.navigateToMain();
            }
        };
    }

    private navigateToAccountForm(action: string) {
        const navigationExtras: NavigationExtras = {
            state: {message: this.confirmationMessage},
            queryParams: {action, confirmation_token: this.confirmationToken},
        };

        const interval = setInterval(() => {
            this.counter--;
        }, 1000);

        setTimeout(() => {
            clearInterval(interval);
            this.counter = 7;

            // console.log('formAction2' + this.formAction);
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

    private navigateToMain = () => {
        const interval = setInterval(() => {
            this.counter--;
        }, 1000);

        setTimeout(() => {
            clearInterval(interval);
            this.counter = 10;

            this.navigationService.navigateToMain();
        }, 10000);
    };
}
