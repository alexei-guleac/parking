import {Injectable} from '@angular/core';
import {
    MODAL_TYPE,
    MODALS,
} from '@app/components/account/modals/ngbd-modal-confirm/ngbd-modal-confirm-autofocus.component';
import {parkingStatuses} from '@app/models/ParkingLotStatus';
import {ModalDismissReasons, NgbModal, NgbModalRef,} from '@ng-bootstrap/ng-bootstrap';


@Injectable({
    providedIn: 'root',
})
export class ModalService {
    private standardModalWindowOptions = {
        centered: true,
        windowClass: 'modal-holder',
        backdropClass: 'blurred-backdrop',
    };

    private submitBtnTxt = 'Ok';

    private cancelBtnTxt = 'Cancel';

    constructor(private modalService: NgbModal) {
    }

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

    isSubmitResult(result: string): boolean {
        return result.toLowerCase().includes(this.submitBtnTxt.toLowerCase());
    }

    processDismissReason(logoutModalResult: string) {
        return (reason) => {
            logoutModalResult = `Dismissed ${this.getDismissReason(reason)}`;
            console.log(logoutModalResult);
        };
    }

    getDismissReason(reason: any): string {
        if (reason === ModalDismissReasons.ESC) {
            return 'by pressing ESC';
        } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
            return 'by clicking on a backdrop';
        } else {
            return `with: ${reason}`;
        }
    }

    openScrollModal(longContent: any) {
        this.modalService.open(longContent, {scrollable: true});
    }

    openModalByType(type: string) {
        return this.modalService.open(
            MODALS[type],
            this.standardModalWindowOptions
        );
    }

    openProfileEditModal() {
        return this.openModalByType(MODAL_TYPE.accountEditForm);
    }
}
