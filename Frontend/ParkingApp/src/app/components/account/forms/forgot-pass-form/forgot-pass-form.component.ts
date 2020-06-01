import { Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { ComponentWithErrorMsg } from '@app/components/account/forms/account-form/account-form.component';
import { AuthenticationService } from '@app/services/account/auth.service';
import { FormControlService } from '@app/services/account/form-control.service';
import { handleHttpErrorResponse } from '@app/services/helpers/global-http-interceptor-service.service';
import { NavigationService } from '@app/services/navigation/navigation.service';
import { DeviceInfoStorage } from '@app/utils/device-fingerprint';
import { isNonEmptyString } from '@app/utils/string-utils';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';


/**
 * Forgot password form component
 */
@Component({
    selector: 'app-forgot-pass-form',
    templateUrl: './forgot-pass-form.component.html',
    styleUrls: ['./forgot-pass-form.component.scss']
})
export class ForgotPassFormComponent
    implements OnInit, OnDestroy, ComponentWithErrorMsg {

    errorMessage: string;

    @Output()
    userLoginEvent: EventEmitter<any> = new EventEmitter<any>();

    sendSuccess = false;

    private forgotPassServerReqSubscription: Subscription;

    private email: string;

    private forgotForm: FormGroup;

    private requestSuccess: boolean;

    private requestFailed: boolean;

    private confirmationMessage: string;

    private submitted = false;

    constructor(
        private authenticationService: AuthenticationService,
        private navigation: NavigationService,
        private formControlService: FormControlService,
        private translate: TranslateService
    ) {
    }

    get mail() {
        return this.forgotForm.get('email');
    }

    /**
     * Initialize the directive/component after Angular first displays the data-bound properties
     * and sets the directive/component's input properties.
     * Called once, after the first ngOnChanges()
     */
    ngOnInit(): void {
        this.createForm();
    }

    /**
     * Cleanup just before Angular destroys the directive/component.
     * Unsubscribe Observables and detach event handlers to avoid memory leaks.
     * Called just before Angular destroys the directive/component
     */
    ngOnDestroy(): void {
        // close forgot password server request subscription
        if (
            this.forgotPassServerReqSubscription &&
            !this.forgotPassServerReqSubscription.closed
        ) {
            this.forgotPassServerReqSubscription.unsubscribe();
        }
    }

    /**
     * Submit form handler
     * @param valid - form validation state
     */
    onSubmit(valid: boolean) {
        this.submitted = true;
        if (this.forgotForm.hasError('invalid')) {
            this.submitted = false;
            return;
        }

        if (isNonEmptyString(this.email)) {
            if (!valid) {
                return;
            }
            this.handleForgotPass();
        }
    }

    /**
     * Initiate forgot password form group with validation
     */
    private createForm() {
        this.forgotForm = new FormGroup({
            email: this.formControlService.getEmailFormControl('')
        });
    }

    /**
     * Handle user profile forgot password request
     */
    private handleForgotPass() {
        this.forgotPassServerReqSubscription = this.authenticationService
            .processForgotPasswordRequest(
                this.email,
                DeviceInfoStorage.deviceInfo
            )
            .subscribe(this.handleForgotPasswordResponse(), this.handleError());
    }

    /**
     * Handle forgot password server response
     */
    private handleForgotPasswordResponse() {
        return (response: any) => {
            if (response.success) {
                this.requestSuccess = true;
                this.disableButtonAfterDelay();
                this.requestFailed = false;
                this.confirmationMessage = this.translate.instant('forgot-pass-form.success-msg');
                this.redirectToMainAfterDelay();
            }
        };
    }

    /**
     * Handle server response error
     */
    private handleError() {
        return (error) => {
            this.requestSuccess = false;
            this.requestFailed = true;

            handleHttpErrorResponse(error, this);
        };
    }

    /**
     * Disable submit button due to prevention of abuse
     */
    private disableButtonAfterDelay() {
        setTimeout(() => {
            this.sendSuccess = true;
        }, 1050);
    }

    /**
     * navigate to main navigation page after 5 seconds delay
     */
    private redirectToMainAfterDelay() {
        setTimeout(() => {
            this.navigation.navigateToMain();
        }, 5000);
    }
}
