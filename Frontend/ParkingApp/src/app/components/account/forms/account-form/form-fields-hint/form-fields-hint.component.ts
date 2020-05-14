import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';


@Component({
    selector: 'app-form-fields-hint',
    templateUrl: './form-fields-hint.component.html',
    styleUrls: ['./form-fields-hint.component.scss']
})
export class FormFieldsHintComponent implements OnInit {

    constructor(public activeModal: NgbActiveModal) {
    }

    ngOnInit() {
    }

}
