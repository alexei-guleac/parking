import {Component, EventEmitter, OnDestroy, OnInit, Output,} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {ComponentWithErrorMsg} from '@app/components/account/forms/account-form/account-form.component';
import {AuthenticationService} from '@app/services/account/auth.service';
import {FormControlService} from '@app/services/account/form-control.service';
import {handleHttpErrorResponse} from '@app/services/helpers/global-http-interceptor-service.service';
import {NavigationService} from '@app/services/navigation/navigation.service';
import {DeviceInfoStorage} from '@app/utils/device-fingerprint';
import {isNonEmptyString} from '@app/utils/string-utils';
import {Subscription} from 'rxjs';


@Component({
    selector: 'app-forgot-pass-form',
    templateUrl: './forgot-pass-form.component.html',
    styleUrls: ['./forgot-pass-form.component.scss'],
})
export class ForgotPassFormComponent
    implements OnInit, OnDestroy, ComponentWithErrorMsg {
    errorMessage: string;

    @Output()
    userLoginEvent: EventEmitter<any> = new EventEmitter<any>();

    sendSuccess = false;

    private forgotPassSubscription: Subscription;

    private email: string;

    private forgotForm: FormGroup;

    private requestSuccess: boolean;

    private requestFailed: boolean;

    private confirmationMessage: string;

    private submitted = false;

    constructor(
        private authenticationService: AuthenticationService,
        private navigation: NavigationService,
        private formControlService: FormControlService
    ) {
    }

    get mail() {
        return this.forgotForm.get('email');
    }

    ngOnInit(): void {
        this.createForm();
    }

    ngOnDestroy(): void {
        if (
            this.forgotPassSubscription &&
            !this.forgotPassSubscription.closed
        ) {
            this.forgotPassSubscription.unsubscribe();
        }
    }

    onSubmit(valid: boolean) {
        this.submitted = true;
        if (this.forgotForm.hasError('invalid')) {
            this.submitted = false;
            return;
        }

        if (isNonEmptyString(this.email)) {
            /*console.log(this.email);
            console.log('TEST' + this.forgotForm.hasError('invalid'));
            console.log('VALID' + valid);*/
            if (!valid) {
                return;
            }
            this.handleForgotPass();
        }
    }

    private createForm() {
        this.forgotForm = new FormGroup({
            email: this.formControlService.getEmailFormControl(''),
        });
    }

    private handleForgotPass() {
        this.forgotPassSubscription = this.authenticationService
            .processForgotPasswordRequest(
                this.email,
                DeviceInfoStorage.deviceInfo
            )
            .subscribe(this.handleForgotPasswordResponse(), this.handleError());
    }

    private handleForgotPasswordResponse() {
        return (response: any) => {
            if (response.success) {
                this.requestSuccess = true;
                this.disableButtonAfterDelay();
                this.requestFailed = false;
                this.confirmationMessage =
                    'Request to reset password received. ' +
                    'Check your inbox for the reset link. ' +
                    'You will be redirected to the main page in 5 seconds.';
                console.log(this.confirmationMessage);
                this.redirectToMainAfterDelay();
            }
        };
    }

    private handleError() {
        return (error) => {
            this.requestSuccess = false;
            this.requestFailed = true;

            handleHttpErrorResponse(error, this);
        };
    }

    private disableButtonAfterDelay() {
        setTimeout(() => {
            this.sendSuccess = true;
        }, 1050);
    }

    private redirectToMainAfterDelay() {
        setTimeout(() => {
            this.navigation.navigateToMain();
        }, 5000);
    }
}
