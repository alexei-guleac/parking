import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {ActivatedRoute, NavigationExtras} from '@angular/router';
import {ComponentWithErrorMsg} from '@app/components/account/forms/account-form/account-form.component';
import {ResetPasswordRequest} from '@app/models/payload/ResetPasswordRequest';
import {AuthenticationService} from '@app/services/account/auth.service';
import {
    FormControlService,
    togglePassConfirmTextType,
    togglePassTextType,
} from '@app/services/account/form-control.service';
import {handleHttpErrorResponse} from '@app/services/helpers/global-http-interceptor-service.service';
import {actions} from '@app/services/navigation/app.endpoints';
import {NavigationService} from '@app/services/navigation/navigation.service';
import {DeviceInfoStorage} from '@app/utils/device-fingerprint';
import {isNonEmptyStrings} from '@app/utils/string-utils';
import {Subscription} from 'rxjs';


@Component({
    selector: 'app-reset-password-form',
    templateUrl: './reset-password.component.html',
    styleUrls: ['./reset-password.component.scss'],
})
export class ResetPasswordComponent
    implements OnInit, OnDestroy, ComponentWithErrorMsg {
    @Input() confirmationToken: string;

    errorMessage: string;

    private resetPasswordSubscription: Subscription;

    // Switching method
    private togglePassVisible = togglePassTextType;

    private togglePassConfirmVisible = togglePassConfirmTextType;

    private password: string;

    private passConfirm: string;

    private invalidReset = false;

    private resetSuccess = false;

    private fieldTextTypePass: boolean;

    private fieldTextTypePassConfirm: boolean;

    private resetForm: FormGroup;

    private submitted = false;

    private confirmationMessage: string;

    constructor(
        private route: ActivatedRoute,
        private navigation: NavigationService,
        private authenticationService: AuthenticationService,
        private formControlService: FormControlService
    ) {
    }

    get pass() {
        return this.resetForm.get('password');
    }

    get passConf() {
        return this.resetForm.get('passConfirm');
    }

    ngOnInit(): void {
        this.createForm();
    }

    ngOnDestroy(): void {
        if (
            this.resetPasswordSubscription &&
            !this.resetPasswordSubscription.closed
        ) {
            this.resetPasswordSubscription.unsubscribe();
        }
    }

    onSubmit(valid: boolean) {
        this.submitted = true;
        if (this.resetForm.hasError('invalid')) {
            this.submitted = false;
        }

        if (isNonEmptyStrings(this.password, this.passConfirm)) {
            // console.log(this.password);
            if (!valid) {
                return;
            }
            this.handlePassReset();
        }
    }

    private createForm() {
        this.resetForm = new FormGroup(
            {
                password: this.formControlService.getPasswordFormControl(
                    this.password
                ),
                passConfirm: this.formControlService.getConfirmPasswordFormControl(
                    this.passConfirm
                ),
            },
            this.formControlService
                .checkPasswordConfirm('password', 'passConfirm')
                .bind(this)
        );
    }

    private handlePassReset() {
        this.resetPasswordSubscription = this.authenticationService
            .processResetPasswordRequest(
                new ResetPasswordRequest(this.confirmationToken, this.password),
                DeviceInfoStorage.deviceInfo
            )
            .subscribe(
                this.handleResetPasswordResponse(),
                this.handleResetPasswordError()
            );
    }

    private handleResetPasswordResponse() {
        return (response: any) => {
            if (response.success) {
                this.invalidReset = false;
                this.resetSuccess = true;
                this.confirmationMessage =
                    'Password successfully reset. You can now log in with the new credentials.';
                console.log(this.confirmationMessage);
                alert(this.confirmationMessage);

                this.navigateToLogin();
            }
        };
    }

    private handleResetPasswordError() {
        return (error) => {
            this.invalidReset = true;
            this.resetSuccess = false;
            // console.log(error);
            alert('Reset failed.');
            // console.log('Reset failed.');

            handleHttpErrorResponse(error, this);
        };
    }

    private navigateToLogin() {
        const navigationExtras: NavigationExtras = {
            state: {message: this.confirmationMessage},
            queryParams: {action: actions.login},
        };

        this.navigation.navigateToLoginWithExtras(navigationExtras);
    }
}
