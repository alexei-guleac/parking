import {RouterModule, Routes} from '@angular/router';
import {MainComponent} from './components/main/main.component';
import {ParkingLayoutComponent} from './components/parking-layout/parking-layout.component';
import {StatisticsComponent} from './components/statistics/statistics.component';
import {PrefetchStatsService} from './services/data/prefetch-stats.service';
import {PrefetchParkingLotsService} from './services/data/prefetch-parking-lots.service';
import {AccountFormComponent} from './components/Account/Forms/account-form/account-form.component';
import {PageNotFoundComponent} from './components/page-not-found/page-not-found.component';
import {ConfirmUserComponent} from './components/Account/confirm-user/confirm-user.component';
import {NoConnectionComponent} from './components/no-connection/no-connection.component';

const routes: Routes = [
    {path: '', component: MainComponent},
    {path: 'layout', component: ParkingLayoutComponent},
    {
        path: 'statistics', component: StatisticsComponent,
        resolve: {statistics: PrefetchStatsService, parkingLots: PrefetchParkingLotsService}
    },
    {path: 'account_reset', component: AccountFormComponent},
    {path: 'account', redirectTo: '/account?action=login', pathMatch: 'full'},
    {path: 'login', redirectTo: '/account?action=login', pathMatch: 'full'},
    {path: 'account', component: AccountFormComponent, pathMatch: 'full'},
    {path: 'confirm-account', component: ConfirmUserComponent},
    {path: 'confirm_reset', component: ConfirmUserComponent},
    {path: 'logout', component: AccountFormComponent},
    {path: 'registration', component: AccountFormComponent},
    {path: '404', component: PageNotFoundComponent},
    {path: '500', component: NoConnectionComponent},
    {path: '**', redirectTo: '/404'}
];

export const AppRouting = RouterModule.forRoot(routes, {
    useHash: false
});
