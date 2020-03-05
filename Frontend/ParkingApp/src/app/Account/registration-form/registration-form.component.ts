import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthenticationService} from '../auth.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {regexpTestValidator} from '../validation/regexp-name-validator';
import {isNonEmptyString} from '../validation/string-utils';
import {RegularExpressions} from '../validation/reg-exp-patterns';

@Component({
    selector: 'app-reg-form',
    templateUrl: './registration-form.component.html',
    styleUrls: ['./registration-form.component.css']
})
export class RegFormComponent implements OnInit {

    username: string;
    email: string;
    firstname: string;
    lastname: string;
    password: string;
    passConfirm: string;

    errorMessage = 'Invalid Credentials';
    successMessage: string;

    invalidReg = false;
    regSuccess = false;

    @Output()
    userRegEvent = new EventEmitter();

    private regForm: FormGroup;

    submitted = false;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private authenticationService: AuthenticationService) {
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

            passConfirm:
                new FormControl(this.passConfirm, [
                    Validators.required,
                    Validators.minLength(6),
                    Validators.maxLength(10),
                ])
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

    onSubmit() {
        if (isNonEmptyString(this.username) && isNonEmptyString(this.password)) {
            console.log(this.username + '  ' + this.password);
            this.submitted = true;

            /*const newUser = new User(
                this.registerForm.value.username,
                this.registerForm.value.email,
                this.registerForm.value.password,
                this.registerForm.value.name,
                this.registerForm.value.url,
                this.registerForm.value.address,
                this.registerForm.value.city,
                this.registerForm.value.state,
                this.registerForm.value.zipcode,
                this.registerForm.value.phone
            );*/
            this.handleRegistration();
        }
    }

    handleRegistration() {
        this.authenticationService.authenticationServiceRegistration(this.username, this.password).subscribe(
            response => {

                if (response) {
                    this.invalidReg = false;
                    this.regSuccess = true;
                    this.successMessage = 'Registration Successful.';
                    console.log(this.successMessage);
                    alert(this.successMessage);

                    this.navigateToLogin();
                } else {
                    this.invalidReg = true;
                    this.regSuccess = false;
                    console.log(this.successMessage);
                }

            }, error => {
                console.log(error);
                alert('Registration failed.');
                console.log('Registration failed.');
            });
    }

    navigateToLogin() {
        this.router.navigate(['login'], {queryParams: {action: 'login'}});
    }
}
