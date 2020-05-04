import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup } from "@angular/forms";
import { FormControlService } from "@app/services/account/form-control.service";
import { isNonEmptyStrings } from "@app/utils/string-utils";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";


/**
 * Account edit modal window form
 */
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

    /**
     * Initialize the directive/component after Angular first displays the data-bound properties
     * and sets the directive/component's input properties.
     * Called once, after the first ngOnChanges()
     */
    ngOnInit(): void {
        this.createForm();
    }

    /**
     * Submit form handler
     * @param valid - form validation state
     */
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

    /**
     * Initiate profile edit form group with validation
     */
    private createForm() {
        this.editForm = new FormGroup({
            username: this.formControlService.getUsernameFormControl(this.username),
            firstname: this.formControlService.getFirstnameFormControl(this.firstname),
            lastname: this.formControlService.getLastnameFormControl(this.lastname)
        });
    }

    /**
     * Close modal profile edit window with form fields result
     * @param username - form username input value
     * @param firstname - form firstname input value
     * @param lastname - form lastname input value
     */
    private handleEditProfile(username, firstname, lastname) {
        this.activeModal.close({username, firstname, lastname});
    }
}
