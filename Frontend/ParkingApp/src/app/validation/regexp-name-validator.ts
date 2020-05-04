import { AbstractControl, ValidatorFn } from "@angular/forms";


/**
 * Username regular expression validator
 * @param nameRe - target regular expression
 */
export function regexpTestValidator(nameRe: RegExp): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
        const forbidden = nameRe.test(control.value);
        return !forbidden ? {forbiddenName: {value: control.value}} : null;
    };
}
