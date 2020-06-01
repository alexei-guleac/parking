import { RouterModule, Routes } from '@angular/router';
import { UserProfileComponent } from '@app/components/account/user-profile/user-profile.component';
import { ConfirmUserComponent } from './components/account/confirm-user/confirm-user.component';
import { AccountFormComponent } from './components/account/forms/account-form/account-form.component';
import { AccessDeniedComponent } from './components/pages/errors/access-denied/access-denied.component';
import { NoConnectionComponent } from './components/pages/errors/no-connection/no-connection.component';
import { PageNotFoundComponent } from './components/pages/errors/page-not-found/page-not-found.component';
import { MainComponent } from './components/pages/main/main.component';
import { ParkingLayoutComponent } from './components/pages/parking-layout/parking-layout.component';
import { StatisticsComponent } from './components/pages/statistics/statistics.component';
import { PrefetchParkingLotsService } from './services/data/prefetch-parking-lots.service';
import { PrefetchStatsService } from './services/data/prefetch-stats.service';
import { appRoutes } from './services/navigation/app.endpoints';
import { DirectAccessGuard } from './services/navigation/guards/direct-access-guard.service';
import { ResetGuard } from './services/navigation/guards/reset-guard.service';

/* Application routes */
const MAIN = appRoutes.main;
const LAYOUT = appRoutes.layout;
const STATISTICS = appRoutes.statistics;

const ACCOUNT_RESET = appRoutes.reset;
const ACCOUNT = appRoutes.account;
const ACCOUNT_CONFIRM = appRoutes.accountConfirm;
const ACCOUNT_CONFIRM_RESET = appRoutes.accountConfirmReset;
const LOGOUT = appRoutes.logout;
const REGISTRATION = appRoutes.registration;
const PROFILE = appRoutes.profile;

const NOT_FOUND = appRoutes.notFound;
const SERVER_ERROR = appRoutes.serverError;
const ACCESS_DENIED = appRoutes.accessDenied;

const routes: Routes = [
    // pages
    {
        path: MAIN, component: MainComponent
    },
    { path: LAYOUT, component: ParkingLayoutComponent },
    {
        path: STATISTICS,
        component: StatisticsComponent,
        resolve: {
            statistics: PrefetchStatsService,
            parkingLots: PrefetchParkingLotsService
        }
    },

    // user profile
    { path: PROFILE, component: UserProfileComponent },

    // account forms
    {
        path: ACCOUNT_RESET,
        component: AccountFormComponent,
        canActivate: [ResetGuard, DirectAccessGuard]
    },
    { path: ACCOUNT, component: AccountFormComponent, pathMatch: 'full' },
    { path: ACCOUNT_CONFIRM, component: ConfirmUserComponent },
    { path: ACCOUNT_CONFIRM_RESET, component: ConfirmUserComponent },
    { path: LOGOUT, component: AccountFormComponent },
    { path: REGISTRATION, component: AccountFormComponent },

    // error pages
    { path: NOT_FOUND, component: PageNotFoundComponent },
    { path: SERVER_ERROR, component: NoConnectionComponent },
    { path: ACCESS_DENIED, component: AccessDeniedComponent },

    { path: '**', redirectTo: NOT_FOUND }
];

export const AppRouting = RouterModule.forRoot(routes, {
    useHash: false
});
