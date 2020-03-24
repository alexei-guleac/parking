import {RouterModule, Routes} from '@angular/router';
import {ConfirmUserComponent} from './components/Account/confirm-user/confirm-user.component';
import {AccountFormComponent} from './components/Account/Forms/account-form/account-form.component';
import {GithubOauthComponent} from './components/Account/github-oauth/github-oauth.component';
import {MainComponent} from './components/main/main.component';
import {NoConnectionComponent} from './components/no-connection/no-connection.component';
import {PageNotFoundComponent} from './components/page-not-found/page-not-found.component';
import {ParkingLayoutComponent} from './components/parking-layout/parking-layout.component';
import {StatisticsComponent} from './components/statistics/statistics.component';
import {PrefetchParkingLotsService} from './services/data/prefetch-parking-lots.service';
import {PrefetchStatsService} from './services/data/prefetch-stats.service';
import {appRoutes} from './services/navigation/app.endpoints';
import {DirectAccessGuard} from './services/navigation/guards/direct-access-guard.service';
import {ResetGuard} from './services/navigation/guards/reset-guard.service';


const MAIN = appRoutes.main;
const LAYOUT = appRoutes.layout;
const STATISTICS = appRoutes.statistics;
const ACCOUNT_RESET = appRoutes.reset;
const ACCOUNT = appRoutes.account;
const LOGIN = appRoutes.login;
const ACCOUNT_LOGIN = appRoutes.accountLogin;
const GITHUB_OAUTH = appRoutes.gitOAuth;
const ACCOUNT_CONFIRM = appRoutes.accountConfirm;
const ACCOUNT_CONFIRM_RESET = appRoutes.accountConfirmReset;
const LOGOUT = appRoutes.logout;
const REGISTRATION = appRoutes.registration;
const NOT_FOUND = appRoutes.notFound;
const SERVER_ERROR = appRoutes.serverError;


const routes: Routes = [
    {path: MAIN, component: MainComponent},
    {path: LAYOUT, component: ParkingLayoutComponent},
    {
        path: STATISTICS, component: StatisticsComponent,
        resolve: {statistics: PrefetchStatsService, parkingLots: PrefetchParkingLotsService}
    },
    {
        path: ACCOUNT_RESET, component: AccountFormComponent, canActivate: [ResetGuard, DirectAccessGuard]
    },
    /*{path: ACCOUNT, redirectTo: ACCOUNT_LOGIN, pathMatch: 'full'},
    {path: LOGIN, redirectTo: ACCOUNT_LOGIN, pathMatch: 'full'},*/
    {path: ACCOUNT, component: AccountFormComponent, pathMatch: 'full'},
    {path: GITHUB_OAUTH, component: GithubOauthComponent},
    {path: ACCOUNT_CONFIRM, component: ConfirmUserComponent},
    {path: ACCOUNT_CONFIRM_RESET, component: ConfirmUserComponent},
    {path: LOGOUT, component: AccountFormComponent},
    {path: REGISTRATION, component: AccountFormComponent},
    {path: NOT_FOUND, component: PageNotFoundComponent},
    {path: SERVER_ERROR, component: NoConnectionComponent},
    {path: '**', redirectTo: NOT_FOUND}
];

export const AppRouting = RouterModule.forRoot(routes, {
    useHash: false
});
