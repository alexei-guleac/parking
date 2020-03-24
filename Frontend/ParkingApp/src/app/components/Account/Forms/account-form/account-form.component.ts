import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ServerErrorMessage} from '../../../../models/ServerErrorMessage';
import {actions} from '../../../../services/navigation/app.endpoints';
import {NavigationService} from '../../../../services/navigation/navigation.service';
import {_getFingerprint} from '../../../../utils/device-fingerprint';
import {containsString} from '../../../../utils/string-utils';
import {fadeInOut} from '../../../animations/animations';


@Component({
    selector: 'app-account-form',
    animations: [fadeInOut],
    templateUrl: './account-form.component.html',
    styleUrls: ['./account-form.component.css']
})
export class AccountFormComponent implements OnInit {

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

    constructor(private router: Router,
                private route: ActivatedRoute,
                private navigation: NavigationService) {
        this.handleRedirect();
        this.subscribeUrlParams();
    }

    ngOnInit() {
        _getFingerprint();
    }

    private subscribeUrlParams() {
        console.log('Called Constructor');
        this.route.queryParams.subscribe(params => {
            if (params.action === actions.reset) {
                this.confirmationToken = params.confirmation_token;
                this.navigateToResetPass();
            }
            if (params.action === actions.login) {
                this.navigateToLogin();
            }
        });
    }

    // Show error message from global error interceptor
    // or registration confirmation service
    private handleRedirect() {

        const navigation = this.router.getCurrentNavigation();
        const state = navigation.extras.state as any;

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
        this.navigation.navigateToMain();
    }

    private navigateToLogin() {
        this.switchLayout(true, false, false, false);
    }

    private navigateToRegistration() {
        this.switchLayout(false, true, false, false);
    }

    private navigateToForgotPass() {
        this.switchLayout(false, false, true, false);
    }

    private navigateToResetPass() {
        this.switchLayout(false, false, false, true);
    }

    private switchLayout(stateLogin: boolean, stateRegister: boolean, stateForgotPassword: boolean, stateResetPassword: boolean) {
        this.login = stateLogin;
        this.register = stateRegister;
        this.forgotPass = stateForgotPassword;
        this.resetPass = stateResetPassword;
    }
}
