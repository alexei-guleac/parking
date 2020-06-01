import { Injectable } from '@angular/core';
import { MODAL_TYPE, MODALS } from '@app/components/modals/ngbd-modal-confirm/ngbd-modal-confirm-autofocus.component';
import { parkingStatuses } from '@app/models/ParkingLotStatus';
import { capitalizeFirstLetter } from '@app/utils/string-utils';
import { ModalDismissReasons, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';


/**
 * Ng-Bootstrap modal windows service
 */
@Injectable({
    providedIn: 'root'
})
export class ModalService {

    /*
    * Standard common modal windows options */
    private standardModalWindowOptions = {
        centered: true,
        windowClass: 'modal-holder',
        backdropClass: 'blurred-backdrop'
    };

    private submitBtnTxt;

    private readonly cancelBtnTxt;

    private logoutModalResult: string;

    constructor(private modalService: NgbModal,
                private translate: TranslateService) {
        // patterns only for revealing the result of a window, do not replace this to locale
        this.submitBtnTxt = 'Ok';
        this.cancelBtnTxt = 'Cancel';
    }

    /**
     * Open standard ng-bootstrap confirm modal window
     * @param title - modal window title
     * @param body  - modal window body
     * @param submitBtnTxt - modal window submit button text
     * @param cancelBtnTxt - modal window cancel button text
     * @param type - specified window type
     * @param options - ng-bootstrap modal window options
     * (content-center, window style class, background site style class)
     */
    openModal(
        title: string,
        body: string,
        submitBtnTxt = this.submitBtnTxt,
        cancelBtnTxt = this.cancelBtnTxt,
        type: string,
        options: {}
    ): NgbModalRef {
        const modalRef = this.modalService.open(MODALS[type], options);

        modalRef.componentInstance.title = title;
        document.querySelector('.modal-body').innerHTML = body;
        modalRef.componentInstance.submitBtnTxt = submitBtnTxt;
        modalRef.componentInstance.cancelBtnTxt = cancelBtnTxt;

        return modalRef;
    }

    /**
     * Open standard ng-bootstrap confirm modal window with autofocus submit button
     * @param title - modal window title
     * @param body  - modal window body
     * @param submitBtnTxt - modal window submit button text
     * @param cancelBtnTxt - modal window cancel button text
     * @param options - ng-bootstrap modal window options
     * (content-center, window style class, background site style class)
     */
    openAutofocusModal(
        title: string,
        body: string,
        submitBtnTxt = this.submitBtnTxt,
        cancelBtnTxt = this.cancelBtnTxt,
        options: {}
    ): NgbModalRef {
        return this.openModal(
            title,
            body,
            submitBtnTxt,
            cancelBtnTxt,
            MODAL_TYPE.autofocus,
            options
        );
    }

    /**
     * Show account log out confirm modal window
     */
    openLogoutModal(): NgbModalRef {
        const modalTitle = this.translate.instant('modals.logout');
        const modalBodyTemplate =
            '<p><strong>' + this.translate.instant('modals.logout-body') + '</strong></p>';
        const cancelBtnTxt =
            this.translate.instant('modals.cancel');

        return this.openAutofocusModal(
            modalTitle,
            modalBodyTemplate,
            modalTitle,
            cancelBtnTxt,
            this.standardModalWindowOptions
        );
    }

    /**
     * Show confirmation profile deleting confirm modal window
     * @param username - user name
     */
    openDeleteProfileModal(username: string): NgbModalRef {
        const modalTitle = this.translate.instant('modals.deletion');
        const modalBodyTemplate =
            this.translate.instant('modals.deletion-body', { username });
        const cancelBtnTxt =
            this.translate.instant('modals.cancel');

        return this.openAutofocusModal(
            modalTitle,
            modalBodyTemplate,
            modalTitle,
            cancelBtnTxt,
            this.standardModalWindowOptions
        );
    }

    /**
     * Show parking lot reservation confirm modal window
     * @param parkingLotNumber - target parking lot number
     * @param parkingLotStatus - target parking lot status
     */
    openReservationModal(
        parkingLotNumber: number,
        parkingLotStatus: string
    ): NgbModalRef {
        const modalTitle = this.translate.instant('modals.reservation');
        const operation =
            parkingLotStatus === parkingStatuses.FREE
                ? this.translate.instant('modals.reserve-operation')
                : this.translate.instant('modals.unreserve-operation');
        const hint =
            parkingLotStatus === parkingStatuses.FREE
                ? this.translate.instant('modals.reserve-action')
                : this.translate.instant('modals.unreserve-action');
        const modalBodyTemplate =
            this.translate.instant('modals.reservation-body', { operation, parkingLotNumber, hint });
        const submitBtnTxt =
            parkingLotStatus === parkingStatuses.FREE
                ? capitalizeFirstLetter(this.translate.instant('modals.reserve-operation'))
                : capitalizeFirstLetter(this.translate.instant('modals.unreserve-operation'));
        const cancelBtnTxt =
            this.translate.instant('modals.cancel');

        return this.openAutofocusModal(
            modalTitle,
            modalBodyTemplate,
            submitBtnTxt,
            cancelBtnTxt,
            this.standardModalWindowOptions
        );
    }

    /**
     * Check if submit button was pressed
     * @param result - modal window dismiss result
     */
    isSubmitResult(result: string): boolean {
        return result.toLowerCase().includes(this.submitBtnTxt.toLowerCase());
    }

    /**
     * Process modal window dismiss reason
     * @param logoutModalResult - target result of dismiss
     */
    processDismissReason(logoutModalResult: string) {
        return (reason) => {
            logoutModalResult = `Dismissed ${this.getDismissReason(reason)}`;
        };
    }

    /**
     * Specify the dismiss reason
     * @param reason - modal window dismiss reason
     */
    getDismissReason(reason: any): string {
        if (reason === ModalDismissReasons.ESC) {
            return 'by pressing ESC';
        } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
            return 'by clicking on a backdrop';
        } else {
            return `with: ${reason}`;
        }
    }

    /**
     * Open modal window with scrolled content
     * @param longContent - some window content
     */
    openScrollModal(longContent: any) {
        this.modalService.open(longContent, { scrollable: true });
    }

    /**
     * Open modal window by specified type
     * @param type - target modal window type
     */
    openModalByType(type: string) {
        return this.modalService.open(
            MODALS[type],
            this.standardModalWindowOptions
        );
    }

    /**
     * Open profile editing modal form
     */
    openProfileEditModal() {
        return this.openModalByType(MODAL_TYPE.accountEditForm);
    }

    /**
     * Open social services account connect modal
     */
    openSocialConnectModal() {
        return this.openModalByType(MODAL_TYPE.socialConnect);
    }

    /**
     * Show form fields hint
     */
    openFormHintModal() {
        return this.openModalByType(MODAL_TYPE.formFieldsHint);
    }

    /**
     * Handle modal window dismiss
     */
    handleDismissResult(nextCallback?) {
        return (reason) => {
            this.logoutModalResult = `Dismissed ${this.getDismissReason(
                reason
            )}`;
            if (nextCallback) nextCallback();
        };
    }

    /**
     * Handle modal window submit with next callback
     */
    handleSubmitResultWithCallback(component, nextCallback?) {
        return (result) => {
            this.logoutModalResult = `Closed with: ${result}`;
            if (this.isSubmitResult(result)) {
                if (nextCallback) component.nextCallback();
            }
        };
    }
}
