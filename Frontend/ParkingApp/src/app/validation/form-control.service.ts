import {Injectable} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {RegularExpressions} from './reg-exp-patterns';
import {regexpTestValidator} from './regexp-name-validator';


@Injectable({
    providedIn: 'root'
})
export class FormControlService {

    constructor() {
    }

    getUsernameFormControl(username: string) {
        return new FormControl(username, [
            Validators.required,
            Validators.minLength(5),
            Validators.maxLength(15),
            regexpTestValidator(RegularExpressions.usernamePattern)
        ]);
    }

    getEmailFormControl(email: string) {
        return new FormControl(email, [
            Validators.required,
            Validators.minLength(8),
            Validators.maxLength(35),
            Validators.email,
            Validators.pattern(RegularExpressions.emailPattern)
        ]);
    }

    getPasswordFormControl(password: string) {
        return new FormControl(password, [
            Validators.required,
            Validators.minLength(6),
            Validators.maxLength(10),
            Validators.pattern(RegularExpressions.passwordPatternStr)
        ]);
    }

    getConfirmPasswordFormControl(email: string) {
        return new FormControl(email, [
            Validators.required,
            Validators.minLength(8),
            Validators.maxLength(35),
            Validators.email,
            Validators.pattern(RegularExpressions.emailPattern)
        ]);
    }

    getFirstnameFormControl(firstname: string) {
        return new FormControl(firstname, [
            Validators.required,
            Validators.minLength(3),
            Validators.maxLength(15),
            regexpTestValidator(RegularExpressions.namePattern)
        ]);
    }

    getLastnameFormControl(lastname: string) {
        return new FormControl(lastname, [
            Validators.required,
            Validators.minLength(3),
            Validators.maxLength(15),
            regexpTestValidator(RegularExpressions.namePattern)
        ]);
    }

    pwdConfirming(key: string, confirmationKey: string) {
        return (group: FormGroup) => {
            const input = group.controls[key];
            const confirmationInput = group.controls[confirmationKey];

            // console.log('Pass ' + input);
            // console.log('confirmPass ' + confirmationInput);

            // tslint:disable-next-line:triple-equals
            // @ts-ignore
            // console.log('confirmPass  equal ' + confirmationInput === input);


            return confirmationInput.setErrors(
                input.value !== confirmationInput.value ? {notEqual: true} : null
            );
        };
    }
}
