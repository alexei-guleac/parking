import { Component, OnInit } from "@angular/core";
import { ComponentWithErrorMsg } from "@app/components/account/forms/account-form/account-form.component";
import { User } from "@app/models/User";
import { AuthenticationService } from "@app/services/account/auth.service";
import { handleHttpErrorResponse } from "@app/services/helpers/global-http-interceptor-service.service";
import { ModalService } from "@app/services/modals/modal.service";
import { NavigationService } from "@app/services/navigation/navigation.service";
import { UserService } from "@app/services/users/user.service";
import { capitalize } from "@app/utils/string-utils";


/**
 * User profile page
 */
@Component({
    selector: "app-user-profile",
    templateUrl: "./user-profile.component.html",
    styleUrls: ["./user-profile.component.scss"]
})
export class UserProfileComponent implements OnInit, ComponentWithErrorMsg {

    errorMessage: string;

    private successMessage: string;

    private isActionSuccess = false;

    private isActionFailed = false;

    private avatar = "~/images/logo.png";

    private user: User;

    private usernameForUpdate: string;

    // specified field names allowed for editing
    private editableFields = ["username", "firstname", "lastname"];

    private status: string;

    constructor(
        private userService: UserService,
        private modalService: ModalService,
        private authenticationService: AuthenticationService,
        private navigationService: NavigationService
    ) {
    }

    /**
     * Initialize the directive/component after Angular first displays the data-bound properties
     * and sets the directive/component's input properties.
     * Called once, after the first ngOnChanges()
     */
    ngOnInit() {
        this.loadLoggedInUser();
    }

    /**
     * Go back to main parking page
     */
    goBack() {
        this.navigationService.navigateToMain();
    }

    /**
     * Load logged in user
     */
    private loadLoggedInUser() {
        const username = this.authenticationService.getLoggedInUserName();
        if (username) {
            this.loadUserProfile(username);
        }
    }

    /**
     * Handle current logged in user profile loading
     * @param username - current logged in user
     */
    private loadUserProfile(username: string) {
        this.userService
            .getUserByUsername(username)
            .subscribe(
                this.handleGetUserResponse(),
                this.handleUserActionError()
            );
    }

    /**
     * Handle user server response
     */
    private handleGetUserResponse() {
        return (user) => {
            if (user) {
                this.user = user;
                console.log("user" + user);
                this.usernameForUpdate = user.username;
            }
        };
    }

    /**
     * Capitalize the first letter of the string
     * @param field - target string field
     */
    private capitalize(field: string) {
        return capitalize(field);
    }

    /**
     * Open profile editing form
     */
    private editUserInfo() {
        this.showProfileEditModal();
    }

    /**
     * Open profile editing modal window form
     */
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

    /**
     * Handle user profile editing
     * @param logoutModalResult - result of user profile editing
     */
    private processEditUserFormResult(logoutModalResult: string) {
        return (result) => {
            logoutModalResult = `Closed with: ${JSON.stringify(result)}`;
            // console.log(logoutModalResult);
            const user = this.buildUpdatedUser(result);

            if (this.userStateChanged(user)) {
                this.updateUserProfile(user);
            }
        };
    }

    /**
     * Build new user information from modal window form result
     * @param result - modal window form result
     */
    private buildUpdatedUser(result) {
        const user = new User();
        this.editableFields.forEach(this.assignUserFields(result, user));

        return user;
    }

    /**
     * Assign editing data fields to new user
     * @param result - modal window form result
     * @param user - target new user
     */
    private assignUserFields(result, user: User) {
        return (field) => {

            // if this field exists and not equals previous
            if (result[field] && result[field] !== this.user[field]) {
                this.user[field] = result[field];
                user[field] = result[field];

                if (field === "firstname") {
                    if (!user.lastname) {
                        user.lastname = this.user.lastname;
                    }
                }
                if (field === "lastname") {
                    if (!user.firstname) {
                        user.firstname = this.user.firstname;
                    }
                }
            }
        };
    }

