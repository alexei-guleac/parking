import { AfterViewInit, Component, EventEmitter, Output, ViewEncapsulation } from '@angular/core';
import { openClose } from '@app/components/animations/animations';
import { AuthenticationService } from '@app/services/account/auth.service';
import { ModalService } from '@app/services/modals/modal.service';
import { NavigationService } from '@app/services/navigation/navigation.service';
import { ThemeService } from '@app/services/theme.service';
import { capitalizeFirstLetter } from '@app/utils/string-utils';
import { TranslateService } from '@ngx-translate/core';
import { localization } from '../../../environments/localization';


/**
 * Application navigation menu component
 */
@Component({
    selector: 'app-menu',
    animations: [openClose],
    templateUrl: './menu.component.html',
    encapsulation: ViewEncapsulation.None,
    styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements AfterViewInit {
    // for refreshing data
    @Output()
    goBackEvent = new EventEmitter();

    appMenuTooltip = '';

    locales = localization.locales;

    selectedLanguage: string;

    constructor(
        private authenticationService: AuthenticationService,
        private navigation: NavigationService,
        private modalService: ModalService,
        private navigationService: NavigationService,
        private themeService: ThemeService,
        private translate: TranslateService
    ) {
    }

    /**
     * A lifecycle hook that is called after Angular has fully initialized a component's view.
     * Define an ngAfterViewInit() method to handle any additional initialization tasks
     */
    ngAfterViewInit(): void {
        // this.appMenuTooltip = this.translate.instant('menu.tooltips.open-menu');
        this.themeService.initThemeToogle();
        this.initLanguageSelect();
    }

    /*
    * Methods below are intended for navigation to corresponding page
    * */

    /**
     * Change application menu button tooltip text depends on close/open state
     */
    changeMenuTooltip() {
        if (
            document.querySelector('#navbarNav').classList.contains('show')) {
            this.appMenuTooltip = this.translate.instant('menu.tooltips.open-menu');
        } else {
            this.appMenuTooltip = this.translate.instant('menu.tooltips.close-menu');
        }
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

    /**
     * Check if user is logged in
     */
    private isUserLoggedIn() {
        return this.authenticationService.isUserLoggedIn();
    }

    /**
     * Get logged in user name
     */
    private getUserName() {
        return this.authenticationService.getLoggedInUserName();
    }

    /**
     * Capitalize the first letter ща the string
     * @param field - target string field
     */
    private capitalize(field: string) {
        return capitalizeFirstLetter(field);
    }

    /**
     * Open user logout confirm modal window
     */
    private showLogoutModal() {
        const modalRef = this.modalService.openLogoutModal();

        modalRef.result.then(
            (result) => {
                if (this.modalService.isSubmitResult(result)) {
                    this.logout();
                }
            },
            this.modalService.handleDismissResult()
        );
    }

    /**
     * Full logout from all profiles and services with cleaning all additional login information
     */
    private logout() {
        this.authenticationService.fullLogout();
        this.navigationService.navigateToMain();
        // for refreshing data
        this.goBackEvent.emit();
    }

    /**
     * Setup language select in accordance with browser or user selected language
     * This also fix angular first empty option in select
     */
    private initLanguageSelect() {
        setTimeout(() => {
            const menu = document.querySelector('.form-control.select');
            // tslint:disable-next-line:forin
            for (let i = 0; i < menu.children.length; i++) {
                if (menu.children.item(i).textContent.trim() === this.translate.currentLang) {
                    menu[i].selected = true;
                    break;
                }
            }
        }, 1000);
    };

    /**
     * Switch application language based on user choice
     */
    private switchLanguage() {
        if (this.selectedLanguage) {
            this.translate.use(this.selectedLanguage);
        }
    }
}
