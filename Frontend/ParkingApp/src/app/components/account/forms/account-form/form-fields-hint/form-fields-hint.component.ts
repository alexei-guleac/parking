import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';


@Component({
    selector: 'app-form-fields-hint',
    templateUrl: './form-fields-hint.component.html',
    styleUrls: ['./form-fields-hint.component.scss']
})
export class FormFieldsHintComponent {

    constructor(public activeModal: NgbActiveModal) {
    }

}
