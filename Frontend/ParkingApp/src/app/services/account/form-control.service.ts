import { Injectable } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { RegularExpressions } from "@app/validation/reg-exp-patterns";
import { regexpTestValidator } from "@app/validation/regexp-name-validator";


/**
 * Provides Angular form controls for form inputs
 */
@Injectable({
    providedIn: 'root',
})
export class FormControlService {
    constructor() {
    }

    /**
     * Get form control for username
     * @param username - user username
     */
    getUsernameFormControl(username: string) {
        return new FormControl(username, [
            Validators.required,
            Validators.minLength(5),
            Validators.maxLength(15),
            regexpTestValidator(RegularExpressions.usernamePattern),
        ]);
    }

    /**
     * Get form control for email
     * @param email - user email
     */
    getEmailFormControl(email: string) {
        return new FormControl(email, [
            Validators.required,
            Validators.minLength(8),
            Validators.maxLength(35),
            Validators.email,
            Validators.pattern(RegularExpressions.emailPattern),
        ]);
    }

    /**
     * Get form control for password
     * @param password - user password
     */
    getPasswordFormControl(password: string) {
        return new FormControl(password, [
            Validators.required,
            Validators.minLength(6),
            Validators.maxLength(10),
            Validators.pattern(RegularExpressions.passwordPatternStr),
        ]);
    }

    /**
     * Get form control for password confirmation
     * @param password - user password confirmation
     */
    getConfirmPasswordFormControl(password: string) {
        return new FormControl(password, [
            Validators.required,
            Validators.minLength(8),
            Validators.maxLength(35),
            Validators.email,
            Validators.pattern(RegularExpressions.emailPattern),
        ]);
    }

    /**
     * Get form control for firstname
     * @param firstname - user firstname
     */
    getFirstnameFormControl(firstname: string) {
        return new FormControl(firstname, [
            Validators.required,
            Validators.minLength(3),
            Validators.maxLength(15),
            regexpTestValidator(RegularExpressions.namePattern),
        ]);
    }

    /**
     * Get form control for lastname
     * @param lastname - user lastname
     */
    getLastnameFormControl(lastname: string) {
        return new FormControl(lastname, [
            Validators.required,
            Validators.minLength(3),
            Validators.maxLength(15),
            regexpTestValidator(RegularExpressions.namePattern),
        ]);
    }

    /**
     * Check if password and password confirmation equals
     * @param key - target field value for comparison
     * @param confirmationKey - confirmation field
     */
    checkPasswordConfirm(key: string, confirmationKey: string) {
        return (group: FormGroup) => {
            const input = group.controls[key];
            const confirmationInput = group.controls[confirmationKey];

            return confirmationInput.setErrors(
                input.value !== confirmationInput.value
                    ? {notEqual: true}
                    : null
            );
        };
    }
}

/**
 * Toggle password field type for get visibility
 * @param component - component with field to be switched
 */
export const togglePassTextType = (component) => {
    component.fieldTextTypePass = !component.fieldTextTypePass;
};

/**
 * Toggle password confirmation field type for get visibility
 * @param component - component with field to be switched
 */
export const togglePassConfirmTextType = (component) => {
    component.fieldTextTypePassConfirm = !component.fieldTextTypePassConfirm;
};
