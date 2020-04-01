import {HttpErrorResponse} from '@angular/common/http';
import {Component, Input, OnInit} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {ActivatedRoute, NavigationExtras} from '@angular/router';
import {ResetPasswordRequest} from '../../../../models/payload/ResetPasswordRequest';
import {AuthenticationService} from '../../../../services/account/auth.service';
import {actions} from '../../../../services/navigation/app.endpoints';
import {NavigationService} from '../../../../services/navigation/navigation.service';
import {DeviceInfoStorage} from '../../../../utils/device-fingerprint';
import {isNonEmptyString} from '../../../../utils/string-utils';
import {FormControlService} from '../../../../validation/form-control.service';


@Component({
    selector: 'app-reset-password-form',
    templateUrl: './reset-password.component.html',
    styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {
    private password: string;

    private passConfirm: string;

    private invalidReset = false;

    private resetSuccess = false;

    private fieldTextTypePass: boolean;

    private fieldTextTypePassConfirm: boolean;

    private resetForm: FormGroup;

    @Input() confirmationToken: string;

    private submitted = false;

    private confirmationMessage: string;

    private errorMessage: string;

    constructor(
        private route: ActivatedRoute,
        private navigation: NavigationService,
        private authenticationService: AuthenticationService,
        private formControlService: FormControlService
    ) {
    }

    ngOnInit(): void {
        this.resetForm = new FormGroup({
            password: this.formControlService.getPasswordFormControl(
                this.password
            ),
            passConfirm: this.formControlService.getConfirmPasswordFormControl(
                this.passConfirm
            )
        }, this.formControlService.pwdConfirming('password', 'passConfirm').bind(this));
    }

    // Switching method
    private togglePassTextType() {
        this.fieldTextTypePass = !this.fieldTextTypePass;
    }

    private togglePassConfirmTextType() {
        this.fieldTextTypePassConfirm = !this.fieldTextTypePassConfirm;
    }

    onSubmit(valid: boolean) {
        this.submitted = true;
        if (this.resetForm.hasError('invalid')) {
            this.submitted = false;
        }

        if (
            isNonEmptyString(this.password) &&
            isNonEmptyString(this.passConfirm)
        ) {
            console.log(this.password);
            if (!valid) {
                return;
            }
            this.handlePassReset();
        }
    }

    private handlePassReset() {
        this.authenticationService
            .processResetPasswordRequest(
                new ResetPasswordRequest(this.confirmationToken, this.password),
                DeviceInfoStorage.deviceInfo
            )
            .subscribe(
                (response: any) => {
                    if (response.success) {
                        this.invalidReset = false;
                        this.resetSuccess = true;
                        this.confirmationMessage =
                            'Password successfully reset. You can now log in with the new credentials.';
                        console.log(this.confirmationMessage);
                        alert(this.confirmationMessage);

                        this.navigateToLogin();
                    }
                },
                error => {
                    this.invalidReset = true;
                    this.resetSuccess = false;
                    console.log(error);
                    alert('Reset failed.');
                    console.log('Reset failed.');

                    if (error instanceof HttpErrorResponse) {
                        this.errorMessage = error.error.message
                            ? error.error.message
                            : error.error;
                    }
                }
            );
    }

    private navigateToLogin() {
        const navigationExtras: NavigationExtras = {
            state: {message: this.confirmationMessage},
            queryParams: {action: actions.login}
        };

        this.navigation.navigateToLoginWithExtras(navigationExtras);
    }

    get pass() {
        return this.resetForm.get('password');
    }

    get passConf() {
        return this.resetForm.get('passConfirm');
    }
}
