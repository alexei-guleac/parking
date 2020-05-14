import { Component, Input, Type } from '@angular/core';
import { FormFieldsHintComponent } from '@app/components/account/forms/account-form/form-fields-hint/form-fields-hint.component';
import { AccountEditModalFormComponent } from '@app/components/account/modals/account-edit-modal-content/account-edit-modal-form.component';
import { SocialConnectionModalComponent } from '@app/components/account/modals/social-connection-modal/social-connection-modal.component';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';


/**
 * Confirm modal windows ng-bootstrap templates (optionally with button autofocus)
 */
@Component({
    selector: 'app-ngbd-modal-confirm',
    template: `
        <div class="modal-header">
            <h4 class="modal-title" id="modal-title">{{ title }}</h4>
            <button
                    type="button"
                    class="close"
                    aria-describedby="modal-title"
                    aria-label="Close button"
                    (click)="modal.dismiss('Cross click')"
            >
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
        <div class="modal-body">{{ body }}</div>
        <div class="modal-footer">
            <button
                    type="button"
                    class="btn btn-outline-secondary"
                    (click)="modal.dismiss('cancel click')"
            >
                {{ cancelBtnTxt || 'Cancel' }}
            </button>
            <button
                    type="button"
                    appNoDblClick
                    class="btn btn-danger"
                    (click)="modal.close('Ok click')"
            >
                {{ submitBtnTxt || 'Ok' }}
            </button>
        </div>
    `,
    styleUrls: ['./ngbd-modal.component.scss']
})
export class NgbdModalConfirmComponent {
    @Input()
    title: string;

    @Input()
    body: string;

    @Input()
    cancelBtnTxt: string;

    @Input()
    submitBtnTxt: string;

    constructor(public modal: NgbActiveModal) {
    }
}

@Component({
    selector: 'app-ngbd-modal-confirm-autofocus',
    template: `
        <div class="modal-header">
            <h4 class="modal-title" id="modal-title">{{ title }}</h4>
            <button
                    type="button"
                    class="close"
                    aria-describedby="modal-title"
                    aria-label="Close button"
                    (click)="modal.dismiss('Cross click')"
            >
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
        <div class="modal-body">{{ body }}</div>
        <div class="modal-footer">
            <button
                    type="button"
                    class="btn btn-outline-secondary"
                    (click)="modal.dismiss('cancel click')"
            >
                {{ cancelBtnTxt || 'Cancel' }}
            </button>
            <button
                    type="button"
                    ngbAutofocus
                    appNoDblClick
                    class="btn btn-danger"
                    (click)="modal.close('Ok click')"
            >
                {{ submitBtnTxt || 'Ok' }}
            </button>
        </div>
    `,
    styleUrls: ['./ngbd-modal.component.scss']
})
export class NgbdModalConfirmAutofocusComponent {
    @Input()
    title: string;

    @Input()
    body: string;

    @Input()
    cancelBtnTxt: string;

    @Input()
    submitBtnTxt: string;

    constructor(public modal: NgbActiveModal) {
    }
}

/**
 * Modal window types components-handlers
 */
export const MODALS: { [name: string]: Type<any> } = {
    focusFirst: NgbdModalConfirmComponent,
    autofocus: NgbdModalConfirmAutofocusComponent,
    accountEditForm: AccountEditModalFormComponent,
    usernameEditForm: AccountEditModalFormComponent,
    socialConnect: SocialConnectionModalComponent,
    formFieldsHint: FormFieldsHintComponent
};

/**
 * Modal window types
 */
export const MODAL_TYPE = {
    focusFirst: 'focusFirst',
    autofocus: 'autofocus',
    accountEditForm: 'accountEditForm',
    usernameEditForm: 'usernameEditForm',
    socialConnect: 'socialConnect',
    formFieldsHint: 'formFieldsHint'

};
