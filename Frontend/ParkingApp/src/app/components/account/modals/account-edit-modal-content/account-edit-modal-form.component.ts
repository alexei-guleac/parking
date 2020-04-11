import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {FormControlService} from '@app/services/account/form-control.service';
import {isNonEmptyStrings} from '@app/utils/string-utils';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';


@Component({
    selector: 'app-account-edit-modal-content',
    templateUrl: './account-edit-modal-form.component.html',
    styleUrls: ['./account-edit-modal-form.component.scss'],
})
export class AccountEditModalFormComponent implements OnInit {
    private submitted = false;

    private editForm: FormGroup;

    private username: string;

    private firstname: string;

    private lastname: string;

    constructor(
        public activeModal: NgbActiveModal,
        private formBuilder: FormBuilder,
        private formControlService: FormControlService
    ) {
    }

    get name() {
        return this.editForm.get('username');
    }

    get fname() {
        return this.editForm.get('firstname');
    }

    get lname() {
        return this.editForm.get('lastname');
    }

    ngOnInit(): void {
        this.createForm();
    }

    onSubmit(valid: boolean) {
        this.submitted = true;
        if (this.editForm.hasError('invalid')) {
            this.submitted = false;
        }

        if (isNonEmptyStrings(this.username, this.firstname, this.lastname)) {
            if (!valid) {
                return;
            }
            this.handleEditProfile(
                this.username,
                this.firstname,
                this.lastname
            );
        }
    }

    private createForm() {
        this.editForm = new FormGroup({
            username: this.formControlService.getUsernameFormControl(
                this.username
            ),
            firstname: this.formControlService.getFirstnameFormControl(
                this.firstname
            ),
            lastname: this.formControlService.getLastnameFormControl(
                this.lastname
            ),
        });
    }

    private handleEditProfile(username, firstname, lastname) {
        this.activeModal.close({username, firstname, lastname});
    }
}
