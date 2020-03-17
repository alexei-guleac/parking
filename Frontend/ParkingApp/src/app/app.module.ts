import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatCardModule} from '@angular/material/card';
import {MatButtonModule} from '@angular/material/button';
import {NgxPaginationModule} from 'ngx-pagination';
import {AppComponent} from './app.component';
import {PageNotFoundComponent} from './components/page-not-found/page-not-found.component';
import {MainComponent} from './components/main/main.component';
import {MenuComponent} from './components/menu/menu.component';
import {ParkingLotDetailComponent} from './components/main/parking-lot-detail/parking-lot-detail.component';
import {LoginFormComponent} from './components/Account/Forms/login-form/login-form.component';
import {StatisticsComponent} from './components/statistics/statistics.component';
import {FeatureComponent} from './components/old/features/feature.component';
import {ParkingLayoutComponent} from './components/parking-layout/parking-layout.component';
import {ParkingLayoutOldComponent} from './components/old/parking-layout-old/parking-layout-old.component';
import {RegFormComponent} from './components/Account/Forms/registration-form/registration-form.component';
import {EqualValidator} from './validation/equal-validator.directive';
import {NoDblClickDirective} from './validation/no-dbl-click.directive';
import {AuthServiceConfig, SocialLoginModule} from 'angularx-social-login';
import {RECAPTCHA_URL, ReCaptchaDirective} from './validation/recaptcha-validator';
import {OrSeparatorComponent} from './components/Account/Forms/or-separator/or-separator.component';
import {UsernameInputComponent} from './components/Account/Forms/username-input/username-input.component';
import {SocialButtonsComponent} from './components/Account/Forms/social-buttons/social-buttons.component';
import {GlobalHttpErrorInterceptorService} from './services/helpers/global-http-interceptor-service.service';
import {Angulartics2Module} from 'angulartics2';
import {XhrInterceptor} from './services/helpers/xhr-interceptor.service';
import {TokenInterceptor} from './services/helpers/token-interceptor.service';
import {provideConfig} from './services/account/social-auth.service';
import {AccountStorageTypeService} from './services/account/session-storage.service';
import {environment} from '../environments/environment';
import {api} from './services/navigation/app.endpoints';
import {AccountFormComponent} from './components/Account/Forms/account-form/account-form.component';
import {ForgotPassFormComponent} from './components/Account/Forms/forgot-pass-form/forgot-pass-form.component';
import {ConfirmUserComponent} from './components/Account/confirm-user/confirm-user.component';
import {AppRouting} from './app.routing';
import {JwtAppModule} from './jwt.module';
import {NgxUiLoaderHttpModule, NgxUiLoaderModule, NgxUiLoaderRouterModule} from 'ngx-ui-loader';
import {NoConnectionComponent} from "./components/no-connection/no-connection.component";
import {ResetPasswordComponent} from "./components/Account/Forms/reset-password/reset-password.component";

// import {} from 'bower_components/angular-mailcheck';


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
        ParkingLayoutComponent,
        ParkingLayoutOldComponent,
        OrSeparatorComponent,
        UsernameInputComponent,
        SocialButtonsComponent,
        AccountFormComponent,
        ForgotPassFormComponent,
        ConfirmUserComponent,
        ResetPasswordComponent,
        NoConnectionComponent
    ],

    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        FormsModule,
        ReactiveFormsModule,
        HttpClientModule,
        AppRouting,
        NgxPaginationModule,
        BrowserAnimationsModule,
        SocialLoginModule,
        MatCardModule,
        MatButtonModule,
        Angulartics2Module.forRoot(),
        JwtAppModule,
        NgxUiLoaderModule,      // import NgxUiLoaderModule
        NgxUiLoaderRouterModule.forRoot({
            showForeground: true
        }), // import NgxUiLoaderRouterModule. By default, it will show foreground loader.
        // If you need to show background spinner, do as follow:
        // NgxUiLoaderRouterModule.forRoot({ showForeground: false })
        // AngularMailAutocompleteModule
        NgxUiLoaderHttpModule.forRoot({
            showForeground: true,
            minTime: 3000
        }) // import NgxUiLoaderHttpModule. By default, it will show background loader.
        // If you need to show foreground spinner, do as follow:
        // NgxUiLoaderHttpModule.forRoot({ showForeground: true })
    ],

    providers: [{
        provide: RECAPTCHA_URL,
        useValue: environment.restUrl + api.recaptcha
    },
        {provide: HTTP_INTERCEPTORS, useClass: GlobalHttpErrorInterceptorService, multi: true},
        {provide: HTTP_INTERCEPTORS, useClass: XhrInterceptor, multi: true},
        {provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true},
        {
            provide: AuthServiceConfig,
            useFactory: provideConfig
        }, AccountStorageTypeService],
    bootstrap: [AppComponent]
})

export class AppModule {
}
