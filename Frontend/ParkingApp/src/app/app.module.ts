import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatButtonModule} from '@angular/material/button';
import {MatCardModule} from '@angular/material/card';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MsalInterceptor, MsalModule} from '@azure/msal-angular';
import {Angulartics2Module} from 'angulartics2';
import {AuthServiceConfig, SocialLoginModule} from 'angularx-social-login-vk';
import {LoggerModule, NgxLoggerLevel} from 'ngx-logger';
import {NgxPaginationModule} from 'ngx-pagination';
import {NgxUiLoaderHttpModule, NgxUiLoaderModule, NgxUiLoaderRouterModule} from 'ngx-ui-loader';
import {environment} from '../environments/environment';
import {AppComponent} from './app.component';
import {AppRouting} from './app.routing';
import {ConfirmUserComponent} from './components/Account/confirm-user/confirm-user.component';
import {AccountFormComponent} from './components/Account/Forms/account-form/account-form.component';
import {ForgotPassFormComponent} from './components/Account/Forms/forgot-pass-form/forgot-pass-form.component';
import {LoginFormComponent} from './components/Account/Forms/login-form/login-form.component';
import {OrSeparatorComponent} from './components/Account/Forms/or-separator/or-separator.component';
import {RegFormComponent} from './components/Account/Forms/registration-form/registration-form.component';
import {ResetPasswordComponent} from './components/Account/Forms/reset-password/reset-password.component';
import {SocialButtonsComponent} from './components/Account/Forms/social-buttons/social-buttons.component';
import {UsernameInputComponent} from './components/Account/Forms/username-input/username-input.component';
import {GithubOauthComponent} from './components/Account/github-oauth/github-oauth.component';
import {MainComponent} from './components/main/main.component';
import {ParkingLotDetailComponent} from './components/main/parking-lot-detail/parking-lot-detail.component';
import {MenuComponent} from './components/menu/menu.component';
import {NoConnectionComponent} from './components/no-connection/no-connection.component';
import {FeatureComponent} from './components/old/features/feature.component';
import {ParkingLayoutOldComponent} from './components/old/parking-layout-old/parking-layout-old.component';
import {PageNotFoundComponent} from './components/page-not-found/page-not-found.component';
import {ParkingLayoutComponent} from './components/parking-layout/parking-layout.component';
import {StatisticsComponent} from './components/statistics/statistics.component';
import {JwtAppModule} from './jwt.module';
import {AccountStorageTypeService} from './services/account/session-storage.service';
import {provideConfig} from './services/account/social-auth.service';
import {GlobalHttpErrorInterceptorService} from './services/helpers/global-http-interceptor-service.service';
import {TokenInterceptor} from './services/helpers/token-interceptor.service';
import {XhrInterceptor} from './services/helpers/xhr-interceptor.service';
import {api} from './services/navigation/app.endpoints';
import {DirectAccessGuard} from './services/navigation/guards/direct-access-guard.service';
import {ResetGuard} from './services/navigation/guards/reset-guard.service';
import {EqualValidator} from './validation/equal-validator.directive';
import {NoDblClickDirective} from './validation/no-dbl-click.directive';
import {RECAPTCHA_URL, ReCaptchaDirective} from './validation/recaptcha-validator';

// import {} from 'bower_components/angular-mailcheck';


export const protectedResourceMap: [string, string[]][] = [
    ['https://graph.microsoft.com/v1.0/me', ['user.read']]
];

const isIE = window.navigator.userAgent.indexOf('MSIE ') > -1 || window.navigator.userAgent.indexOf('Trident/') > -1;

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
        NoConnectionComponent,
        GithubOauthComponent,

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
        }), // import NgxUiLoaderHttpModule. By default, it will show background loader.
        // If you need to show foreground spinner, do as follow:
        // NgxUiLoaderHttpModule.forRoot({ showForeground: true })
        LoggerModule.forRoot({
            level: !environment.production ? NgxLoggerLevel.LOG : NgxLoggerLevel.OFF,
            // serverLogLevel
            serverLogLevel: NgxLoggerLevel.OFF
        }),
        MsalModule.forRoot({
                auth: {
                    clientId: 'f90657e8-cfae-450c-98b8-f595ca39a884',
                    authority: 'https://login.microsoftonline.com/common/',
                    validateAuthority: true,
                    redirectUri: 'http://localhost:4200/account',
                    postLogoutRedirectUri: 'http://localhost:4200/account',
                    navigateToLoginRequestUrl: true,
                },
                cache: {
                    cacheLocation: 'localStorage',
                    storeAuthStateInCookie: isIE, // set to true for IE 11
                },
            },
            {
                popUp: !isIE,
                consentScopes: [
                    'user.read',
                    'openid',
                    'profile'
                ],
                unprotectedResources: ['https://www.microsoft.com/en-us/'],
                protectedResourceMap,
                extraQueryParameters: {}
            })
    ],

    providers: [
        ResetGuard, DirectAccessGuard,
        {
            provide: RECAPTCHA_URL,
            useValue: environment.restUrl + api.recaptcha
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: MsalInterceptor,
            multi: true
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
    // Diagnostic only: inspect router configuration
    /*constructor(router: Router) {
        // Use a custom replacer to display function names in the route configs
        const replacer = (key, value) => (typeof value === 'function') ? value.name : value;

        console.log('Routes: ', JSON.stringify(router.config, replacer, 2));
    }*/
}
