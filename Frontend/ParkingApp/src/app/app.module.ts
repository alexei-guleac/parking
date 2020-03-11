import {BrowserModule} from '@angular/platform-browser';
import {Injectable, NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {RouterModule, Routes} from '@angular/router';
import {NgxPaginationModule} from 'ngx-pagination';
import {AppComponent} from './app.component';
import {PageNotFoundComponent} from './page-not-found/page-not-found.component';
import {MainComponent} from './main/main.component';
import {MenuComponent} from './menu/menu.component';
import {ParkingLotDetailComponent} from './main/parking-lot-detail/parking-lot-detail.component';
import {LoginFormComponent} from './Account/Forms/login-form/login-form.component';
import {StatisticsComponent} from './statistics/statistics.component';
import {FeatureComponent} from './feature/feature.component';
import {PrefetchStatsService} from './prefetch-stats.service';
import {PrefetchParkingLotsService} from './prefetch-parking-lots.service';
import {Feature2Component} from './feature/feature2/feature2.component';
import {ParkingLayoutComponent} from './main/parking-layout/parking-layout.component';
import {RegFormComponent} from './Account/Forms/registration-form/registration-form.component';
import {EqualValidator} from './Account/Forms/validation/equal-validator.directive';
import {NoDblClickDirective} from './Account/Forms/validation/no-dbl-click.directive';
import {AuthServiceConfig, FacebookLoginProvider, SocialLoginModule} from 'angularx-social-login';
import {RECAPTCHA_URL, ReCaptchaDirective} from './Account/Forms/validation/recaptcha-validator';
import {environment} from '../environments/environment';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatCardModule} from '@angular/material/card';
import {MatButtonModule} from '@angular/material/button';
import {OrSeparatorComponent} from './Account/Forms/or-separator/or-separator.component';
import {UsernameInputComponent} from './Account/Forms/username-input/username-input.component';
import {SocialButtonsComponent} from './Account/Forms/social-buttons/social-buttons.component';

// import {} from 'bower_components/angular-mailcheck';


@Injectable()
export class XhrInterceptor implements HttpInterceptor {

    intercept(req: HttpRequest<any>, next: HttpHandler) {
        const xhr = req.clone({
            headers: req.headers.set('X-Requested-With', 'XMLHttpRequest')
        });
        return next.handle(xhr);
    }
}

const config = new AuthServiceConfig([
    {
        id: FacebookLoginProvider.PROVIDER_ID,
        provider: new FacebookLoginProvider('509861456396823')   // nata.nemuza
    }
]);

export function provideConfig() {
    return config;
}

const routes: Routes = [
    {path: 'test', component: FeatureComponent},
    {path: 'test2', component: Feature2Component},
    {path: 'layout', component: ParkingLayoutComponent},
    {
        path: 'stats',
        component: StatisticsComponent,
        resolve: {stats: PrefetchStatsService, parkingLots: PrefetchParkingLotsService}
    },
    {path: '', component: MainComponent},

    {path: 'login', component: LoginFormComponent},
    {path: 'logout', component: LoginFormComponent},
    {path: 'registration', component: RegFormComponent},
    // {path: '', component: LoginFormComponent},

    {path: '404', component: PageNotFoundComponent},
    {path: '**', redirectTo: '/404'}
];

@NgModule({
    declarations: [
        AppComponent,
        PageNotFoundComponent,
        MainComponent,
        MenuComponent,
        ParkingLotDetailComponent,
        LoginFormComponent,
        RegFormComponent,
        ReCaptchaDirective,
        EqualValidator,
        NoDblClickDirective,
        StatisticsComponent,
        FeatureComponent,
        Feature2Component,
        ParkingLayoutComponent,
        OrSeparatorComponent,
        UsernameInputComponent,
        SocialButtonsComponent
    ],

    imports: [
        BrowserModule,
        FormsModule,
        ReactiveFormsModule,
        HttpClientModule,
        RouterModule.forRoot(routes),
        NgxPaginationModule,
        BrowserAnimationsModule,
        SocialLoginModule,
        MatCardModule,
        MatButtonModule,
        // AngularMailAutocompleteModule
    ],

    /*
    providers: [AuthenticationService/*, {provide: HTTP_INTERCEPTORS, useClass: XhrInterceptor, multi: true}* /],
    */
    providers: [{
        provide: RECAPTCHA_URL,
        useValue: environment.restUrl + '/validate_captcha'
    }, {
        provide: AuthServiceConfig,
        useFactory: provideConfig
    }],
    bootstrap: [AppComponent]
})

export class AppModule {
}
