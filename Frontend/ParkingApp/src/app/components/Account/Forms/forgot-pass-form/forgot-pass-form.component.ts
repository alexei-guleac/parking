import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {RegularExpressions} from '../../../../validation/reg-exp-patterns';
import {isNonEmptyString} from '../../../../utils/string-utils';
import {AuthenticationService} from '../../../../services/account/auth.service';
import {HttpErrorResponse} from '@angular/common/http';
import {NavigationService} from '../../../../services/navigation/navigation.service';


@Component({
    selector: 'app-forgot-pass-form',
    templateUrl: './forgot-pass-form.component.html',
    styleUrls: ['./forgot-pass-form.component.css']
})
export class ForgotPassFormComponent implements OnInit {

    private email: string;

    private forgotForm: FormGroup;

    private requestSuccess: boolean;
    private requestFailed: boolean;

    private confirmationMessage: string;
    private errorMessage: string;

    private submitted = false;

    @Output()
    userLoginEvent: EventEmitter<any> = new EventEmitter<any>();
    sendSuccess = false;

    constructor(private authenticationService: AuthenticationService,
                private navigation: NavigationService) {
    }

    get mail() {
        return this.forgotForm.get('email');
    }

    ngOnInit(): void {
        this.forgotForm = new FormGroup({

            email: new FormControl(this.email, [
                Validators.required,
                Validators.minLength(8),
                Validators.maxLength(35),
                Validators.email,

                Validators.pattern(RegularExpressions.emailPattern)
            ]),

        });
    }

    onSubmit() {
        this.submitted = true;
        if (this.forgotForm.hasError('invalid')) {
            this.submitted = false;
        }

        if (isNonEmptyString(this.email)) {
            console.log(this.email);
            this.handleForgotPass();
        }
    }

    handleForgotPass() {
        this.authenticationService.processForgotPasswordRequest(this.email).subscribe(
            (response: any) => {

                if (response.success) {
                    this.requestSuccess = true;
                    this.disableAfterDelay();
                    this.requestFailed = false;
                    this.confirmationMessage = 'Request to reset password received. ' +
                        'Check your inbox for the reset link. ' +
                        'You will be redirected to the main page in 5 seconds.';
                    console.log(this.confirmationMessage);
                    this.redirectToMainAfterDelay();
                }
            }, error => {
                /*console.log('log ');
                console.log(error);*/
                this.requestSuccess = false;
                this.requestFailed = true;

                if (error instanceof HttpErrorResponse) {
                    this.errorMessage = error.error.message ? error.error.message : error.error;
                }
            });
    }

    private disableAfterDelay() {
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
