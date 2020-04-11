import {Component, OnInit} from '@angular/core';
import {ComponentWithErrorMsg} from '@app/components/account/forms/account-form/account-form.component';
import {User} from '@app/models/User';
import {AuthenticationService} from '@app/services/account/auth.service';
import {handleHttpErrorResponse} from '@app/services/helpers/global-http-interceptor-service.service';
import {ModalService} from '@app/services/modals/modal.service';
import {NavigationService} from '@app/services/navigation/navigation.service';
import {UserService} from '@app/services/users/user.service';
import {capitalize} from '@app/utils/string-utils';


@Component({
    selector: 'app-user-profile',
    templateUrl: './user-profile.component.html',
    styleUrls: ['./user-profile.component.scss'],
})
export class UserProfileComponent implements OnInit, ComponentWithErrorMsg {
    errorMessage: string;

    private successMessage: string;

    private isActionSuccess = false;

    private isActionFailed = false;

    private avatar = '~/images/logo.png';

    private user: User;

    private usernameForUpdate: string;

    private editableFields = ['username', 'firstname', 'lastname'];

    private status: string;

    constructor(
        private userService: UserService,
        private modalService: ModalService,
        private authenticationService: AuthenticationService,
        private navigationService: NavigationService
    ) {
    }

    ngOnInit() {
        const username = this.authenticationService.getLoggedInUserName();
        if (username) {
            this.loadUserProfile(username);
        }
    }

    /*    private loadData() {
            this.dataService.getAllParkingLots().subscribe(
                data => {
                    if (data.length !== 0) {
                        this.parkingLots = data.sort((a, b) =>
                            a.number > b.number ? 1 : a.number < b.number ? -1 : 0
                        );
                    } else {
                        this.parkingLots = null;
                    }
                },
                error => {
                    this.parkingLots = null;
                }
            );
        }*/

    private loadUserProfile(username: string) {
        this.userService
            .getUserByUsername(username)
            .subscribe(
                this.handleGetUserResponse(),
                this.handleUserActionError()
            );
    }

    private handleGetUserResponse() {
        return (user) => {
            if (user) {
                console.log(user);
                this.user = user;
                this.usernameForUpdate = user.username;
            }
        };
    }

    private handleUserActionError() {
        return (error) => {
            this.errorMessage = 'Something went wrong';
            handleHttpErrorResponse(error, this);
        };
    }

    private capitalize(field: string) {
        return capitalize(field);
    }

    private editUserInfo() {
        this.showProfileEditModal();
    }

    private showProfileEditModal() {
        const modalRef = this.modalService.openProfileEditModal();
        const componentInstance = modalRef.componentInstance;

        this.initEditFormFields(componentInstance);

        const logoutModalResult = null;
        modalRef.result.then(
            this.processEditUserFormResult(logoutModalResult),
            this.modalService.processDismissReason(logoutModalResult)
        );
    }

    private processEditUserFormResult(logoutModalResult: string) {
        return (result) => {
            logoutModalResult = `Closed with: ${JSON.stringify(result)}`;
            console.log(logoutModalResult);

            const user = this.buildUpdatedUser(result);
            /*console.log(JSON.stringify(user));
            console.log(this.userStateChanged(user));*/
            if (this.userStateChanged(user)) {
                this.updateUserProfile(user);
            }
        };
    }

    private buildUpdatedUser(result) {
        const user = new User();
        this.editableFields.forEach(this.assignUserFields(result, user));

        return user;
    }

    private assignUserFields(result, user: User) {
        return (field) => {
            if (result[field] && result[field] !== this.user[field]) {
                this.user[field] = result[field];
                user[field] = result[field];

                if (field === 'firstname') {
                    if (!user.lastname) {
                        user.lastname = this.user.lastname;
                    }
                }
                if (field === 'lastname') {
                    if (!user.firstname) {
                        user.firstname = this.user.firstname;
                    }
                }
            }
        };
    }

    private userStateChanged(user: User) {
        return this.editableFields.some((field) => user[field] !== undefined);
    }

    private initEditFormFields(componentInstance) {
        this.editableFields.forEach((field) => {
            if (this.user[field]) {
                componentInstance[field] = this.user[field];
            }
        });
    }

    private updateUserProfile(user: User) {
        this.userService
            .saveUser(user, this.usernameForUpdate)
            .subscribe(
                this.handleSaveUserResponse(),
                this.handleUserActionError()
            );
    }

    private handleSaveUserResponse() {
        return (response: any) => {
            if (response.success) {
                this.isActionFailed = false;
                this.isActionSuccess = true;
                this.successMessage = 'Profile successfully updated.';
                console.log(this.successMessage);

                console.log(this.user.username + 'this.user.username');
                console.log(this.usernameForUpdate + 'this.usernameForUpdate');
                if (this.user.username !== this.usernameForUpdate) {
                    this.usernameForUpdate = this.user.username;
                    this.authenticationService.setLoggedInUserName(
                        this.user.username
                    );
                }
            } else {
                this.isActionFailed = true;
                this.isActionSuccess = false;
            }
        };
    }

    private showDeleteUserModal() {
        const modalRef = this.modalService.openDeleteProfileModal(
            this.user.username
        );
        let logoutModalResult: string;

        modalRef.result.then(
            (result) => {
                logoutModalResult = `Closed with: ${result}`;

                if (this.modalService.isSubmitResult(result)) {
                    this.deleteUserProfile();
                }
                console.log(logoutModalResult);
            },
            (reason) => {
                logoutModalResult = `Dismissed ${this.modalService.getDismissReason(
                    reason
                )}`;
                console.log(logoutModalResult);
            }
        );
    }

    private logout() {
        this.authenticationService.processLogout();
        // console.log(sessionStorage.getItem(storageKeys.TOKEN_NAME));
        this.navigationService.navigateToMain();
    }

    private deleteUserProfile() {
        this.userService
            .deleteUser(this.user.username)
            .subscribe(
                this.handleDeleteUserResponse(),
                this.handleUserActionError()
            );
    }

    private handleDeleteUserResponse() {
        return (response: any) => {
            if (response.success) {
                this.isActionFailed = false;
                this.isActionSuccess = true;
                this.successMessage = 'Profile successfully deleted.';
                console.log(this.successMessage);
                setTimeout(() => {
                    this.logout();
                }, 5000);
            } else {
                this.isActionFailed = true;
                this.isActionSuccess = false;
            }
        };
    }
}
