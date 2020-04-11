import {AfterViewInit, Component, EventEmitter, OnInit, Output, ViewEncapsulation,} from '@angular/core';
import {openClose} from '@app/components/animations/animations';
import {AuthenticationService} from '@app/services/account/auth.service';
import {ModalService} from '@app/services/modals/modal.service';
import {NavigationService} from '@app/services/navigation/navigation.service';
import {ThemeService} from '@app/services/theme.service';
import {capitalize} from '@app/utils/string-utils';


@Component({
    selector: 'app-menu',
    animations: [openClose],
    templateUrl: './menu.component.html',
    encapsulation: ViewEncapsulation.None,
    styleUrls: ['./menu.component.scss'],
})
export class MenuComponent implements OnInit, AfterViewInit {
    // for refreshing data
    @Output()
    goBackEvent = new EventEmitter();

    constructor(
        private authenticationService: AuthenticationService,
        private navigation: NavigationService,
        private modalService: ModalService,
        private navigationService: NavigationService,
        private themeService: ThemeService
    ) {
    }

    // private themeToogleChecked = false;

    ngOnInit() {
    }

    ngAfterViewInit(): void {
        this.themeService.initThemeToogle();
    }

    private navigateToMain() {
        this.navigation.navigateToMain();
    }

    private navigateToProfile() {
        this.navigation.navigateToUserProfile();
    }

    private navigateToStatistics() {
        this.navigation.navigateToStatistics();
    }

    private navigateToLayout() {
        this.navigation.navigateToParkingLayout();
    }

    private navigateToLogin() {
        this.navigation.navigateToLogin();
    }

    private isUserLoggedIn() {
        // console.log('isUserLoggedIn ' + this.authenticationService.isUserLoggedIn());
        return this.authenticationService.isUserLoggedIn();
    }

    private getUserName() {
        return this.authenticationService.getLoggedInUserName();
    }

    private capitalize(field: string) {
        return capitalize(field);
    }

    private showLogoutModal() {
        const modalRef = this.modalService.openLogoutModal();
        let logoutModalResult: string;

        modalRef.result.then(
            (result) => {
                logoutModalResult = `Closed with: ${result}`;

                if (this.modalService.isSubmitResult(result)) {
                    this.logout();
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
        // for refreshing data
        this.goBackEvent.emit();
    }
}
