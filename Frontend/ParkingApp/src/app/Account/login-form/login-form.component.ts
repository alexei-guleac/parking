import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthenticationService} from '../auth.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {regexpTestValidator} from '../validation/regexp-name-validator';
import {isNonEmptyString} from '../validation/string-utils';
import {RegularExpressions} from '../validation/reg-exp-patterns';
import {credentials} from '../credentials';

@Component({
    selector: 'app-login-form',
    templateUrl: './login-form.component.html',
    styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent implements OnInit {

    username: string;
    password: string;
    errorMessage = 'Invalid Credentials';
    successMessage: string;

    invalidLogin = false;
    loginSuccess = false;

    @Output()
    userLoginEvent = new EventEmitter();

    private loginForm: FormGroup;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private authenticationService: AuthenticationService) {
    }

    ngOnInit(): void {
        this.loginForm = new FormGroup({
            username: new FormControl(this.username, [
                Validators.required,
                Validators.minLength(5),
                Validators.maxLength(15),

                // <-- Here's how you pass in the custom validator.
                regexpTestValidator(RegularExpressions.usernamePattern)
            ]),

            password: new FormControl(this.password, [
                Validators.required,
                Validators.minLength(6),
                Validators.maxLength(10),

                Validators.pattern(RegularExpressions.passwordPatternStr)
            ])
        });
    }

    get name() {
        return this.loginForm.get('username');
    }

    get pass() {
        return this.loginForm.get('password');
    }

    onSubmit() {
        if (isNonEmptyString(this.username) && isNonEmptyString(this.password)) {
            console.log(this.username + '  ' + this.password);
            // for local login test
            // this.localLoginTest();
            this.handleLogin();
        }
    }

    handleLogin() {
        this.authenticationService.authenticationServiceLogin(this.username, this.password).subscribe(
            response => {

                if (response) {
                    this.invalidLogin = false;
                    this.loginSuccess = true;
                    this.successMessage = 'Login Successful.';
                    console.log(this.successMessage);

                    this.authenticationService.username = this.username;
                    this.authenticationService.password = this.password;
                    this.authenticationService.registerSuccessfulLogin(this.username);

                    // console.log(sessionStorage.getItem('authenticatedUser'));
                    // console.log(sessionStorage.getItem('token'));

                    this.router.navigate(['']);
                } else {
                    this.invalidLogin = true;
                    this.loginSuccess = false;
                    this.successMessage = 'Login failed.';
                    console.log(this.successMessage);
                }

            }, error => {
                console.log(error);
                alert('Authentication failed.');
                console.log('Authentication failed.');
            });
    }

    private localLoginTest() {
        // only for testing purpose
        if ((this.username === credentials.admin && this.password === credentials.adminPassword)
            || (this.username === credentials.user && this.password === credentials.userPassword)
        ) {
            // credentials error handle
            this.invalidLogin = false;
            this.loginSuccess = true;

            this.authenticationService.registerSuccessfulLogin(this.username);

            console.log(sessionStorage.getItem('authenticatedUser'));

            this.router.navigate(['']);
            this.userLoginEvent.emit();
        } else {
            this.invalidLogin = true;
            this.loginSuccess = false;
        }
    }

    navigateToRegistration() {
        this.router.navigate(['registration']);
    }
}
