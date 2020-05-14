import { Injectable } from '@angular/core';
import { User } from '@app/models/User';
import { storageKeys } from '@app/services/navigation/app.endpoints';
import {
    AuthServiceConfig,
    FacebookLoginProvider,
    GoogleLoginProvider,
    LinkedInLoginProvider,
    LoginOpt,
    VKLoginProvider
} from 'angularx-social-login-vk';
import { BehaviorSubject, Subject } from 'rxjs';


@Injectable({
    providedIn: 'root'
})
export class SocialUserStorageService {

    socialUser: Subject<any> = new BehaviorSubject<any>(null);

    gitUser: Subject<any> = new BehaviorSubject<any>(null);

    socialServerResponse: Subject<any> = new Subject<any>();

    constructor() {

    }

    /**
     * Create backend accepted Facebook social service user
     * @param socialUser - received social user
     */
    createFbUser(socialUser: any): User {
        const user = new User();
        user.id = socialUser.id;
        user.email = socialUser.email;
        user.firstname = this.getFirstname(socialUser.name);
        user.lastname = socialUser.lastName;
        return user;
    }

    /**
     * Create backend accepted Google social service user
     * @param socialUser - received social user
     */
    createGoogleUser(socialUser: any): User {
        const user = new User();
        user.id = socialUser.id;
        user.email = socialUser.email;
        user.firstname = this.getFirstname(socialUser.name);
        user.lastname = socialUser.lastName;
        return user;
    }

    /**
     * Create backend accepted VKontakte social service user
     * @param socialUser - received social user
     */
    createVkUser(socialUser: any): User {
        const user = new User();
        user.id = socialUser.id;
        user.firstname = this.getFirstname(socialUser.name);
        user.lastname = socialUser.lastName;
        return user;
    }

    /**
     * Create backend accepted Github social service user
     * @param socialUser - received social user
     */
    createGitUser(socialUser: any): User {
        const user = new User();
        user.id = socialUser.id;
        user.username = socialUser.login;
        user.email = socialUser.email;
        user.firstname = this.getFirstname(socialUser.name);
        return user;
    }

    /**
     * Create backend accepted Microsoft social service user
     * @param socialUser - received social user
     */
    createMSUser(socialUser: any): User {
        const user = new User();
        user.id = socialUser.id;
        user.email = socialUser.userPrincipalName;
        user.firstname = this.getFirstname(socialUser.displayName);
        user.lastname = socialUser.surname;
        return user;
    }

    /**
     * Create backend accepted Amazon social service user
     * @param socialUser - received social user
     */
    createAmazonUser(socialUser: any): User {
        const user = new User();
        user.id = socialUser.CustomerId;
        user.email = socialUser.PrimaryEmail;
        user.firstname = this.getFirstname(socialUser.Name);
        return user;
    }

    /**
     * Clean Github auth information for avoid conflicts
     * when switching from login to registration and vice versa
     */
    cleanGitAuth() {
        sessionStorage.removeItem(storageKeys.gitCode);
        sessionStorage.removeItem(storageKeys.action);
    }

    /**
     * Save Github OAuth 2.0 code to browser session storage
     * @param code - target user Github OAuth 2.0 code
     */
    setGitOauthCode(code: string) {
        sessionStorage.setItem(storageKeys.gitCode, code);
    }

    /**
     * Get Github OAuth 2.0 code from browser session storage
     */
    getGitOauthCode() {
        return sessionStorage.getItem(storageKeys.gitCode);
    }

    /**
     * Save Github OAuth 2.0 action to browser session storage
     * @param action - target user action (login or registration)
     */
    setGitOauthAction(action: string) {
        sessionStorage.setItem(storageKeys.action, action);
    }

    /**
     * Get Github OAuth 2.0 action from browser session storage
     */
    getGitOauthAction() {
        return sessionStorage.getItem(storageKeys.action);
    }

    /**
     * Clear social user information
     */
    clearSocialUser() {
        this.socialUser = new BehaviorSubject<any>(null);
    }

    clearSocialResponse() {
        this.socialServerResponse = new Subject<any>();
    }

    /**
     * Get social user firstname from fullname
     * @param fullname - social user fullname
     */
    private getFirstname(fullname: string) {
        return fullname.split(' ')[0];
    }
}

/**
 * Additional social service provider options
 */
const fbLoginOptions: LoginOpt = {
    scope:
        'pages_messaging,pages_messaging_subscriptions,email,pages_show_list,manage_pages',
    return_scopes: true,
    enable_profile_selector: true
}; // https://developers.facebook.com/docs/reference/javascript/FB.login/v2.11

const googleLoginOptions: LoginOpt = {
    scope: 'profile email'
}; // https://developers.google.com/api-client-library/javascript/reference/referencedocs#gapiauth2clientconfig

/**
 * Provides common social service providers config
 */
export function provideAuthServiceConfig() {
    return new AuthServiceConfig([
        {
            id: FacebookLoginProvider.PROVIDER_ID,
            provider: new FacebookLoginProvider('509861456396823')
        },
        {
            id: GoogleLoginProvider.PROVIDER_ID,
            provider: new GoogleLoginProvider(
                '878973947322-drnv3qvmpvs3kaie7s699g99t2odi4fq.apps.googleusercontent.com'
            )
        },
        {
            id: VKLoginProvider.PROVIDER_ID,
            provider: new VKLoginProvider('7373880')
        },
        {
            id: LinkedInLoginProvider.PROVIDER_ID,
            provider: new LinkedInLoginProvider('771wdzxnwaaund')
        }
    ]);
}

/**
 * Social service providers names
 */
export const socialProviderNames = {
    fb: 'facebook',
    g: 'google',
    vk: 'vkontakte',
    git: 'github',
    ms: 'microsoft',
    a: 'amazon',
    t: 'twitter',
    in: 'instagram',

    facebook: 'fb',
    google: 'g',
    vkontakte: 'vk',
    linkedin: 'lin',
    github: 'git',
    microsoft: 'ms',
    amazon: 'a',
    twitter: 't',
    instagram: 'in'
};

/**
 * Social providers names used in this application
 */
export const usedSocialProviders = [
    socialProviderNames.fb,
    socialProviderNames.g,
    socialProviderNames.ms,
    socialProviderNames.git,
    socialProviderNames.vk,
    socialProviderNames.a
];

/**
 * Social service login form common provider names
 * (with common library 'angularx-social-login-vk')
 */
export const commonLibrarySocialProviders = [
    socialProviderNames.fb,
    socialProviderNames.g,
    socialProviderNames.vk
];
