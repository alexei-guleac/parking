import { Component } from '@angular/core';


/**
 * Separator between form field and social login buttons
 */
@Component({
    selector: 'app-or-separator',
    template: '<div class="or-seperator"><b>{{ \'separator\' | translate }}</b></div>',
    styleUrls: ['./or-separator.component.scss']
})
export class OrSeparatorComponent {
    constructor() {
    }
}
