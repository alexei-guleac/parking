import { HTTP_INTERCEPTORS, HttpClientModule } from "@angular/common/http";
import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from "@angular/material/card";
import { BrowserModule } from "@angular/platform-browser";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { provideAuthServiceConfig } from "@app/services/account/social/social-user-storage.service";
import { MsalInterceptor } from "@azure/msal-angular";
import { environment } from "@env";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import { Angulartics2Module } from "angulartics2";
import { AuthServiceConfig, SocialLoginModule } from "angularx-social-login-vk";
import { NgxLinkedinModule } from "ngx-linkedin";
import { NgxLoginWithAmazonButtonModule } from "ngx-login-with-amazon-button";
import { NgxPaginationModule } from "ngx-pagination";
import { NgxUiLoaderHttpModule, NgxUiLoaderModule, NgxUiLoaderRouterModule } from "ngx-ui-loader";
import { AppComponent } from "./app.component";
import { AppRouting } from "./app.routing";
import { ConfirmUserComponent } from "./components/account/confirm-user/confirm-user.component";
import { AccountFormComponent } from "./components/account/forms/account-form/account-form.component";
import { FormFieldsHintComponent } from "./components/account/forms/account-form/form-fields-hint/form-fields-hint.component";
import { ForgotPassFormComponent } from "./components/account/forms/forgot-pass-form/forgot-pass-form.component";
import { LoginFormComponent } from "./components/account/forms/login-form/login-form.component";
import { OrSeparatorComponent } from "./components/account/forms/or-separator/or-separator.component";
import { RegFormComponent } from "./components/account/forms/registration-form/registration-form.component";
import { ResetPasswordComponent } from "./components/account/forms/reset-password/reset-password.component";
import { SocialButtonsComponent } from "./components/account/forms/social-buttons/social-buttons.component";
import { AccountEditModalFormComponent } from "./components/account/modals/account-edit-modal-content/account-edit-modal-form.component";
import {
    NgbdModalConfirmAutofocusComponent,
    NgbdModalConfirmComponent
} from "./components/account/modals/ngbd-modal-confirm/ngbd-modal-confirm-autofocus.component";
import { SocialConnectionModalComponent } from "./components/account/modals/social-connection-modal/social-connection-modal.component";
import { SocialToggleComponent } from "./components/account/modals/social-connection-modal/social-toogle/social-toggle.component";
import { UserProfileComponent } from "./components/account/user-profile/user-profile.component";
import { AccessDeniedComponent } from "./components/errors/access-denied/access-denied.component";
import { NoConnectionComponent } from "./components/errors/no-connection/no-connection.component";
import { PageNotFoundComponent } from "./components/errors/page-not-found/page-not-found.component";
import { MenuComponent } from "./components/menu/menu.component";
import { MainComponent } from "./components/pages/main/main.component";
import { ParkingLotDetailComponent } from "./components/pages/main/parking-lot-detail/parking-lot-detail.component";
import { ParkingLayoutComponent } from "./components/pages/parking-layout/parking-layout.component";
import { StatisticsComponent } from "./components/pages/statistics/statistics.component";
import { JwtAppModule } from "./jwt.module";
import { MsalAppModule } from "./msal.module";
import { AccountStorageService } from "./services/account/account-session-storage.service";
import { GlobalHttpErrorInterceptorService } from "./services/helpers/global-http-interceptor-service.service";
import { TokenInterceptor } from "./services/helpers/token-interceptor.service";
import { XhrInterceptor } from "./services/helpers/xhr-interceptor.service";
import { api } from "./services/navigation/app.endpoints";
import { DirectAccessGuard } from "./services/navigation/guards/direct-access-guard.service";
import { ResetGuard } from "./services/navigation/guards/reset-guard.service";
import { NoDblClickDirective } from "./validation/no-dbl-click.directive";
import { RECAPTCHA_URL, ReCaptchaDirective } from "./validation/recaptcha-validator";

/* COMPONENTS DECLARATIONS */

// pages
const PAGES = [
    MainComponent,
    ParkingLayoutComponent,
    ParkingLotDetailComponent,
    StatisticsComponent,
];

// Modals
const MODALS = [
    NgbdModalConfirmComponent,
    NgbdModalConfirmAutofocusComponent,
    AccountEditModalFormComponent,
    SocialConnectionModalComponent,
    FormFieldsHintComponent];

// account forms
const FORMS = [
    AccountFormComponent,
    LoginFormComponent,
    RegFormComponent,
    ConfirmUserComponent,
    ResetPasswordComponent,
    SocialButtonsComponent,
    OrSeparatorComponent,
    ForgotPassFormComponent,
    ReCaptchaDirective,
    NoDblClickDirective,
];

// errors
const ERRORS = [
    PageNotFoundComponent,
    NoConnectionComponent,
    AccessDeniedComponent,
    UserProfileComponent,
];

/* MODULES IMPORTS */

// socials
const SOCIALS = [
    SocialLoginModule,
    NgxLinkedinModule.forRoot({
        clientId: ':clientId:',
    }),
    NgxLoginWithAmazonButtonModule.forRoot(
        'amzn1.application-oa2-client.041834d99fcd4c4fb5f59493ed285af8'
    ),
    MsalAppModule,
];

// loader
const LOADER = [
    NgxUiLoaderModule, // import NgxUiLoaderModule
    NgxUiLoaderRouterModule.forRoot({
        showForeground: true,
    }), // import NgxUiLoaderRouterModule. By default, it will show foreground loader.
    // If you need to show background spinner, do as follow:
    // NgxUiLoaderRouterModule.forRoot({ showForeground: false })
    // AngularMailAutocompleteModule
    NgxUiLoaderHttpModule.forRoot({
        showForeground: true,
        minTime: 3000,
    }), // import NgxUiLoaderHttpModule. By default, it will show background loader.
    // If you need to show foreground spinner, do as follow:
    // NgxUiLoaderHttpModule.forRoot({ showForeground: true })
];

@NgModule({
    declarations: [
        AppComponent,
        MenuComponent,

        ...PAGES,   // pages
        ...MODALS,  // Modals
        ...FORMS,   // account forms
        ...ERRORS, SocialToggleComponent, FormFieldsHintComponent  // errors
    ],

    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        FormsModule,
        ReactiveFormsModule,
        HttpClientModule,

        NgxPaginationModule,

        NgbModule,
        MatCardModule,
        MatButtonModule,
        Angulartics2Module.forRoot(),
        AppRouting,
        JwtAppModule,

        ...SOCIALS, // socials
        ...LOADER  // loader
    ],

    providers: [
        ResetGuard,
        DirectAccessGuard,
        {
            provide: RECAPTCHA_URL,
            useValue: environment.restUrl + api.recaptcha,
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: MsalInterceptor,
            multi: true,
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: GlobalHttpErrorInterceptorService,
            multi: true,
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: XhrInterceptor,
            multi: true,
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: TokenInterceptor,
            multi: true,
        },
        {
            provide: AuthServiceConfig,
            useFactory: provideAuthServiceConfig
        },
        AccountStorageService,
    ],
    bootstrap: [AppComponent],
    entryComponents: [
        ...MODALS
    ],
})

export class AppModule {
}
