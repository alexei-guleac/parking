import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute, NavigationExtras} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';
import {AuthenticationService} from '../../../../services/account/auth.service';
import {NavigationService} from '../../../../services/navigation/navigation.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {RegularExpressions} from '../../../../validation/reg-exp-patterns';
import {isNonEmptyString} from '../../../../utils/string-utils';
import {actions} from "../../../../services/navigation/app.endpoints";


@Component({
    selector: 'app-reset-password',
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

    @Input() username: string;

    private submitted = false;

    private confirmationMessage: string;

    private errorMessage: string;

    constructor(private route: ActivatedRoute,
                private navigation: NavigationService,
                private authenticationService: AuthenticationService) {
    }

    ngOnInit(): void {
        this.resetForm = new FormGroup({
            password: new FormControl(this.password, [
                Validators.required,
                Validators.minLength(6),
                Validators.maxLength(10),

                Validators.pattern(RegularExpressions.passwordPatternStr)
            ]),

            passConfirm: new FormControl(this.passConfirm, [
                Validators.required,
                Validators.minLength(6),
                Validators.maxLength(10),
            ])
        });
    }


    // Switching method
    togglePassTextType() {
        this.fieldTextTypePass = !this.fieldTextTypePass;
    }

    togglePassConfirmTextType() {
        this.fieldTextTypePassConfirm = !this.fieldTextTypePassConfirm;
    }

    onSubmit() {
        this.submitted = true;
        if (this.resetForm.hasError('invalid')) {
            this.submitted = false;
        }

        if (isNonEmptyString(this.password) && isNonEmptyString(this.passConfirm)) {
            console.log(this.password);

            this.handlePassReset();
        }
    }

    handlePassReset() {
        this.authenticationService.processResetPasswordRequest(this.username, this.password).subscribe(
            (response: any) => {

                if (response.success) {
                    this.invalidReset = false;
                    this.resetSuccess = true;
                    this.confirmationMessage = 'Password successfully reset. You can now log in with the new credentials.';
                    console.log(this.confirmationMessage);
                    alert(this.confirmationMessage);

                    this.navigateToLogin();
                }
            }, error => {
                this.invalidReset = true;
                this.resetSuccess = false;
                console.log(error);
                alert('Reset failed.');
                console.log('Reset failed.');

                if (error instanceof HttpErrorResponse) {
                    this.errorMessage = error.error.message ? error.error.message : error.error;
                }
            });
    }

    navigateToLogin() {
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
