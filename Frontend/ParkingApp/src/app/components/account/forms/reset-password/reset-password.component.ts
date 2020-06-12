import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { ActivatedRoute, NavigationExtras } from '@angular/router';
import { ComponentWithErrorMsg } from '@app/components/account/forms/account-form/account-form.component';
import { ResetPasswordRequest } from '@app/models/payload/reset-pass-request.payload';
import { AuthenticationService } from '@app/services/account/auth.service';
import {
    FormControlService,
    togglePassConfirmTextType,
    togglePassTextType
} from '@app/services/account/form-control.service';
import { handleHttpErrorResponse } from '@app/services/helpers/global-http-interceptor-service.service';
import { actions } from '@app/services/navigation/app.endpoints';
import { NavigationService } from '@app/services/navigation/navigation.service';
import { DeviceInfoStorage } from '@app/utils/device-fingerprint';
import { isNonEmptyStrings } from '@app/utils/string-utils';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';


/**
 * Reset password form
 */
@Component({
    selector: 'app-reset-password-form',
    templateUrl: './reset-password.component.html',
    styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent
    implements OnInit, OnDestroy, ComponentWithErrorMsg {

    @Input() confirmationToken: string;

    errorMessage: string;

    private password: string;

    private passConfirm: string;

    private invalidReset = false;

    private resetSuccess = false;

    private resetPasswordServerReqSubscription: Subscription;

    // Switching method
    private togglePassVisible = togglePassTextType;

    private togglePassConfirmVisible = togglePassConfirmTextType;

    private fieldTextTypePass: boolean;

    private fieldTextTypePassConfirm: boolean;

    private resetForm: FormGroup;

    private submitted = false;

    private confirmationMessage: string;

    constructor(
        private route: ActivatedRoute,
        private navigation: NavigationService,
        private authenticationService: AuthenticationService,
        private formControlService: FormControlService,
        private translate: TranslateService
    ) {
    }

    get pass() {
        return this.resetForm.get('password');
    }

    get passConf() {
        return this.resetForm.get('passConfirm');
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
        if (
            this.resetPasswordServerReqSubscription &&
            !this.resetPasswordServerReqSubscription.closed
        ) {
            this.resetPasswordServerReqSubscription.unsubscribe();
        }
    }

    /**
     * Submit form handler
     * @param valid - form validation state
     */
    onSubmit(valid: boolean) {
        this.submitted = true;
        if (this.resetForm.hasError('invalid')) {
            this.submitted = false;
        }

        if (isNonEmptyStrings(this.password, this.passConfirm)) {
            if (!valid) {
                return;
            }
            this.handlePassReset();
        }
    }

    /**
     * Initiate reset password form group with validation
     */
    private createForm() {
        this.resetForm = new FormGroup(
            {
                password: this.formControlService.getPasswordFormControl(this.password),
                passConfirm: this.formControlService.getConfirmPasswordFormControl(this.passConfirm)
            },
            this.formControlService
                .checkPasswordConfirm('password', 'passConfirm')
                .bind(this)
        );
    }

    /**
     * Handle user reset password request
     */
    private handlePassReset() {
        this.resetPasswordServerReqSubscription = this.authenticationService
            .processResetPasswordRequest(
                new ResetPasswordRequest(this.confirmationToken, this.password),
                DeviceInfoStorage.deviceInfo
            )
            .subscribe(
                this.handleResetPasswordResponse(),
                this.handleResetPasswordError()
            );
    }

    /**
     * Handle reset password server response
     */
    private handleResetPasswordResponse() {
        return (response: any) => {
            if (response.success) {
                this.invalidReset = false;
                this.resetSuccess = true;
                this.confirmationMessage = this.translate.instant('reset-password.success-msg');
                // console.log(this.confirmationMessage);
                alert(this.confirmationMessage);

                this.navigateToLogin();
            }
        };
    }

    /**
     * Handle server response error
     */
    private handleResetPasswordError() {
        return (error) => {
            this.invalidReset = true;
            this.resetSuccess = false;
            alert(this.translate.instant('reset-password.err-msg'));

            handleHttpErrorResponse(error, this);
        };
    }

    /**
     * Navigate to login component layout after successful password reset
     */
    private navigateToLogin() {
        const navigationExtras: NavigationExtras = {
            state: { message: this.confirmationMessage },
            queryParams: { action: actions.login }
        };

        this.navigation.navigateToLoginWithExtras(navigationExtras);
    }
}
