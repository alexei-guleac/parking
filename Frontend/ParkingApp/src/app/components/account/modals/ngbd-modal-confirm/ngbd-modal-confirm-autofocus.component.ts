import {Component, Input, Type} from '@angular/core';
import {AccountEditModalFormComponent} from '@app/components/account/modals/account-edit-modal-content/account-edit-modal-form.component';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';


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
    styleUrls: ['./ngbd-modal.component.scss'],
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
    styleUrls: ['./ngbd-modal.component.scss'],
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

export const MODALS: { [name: string]: Type<any> } = {
    focusFirst: NgbdModalConfirmComponent,
    autofocus: NgbdModalConfirmAutofocusComponent,
    accountEditForm: AccountEditModalFormComponent,
    usernameEditForm: AccountEditModalFormComponent,
};

export const MODAL_TYPE = {
    focusFirst: 'focusFirst',
    autofocus: 'autofocus',
    accountEditForm: 'accountEditForm',
    usernameEditForm: 'usernameEditForm',
};

/*@Component({
    selector: 'app-ngbd-modal-confirm-autofocus',
    template: `
        <div class="modal-header">
            <h4 class="modal-title" id="modal-title">Profile deletion</h4>
            <button type="button" class="close" aria-label="Close button" aria-describedby="modal-title"
                    (click)="modal.dismiss('Cross click')">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
        <div class="modal-body">
            <p><strong>Are you sure you want to delete <span class="text-primary">"John Doe"</span>
                profile?</strong>
            </p>
            <p>All information associated to this user profile will be permanently deleted.
                <span class="text-danger">This operation can not be undone.</span>
            </p>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-outline-secondary" (click)="modal.dismiss('cancel click')">
                Cancel
            </button>
            <button type="button" ngbAutofocus appNoDblClick class="btn btn-danger" (click)="modal.close('Ok click')">Ok
            </button>
        </div>
    `
})
export class NgbdModa {
    constructor(public modal: NgbActiveModal) {
    }
}*/
