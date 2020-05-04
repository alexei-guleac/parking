import { Injectable } from "@angular/core";
import {
    MODAL_TYPE,
    MODALS
} from "@app/components/account/modals/ngbd-modal-confirm/ngbd-modal-confirm-autofocus.component";
import { parkingStatuses } from "@app/models/ParkingLotStatus";
import { ModalDismissReasons, NgbModal, NgbModalRef } from "@ng-bootstrap/ng-bootstrap";


/**
 * Ng-Bootstrap modal windows service
 */
@Injectable({
    providedIn: "root"
})
export class ModalService {

    /*
    * Standard common modal windows options */
    private standardModalWindowOptions = {
        centered: true,
        windowClass: 'modal-holder',
        backdropClass: "blurred-backdrop"
    };

    private submitBtnTxt = 'Ok';

    private cancelBtnTxt = 'Cancel';

    private logoutModalResult: string;

    constructor(private modalService: NgbModal) {
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
        const modalTitle = 'Logout';
        const modalBodyTemplate =
            '<p><strong>Are you sure you want to sign out?</strong></p>';

        return this.openAutofocusModal(
            modalTitle,
            modalBodyTemplate,
            modalTitle,
            this.cancelBtnTxt,
            this.standardModalWindowOptions
        );
    }

    /**
     * Show confirmation profile deleting confirm modal window
     * @param username - user name
     */
    openDeleteProfileModal(username: string): NgbModalRef {
        const modalTitle = 'Profile deletion';
        const modalBodyTemplate = `<p><strong>Are you sure you want to delete
                <span class="text-primary">${username}</span> profile?</strong></p>
                <p>All information associated to this user profile will be permanently deleted.
                <span class="text-danger">This operation can not be undone.</span></p>`;

        return this.openAutofocusModal(
            modalTitle,
            modalBodyTemplate,
            modalTitle,
            this.cancelBtnTxt,
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
        const modalTitle = 'Parking lot reservation';
        const operation =
            parkingLotStatus === parkingStatuses.FREE
                ? 'reservate'
                : 'unreserve';
        const hint =
            parkingLotStatus === parkingStatuses.FREE
                ? 'cancel'
                : 'submit again';
        const modalBodyTemplate = `<p><strong>Are you sure you want to ${operation} <span class="text-primary">parking lot</span>
                number <span class="text-primary">${parkingLotNumber}</span>?</strong>
            </p>
            <p><span class="text-success">You can ${hint} this reservation later.</span>
            </p>`;
        const submitBtnTxt =
            parkingLotStatus === parkingStatuses.FREE
                ? 'Reservate'
                : 'Unreserve';

        return this.openAutofocusModal(
            modalTitle,
            modalBodyTemplate,
            submitBtnTxt,
            this.cancelBtnTxt,
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