    /**
     * Check if user data was modified by field presence in result user
     * @param user - new user from editing form
     */
    private userStateChanged(user: User) {
        return this.editableFields.some((field) => user[field] !== undefined);
    }

    /**
     * Fill edit user form with current logged in user data
     * @param componentInstance - modal window form component instance
     */
    private initEditFormFields(componentInstance) {
        this.editableFields.forEach((field) => {
            if (this.user[field]) {
                componentInstance[field] = this.user[field];
            }
        });
    }

    /**
     * handle user profile update request
     * @param user - user being updated
     */
    private updateUserProfile(user: User) {
        this.userService
            .saveUser(user, this.usernameForUpdate)
            .subscribe(
                this.handleSaveUserResponse(),
                this.handleUserActionError()
            );
    }

    /**
     * Handle user modifying server response
     */
    private handleSaveUserResponse() {
        return (response: any) => {
            if (response.success) {
                this.isActionFailed = false;
                this.isActionSuccess = true;
                this.successMessage = "Profile successfully updated.";

                // set new user name
                if (this.user.username !== this.usernameForUpdate) {
                    // save user name for next editing
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

    /**
     * Handle server response error
     */
    private handleUserActionError() {
        return (error) => {
            this.isActionFailed = true;
            this.isActionSuccess = false;

            this.errorMessage = "Something went wrong";
            handleHttpErrorResponse(error, this);
        };
    }

    /**
     * Open social services connection modal window
     */
    private openSocialModal() {
        const modalRef = this.modalService.openSocialConnectModal();
        const componentInstance = modalRef.componentInstance;

        this.initSocialToggles(componentInstance);

        const logoutModalResult = null;
        modalRef.result.then(
            this.processSocialUpdate(), this.processSocialUpdate()
        );
    }

    /**
     * Init social toggles corresponding social connections in user's account
     * @param componentInstance - modal window component instance
     */
    private initSocialToggles(componentInstance) {

        // tslint:disable-next-line:forin
        for (const item in this.user.socialIds) {
            // disable toggle if only one exists
            const enableToggle = Object.keys(this.user.socialIds).length !== 1;

            // hook for all children loaded await
            componentInstance.loaded.subscribe(
                () => {
                    componentInstance.socialToggleComponentChildren.forEach(
                        (child) => {
                            if (child.socialProvider === item) {
                                child.checked = true;
                                child.enabled = enableToggle;
                            }
                        });
                }
            );
        }
    }

    /**
     * Open delete user confirm modal window
     */
    private showDeleteUserModal() {
        const modalRef = this.modalService.openDeleteProfileModal(
            this.user.username
        );
        modalRef.result.then(
            (result) => {
                if (this.modalService.isSubmitResult(result)) {
                    this.deleteUserProfile();
                }
            },
            this.modalService.handleDismissResult()
        );
    }

    /**
     * Handle user profile deleting
     */
    private deleteUserProfile() {
        this.userService
            .deleteUser(this.user.username)
            .subscribe(
                this.handleDeleteUserResponse(),
                this.handleUserActionError()
            );
    }

    /**
     * Handle user profile deleting server response
     */
    private handleDeleteUserResponse() {
        return (response: any) => {
            if (response.success) {
                this.isActionFailed = false;
                this.isActionSuccess = true;
                this.successMessage = "Profile successfully deleted.";

                setTimeout(() => {
                    this.logout();
                }, 5000);
            } else {
                this.isActionFailed = true;
                this.isActionSuccess = false;
            }
        };
    }

    /**
     * Log out from user profile
     */
    private logout() {
        this.authenticationService.processLogout();
        this.navigationService.navigateToMain();
    }

    /**
     * Reload profile after social connection
     */
    private processSocialUpdate() {
        return (result) => {
            this.loadLoggedInUser();
        };
    }
}
