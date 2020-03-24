import {DOCUMENT} from '@angular/common';
import {HttpErrorResponse} from '@angular/common/http';
import {Component, EventEmitter, Inject, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {NgxUiLoaderService} from 'ngx-ui-loader';
import {User} from '../../../../models/User';
import {AuthenticationService} from '../../../../services/account/auth.service';
import {capitalize, isNonEmptyString} from '../../../../utils/string-utils';
import {RegularExpressions} from '../../../../validation/reg-exp-patterns';
import {regexpTestValidator} from '../../../../validation/regexp-name-validator';


@Component({
    selector: 'app-reg-form',
    templateUrl: './registration-form.component.html',
    styleUrls: ['./registration-form.component.css']
})
export class RegFormComponent implements OnInit {

    private username: string;

    private email: string;

    private firstname: string;

    private lastname: string;

    private password: string;

    private passConfirm: string;

    private errorMessage = 'Invalid Credentials';

    private successMessage: string;

    private invalidReg = false;

    private regSuccess = false;

    private fieldTextTypePass: boolean;

    private fieldTextTypePassConfirm: boolean;

    @Output()
    userLoginEvent: EventEmitter<any> = new EventEmitter<any>();

    private regForm: FormGroup;

    private submitted = false;

    private acceptTerms = false;

    private grecaptcha: any;

    constructor(
        @Inject(DOCUMENT) private document: any,
        private route: ActivatedRoute,
        private router: Router,
        private authenticationService: AuthenticationService,
        private ngxService: NgxUiLoaderService) {

        this.grecaptcha = this.document.grecaptcha;
    }

    // Switching method
    private togglePassTextType() {
        this.fieldTextTypePass = !this.fieldTextTypePass;
    }

    private togglePassConfirmTextType() {
        this.fieldTextTypePassConfirm = !this.fieldTextTypePassConfirm;
    }

    ngOnInit(): void {
        this.regForm = new FormGroup({
            username: new FormControl(this.username, [
                Validators.required,
                Validators.minLength(5),
                Validators.maxLength(15),
                // not working
                // Validators.pattern(RegularExpressions.usernamePattern),

                regexpTestValidator(RegularExpressions.usernamePattern)
            ]),

            email: new FormControl(this.email, [
                Validators.required,
                Validators.minLength(8),
                Validators.maxLength(35),
                Validators.email,

                Validators.pattern(RegularExpressions.emailPattern)
                // regexpTestNameValidator(RegularExpressions.emailPatternStr)
            ]),

            firstname: new FormControl(this.firstname, [
                Validators.required,
                Validators.minLength(3),
                Validators.maxLength(15),
                // Validators.pattern(RegularExpressions.namePatternStr),

                regexpTestValidator(RegularExpressions.namePattern)
            ]),

            lastname: new FormControl(this.lastname, [
                Validators.required,
                Validators.minLength(3),
                Validators.maxLength(15),
                // Validators.pattern(RegularExpressions.namePatternStr),

                regexpTestValidator(RegularExpressions.namePattern)
            ]),

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
            ]),
            acceptTerms: new FormControl(),
            captcha: new FormControl()
        });
    }

    onSubmit() {
        this.submitted = true;
        if (this.regForm.hasError('invalid')) {
            this.submitted = false;
        }

        const username = this.username;
        const email = this.email;
        const lastname = capitalize(this.lastname);
        const fullname = capitalize(this.firstname) + ' ' + lastname;
        const pass = this.password;

        if (isNonEmptyString(username) && isNonEmptyString(pass)) {
            console.log(username + '  ' + pass);
            const newUser = new User(
                null,
                username,
                email,
                pass,
                fullname,
                lastname,
            );
            console.log(newUser + '  ');
            this.handleRegistration(newUser);
        }
    }

    private handleRegistration(user: User) {

        this.ngxService.startLoader('loader-01'); // start foreground spinner of the loader "loader-01" with 'default' taskId
        // Stop the foreground loading after 5s
        setTimeout(() => {
            this.ngxService.stopLoader('loader-01'); // stop foreground spinner of the loader "loader-01" with 'default' taskId
        }, 5000);

        this.authenticationService.processRegistration(user).subscribe(
            (response: any) => {

                if (response.success) {
                    this.invalidReg = false;
                    this.regSuccess = true;
                    this.successMessage = 'Registration Successful.';
                    console.log(this.successMessage);
                    alert(this.successMessage);

                    // this.navigateToLogin();
                } else {
                    this.invalidReg = true;
                    this.regSuccess = false;
                    console.log(this.successMessage);
                }

            }, error => {
                this.invalidReg = true;
                this.regSuccess = false;
                console.log(error);
                alert('Registration failed.');
                console.log('Registration failed.');

                if (error instanceof HttpErrorResponse) {
                    this.errorMessage = error.error.message ? error.error.message : error.error;
                }
            });
    }

    get name() {
        return this.regForm.get('username');
    }

    get mail() {
        return this.regForm.get('email');
    }

    get fname() {
        return this.regForm.get('firstname');
    }

    get lname() {
        return this.regForm.get('lastname');
    }

    get pass() {
        return this.regForm.get('password');
    }

    get passConf() {
        return this.regForm.get('passConfirm');
    }

    navigateToLogin() {
        // this.router.navigate([routes.account], {queryParams: {action: actions.login}});
        this.userLoginEvent.emit();
    }
}
