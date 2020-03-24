import {HttpErrorResponse} from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, NavigationExtras, Router} from '@angular/router';
import {AuthenticationService} from '../../../services/account/auth.service';
import {actions, api} from '../../../services/navigation/app.endpoints';
import {NavigationService} from '../../../services/navigation/navigation.service';
import {containsString} from '../../../utils/string-utils';


@Component({
    selector: "app-confirm-registration",
    templateUrl: "./confirm-user.component.html",
    styleUrls: ["./confirm-user.component.css"]
})
export class ConfirmUserComponent implements OnInit {
    private confirmationToken: string;

    private confirmationSuccess: boolean;

    private confirmationMessage: string;

    private errorMessage: string;

    private urlPath: string;

    private formAction = actions.login;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private navigation: NavigationService,
        private authenticationService: AuthenticationService
    ) {
        this.processUrlPath();
        this.subscribeUrlParams();
        this.verifyConfirmation();
    }

    ngOnInit() {
    }

    private subscribeUrlParams() {
        console.log("Called Constructor");
        this.route.queryParams.subscribe(params => {
            this.confirmationToken = params.confirmation_token;
        });
        console.log(this.confirmationToken);
    }

    private processUrlPath() {
        this.urlPath = this.router.url;
        console.log(this.router.url);

        if (containsString(this.urlPath, api.confirmReset)) {
            console.log("dfdfdfdffffffffZZZZZZZZZZ");
            this.formAction = actions.reset;
        }
    }

    private verifyConfirmation() {
        this.authenticationService
            .processUserConfirmation(this.confirmationToken)
            .subscribe(
                (response: any) => {
                    if (response.success) {
                        this.confirmationSuccess = true;
                        this.confirmationMessage = "Confirmation Successful.";
                        console.log(this.confirmationMessage);

                        this.navigateToAccountForm(this.formAction);
                    } else {
                        this.confirmationSuccess = false;
                        this.confirmationMessage =
                            "Confirmation failed. Submit again";
                    }
                },
                error => {
                    /*console.log('log ');
                console.log(error);*/

                    if (error instanceof HttpErrorResponse) {
                        this.errorMessage = error.error.message
                            ? error.error.message
                            : error.error;
                    }
                }
            );
    }

    private navigateToAccountForm(action: string) {
        const navigationExtras: NavigationExtras = {
            state: {message: this.confirmationMessage},
            queryParams: {action, confirmation_token: this.confirmationToken}
        };

        setTimeout(() => {
            if (this.formAction === actions.login) {
                this.navigation.navigateToLoginWithExtras(navigationExtras);
            }
            if (this.formAction === actions.reset) {
                this.navigation.navigateToResetWithExtras(navigationExtras);
            }
        }, 7000);
    }
}
