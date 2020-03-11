import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthenticationService} from '../../auth.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {regexpTestValidator} from '../validation/regexp-name-validator';
import {isNonEmptyString} from '../validation/string-utils';
import {RegularExpressions} from '../validation/reg-exp-patterns';
import {credentials} from '../../credentials';
import {AuthService, FacebookLoginProvider, SocialUser} from 'angularx-social-login';
import {environment} from '../../../../environments/environment';
import {SocialAuthService} from '../../social-auth.service';


@Component({
    selector: 'app-login-form',
    templateUrl: './login-form.component.html',
    styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent implements OnInit {

    invalidLogin = false;
    fieldTextTypePass: boolean;
    user: SocialUser;

    username: string;
    password: string;
    errorMessage = 'Invalid Credentials';
    successMessage: string;

    loginSuccess = false;
    loggedIn: boolean;
    submitted = false;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private authenticationService: AuthenticationService,
        private authService: AuthService,
        private socialService: SocialAuthService) {
    }

    get name() {
        return this.loginForm.get('username');
    }

    @Output()
    userLoginEvent = new EventEmitter();

    private loginForm: FormGroup;

    get pass() {
        return this.loginForm.get('password');
    }

    // Switching method
    togglePassTextType() {
        this.fieldTextTypePass = !this.fieldTextTypePass;
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

        this.initFbLogin();
    }

    onSubmit() {
        this.submitted = true;
        if (this.loginForm.hasError('invalid')) {
            this.submitted = false;
        }
        if (isNonEmptyString(this.username) && isNonEmptyString(this.password)) {
            console.log(this.username + '  ' + this.password);
            // for local login test
            // this.localLoginTest();
            this.handleLogin();
        }
    }

    handleLogin() {
        this.authenticationService.authenticationServiceAuth(this.username, this.password).subscribe(
            (response: any) => {

                if (response.token) {
                    this.invalidLogin = false;
                    this.loginSuccess = true;
                    this.successMessage = 'Login Successful.';
                    console.log(this.successMessage);
                    // console.log(response.token);

                    this.authenticationService.registerSuccessfulAuth(this.username, response.token);

                    /*console.log(sessionStorage.getItem('authenticatedUser'));
                    console.log(sessionStorage.getItem('token'));*/

                    this.router.navigate(['']);
                }
            }, error => {
                this.loginSuccess = false;
                this.invalidLogin = true;
                this.successMessage = 'Login failed.';
                console.log(this.successMessage);
                console.log('log ');
                console.log(error);
                alert('Authentication failed.');
                console.log('Authentication failed.');
            });


        /*this.authenticationService.authenticationServiceLogin(this.username, this.password).subscribe(
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
                    this.loginSuccess = false;
                    this.invalidLogin = true;
                    this.successMessage = 'Login failed.';
                    console.log(this.successMessage);
                }

            }, error => {
                console.log(error);
                alert('Authentication failed.');
                console.log('Authentication failed.');
            });*/
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

    signInWithFB(): void {
        this.authService.signIn(FacebookLoginProvider.PROVIDER_ID);
    }

    socialLogin(id: string, social: string) {
        const url = environment.restUrl + '/login/' + social;
        // console.log('Social login');
        // console.log('Login ' + id);

        this.socialService.socialServiceLogin(id, social).subscribe(
            response => {
                if (response) {
                    this.successMessage = 'Social login Successful.';
                    console.log(this.successMessage);
                    // this.router.navigate(['']);
                } else {
                    this.successMessage = 'Login failed.';
                    console.log(this.successMessage);
                }

            }, error => {
                console.log(error);
                console.log('Authentication failed.');
            });
    }

    signOut(): void {
        this.authService.signOut();
    }

    private initFbLogin() {
        this.authService.authState.subscribe((user) => {
            this.user = user;
            this.loggedIn = (user != null);
            console.log(this.user);

            if (user !== null) {
                this.socialLogin(user.id, 'fb');
            }
        });
    }
}
