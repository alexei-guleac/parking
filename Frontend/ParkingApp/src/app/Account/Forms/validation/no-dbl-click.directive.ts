import {Directive, HostListener} from '@angular/core';

@Directive({
    selector: '[appNoDblClick]'
})
export class NoDblClickDirective {

    constructor() {
    }

    @HostListener('click', ['$event'])
    clickEvent(event) {
        const target = event.target || event.srcElement;
        // see if the srcElement has the disabled property. If so then it is the actual button. If not then the user
        // clicked on the button's text (span element)
        const button = (target.disabled === undefined) ? target.parentElement : target;

        button.setAttribute('disabled', true);
        setTimeout( () => {
            button.removeAttribute('disabled');
        }, 1000);
    }
}
