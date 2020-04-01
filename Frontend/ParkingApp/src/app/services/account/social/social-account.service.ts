import {Injectable} from '@angular/core';
import {
    AuthService,
    AuthServiceConfig,
    FacebookLoginProvider,
    GoogleLoginProvider,
    LinkedInLoginProvider,
    LoginOpt,
    VKLoginProvider
} from 'angularx-social-login-vk';
import {Observable} from 'rxjs';
import {environment} from '../../../../environments/environment';
import {SocialSignInRequest, SocialSignUpRequest} from '../../../models/payload/SocialSignInRequest';
import {User} from '../../../models/User';
import {HttpClientService} from '../../helpers/http-client.service';
import {api} from '../../navigation/app.endpoints';
import {GithubOauthService} from './github-oauth.service';
import {MsalAuthService} from './msal-auth.service';
import {SocialUserService} from './social-user.service';


const fbLoginOptions: LoginOpt = {
    scope:
        'pages_messaging,pages_messaging_subscriptions,email,pages_show_list,manage_pages',
    return_scopes: true,
    enable_profile_selector: true
}; // https://developers.facebook.com/docs/reference/javascript/FB.login/v2.11

const googleLoginOptions: LoginOpt = {
    scope: 'profile email'
}; // https://developers.google.com/api-client-library/javascript/reference/referencedocs#gapiauth2clientconfig

export function provideConfig() {
    return new AuthServiceConfig([
        {
            id: FacebookLoginProvider.PROVIDER_ID,
            provider: new FacebookLoginProvider('509861456396823') // nata.nemuza
        },
        {
            id: GoogleLoginProvider.PROVIDER_ID,
            provider: new GoogleLoginProvider(
                '878973947322-drnv3qvmpvs3kaie7s699g99t2odi4fq.apps.googleusercontent.com'
            ) // aleks7900
        },
        {
            id: VKLoginProvider.PROVIDER_ID,
            provider: new VKLoginProvider('7373880') // AG
        },
        {
            id: LinkedInLoginProvider.PROVIDER_ID,
            provider: new LinkedInLoginProvider('771wdzxnwaaund') // private profile
        }
    ]);
}

export const providerNames = {
    fb: 'facebook',
    g: 'google',
    vk: 'vkontakte',
    lin: 'linkedin',
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

@Injectable({
    providedIn: 'root'
})
export class SocialAccountService {
    constructor(
        private OAuth: AuthService,
        private http: HttpClientService,
        private githubAuthService: GithubOauthService,
        private msalAuthService: MsalAuthService,
        private socialUserService: SocialUserService
    ) {
    }

    socialServiceLogin(
        socialRequest: SocialSignInRequest,
        deviceInfo: any
    ): Observable<any> {
        const url = environment.restUrl + api.authSocial;
        // console.log('Social login');
        // console.log('Login ' + id);

        return this.http.postJsonRequest<any>(url, {
            id: socialRequest.id,
            socialProvider: socialRequest.socialProvider,
            deviceInfo
        });
    }

    socialServiceSignUp(
        socialRequest: SocialSignUpRequest,
        deviceInfo: any
    ): Observable<any> {
        const url = environment.restUrl + api.regSocial;
        // console.log('Social login');
        // console.log('Login ' + id);

        return this.http.postJsonRequest<any>(url, {
            id: socialRequest.id,
            user: socialRequest.user,
            socialProvider: socialRequest.socialProvider,
            deviceInfo
        });
    }

    socialSingIn(socialProvider: string) {
        let socialPlatformProvider: string;

        switch (socialProvider) {
            case providerNames.fb:
                socialPlatformProvider = FacebookLoginProvider.PROVIDER_ID;
                break;
            case providerNames.g:
                socialPlatformProvider = GoogleLoginProvider.PROVIDER_ID;
                break;
            case providerNames.vk:
                socialPlatformProvider = VKLoginProvider.PROVIDER_ID;
                break;
        }

        /*if (socialProvider === providerNames.linkedin.name) {
            console.log('LINKEEEEEEEEEEEEEED');
            socialPlatformProvider = LinkedInLoginProvider.PROVIDER_ID;
            social = providerNames.linkedin.short;
        }*/

        this.OAuth.signIn(socialPlatformProvider).then(socialUser => {
            console.log('then ', socialProvider, socialUser);

            if (socialUser !== null) {
                console.log(socialUser.provider);
                console.log(this.OAuth.readyState);

                this.socialUserService.socialUser.next(socialUser);
            }
        });
    }

    handleGithubOauthRequest(code: string) {
        this.githubAuthService.handleGithubOauthRequest(code);
    }

    githubLogin() {
        this.githubAuthService.loginWithGithub();
    }

    microsoftLogin() {
        this.msalAuthService.msalLogin();
    }

    getMsProfile() {
        return this.msalAuthService.getProfile();
    }

    createUser(socialUser: any, socialProvider: string): User {
        switch (socialProvider) {
            case providerNames.fb:
                return this.socialUserService.createFbUser(socialUser);
            case providerNames.g:
                return this.socialUserService.createGoogleUser(socialUser);
            case providerNames.vk:
                return this.socialUserService.createVkUser(socialUser);
            /*case providerNames.lin:
                return this.socialUserService.createLinkedInUser(socialUser);*/
            case providerNames.git:
                return this.socialUserService.createGitUser(socialUser);
            case providerNames.ms:
                return this.socialUserService.createMSUser(socialUser);
            case providerNames.a:
                return this.socialUserService.createAmazonUser(socialUser);
        }
    }
}
