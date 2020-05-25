import { Injectable } from '@angular/core';
import {
    AmazonAuthRequest,
    AmazonSignUpRequest,
    CommonSocialAuthRequest,
    CommonSocialConnectRequest,
    CommonSocialDisconnectRequest,
    CommonSocialSignUpRequest,
    GithubAuthRequest,
    GithubSignUpRequest,
    MicrosoftAuthRequest,
    MicrosoftSignUpRequest,
    SocialConnectRequest,
    SocialDisconnectRequest,
    SocialSignInRequest,
    SocialSignUpRequest
} from '@app/models/payload/SocialSignInRequest';
import { User } from '@app/models/User';
import { AuthenticationService } from '@app/services/account/auth.service';
import { GithubOauthService } from '@app/services/account/social/github-oauth.service';
import { MsalAuthService } from '@app/services/account/social/msal-auth.service';
import { SocialAccountService } from '@app/services/account/social/social-account.service';
import {
    commonLibrarySocialProviders,
    socialProviderNames,
    SocialUserStorageService
} from '@app/services/account/social/social-user-storage.service';
import { actions } from '@app/services/navigation/app.endpoints';
import { DeviceInfoStorage } from '@app/utils/device-fingerprint';
import { AuthService, FacebookLoginProvider, GoogleLoginProvider, VKLoginProvider } from 'angularx-social-login-vk';
import { Subscription } from 'rxjs';


/**
 * Social providers login/registration methods
 */
@Injectable({
    providedIn: 'root'
})
export class SocialProviderService {

    private msAuthSubscription: Subscription;

    private msLoginAllowed = false;

    private accountAction: string;

    constructor(private OAuth: AuthService,
                private socialService: SocialAccountService,
                private githubAuthService: GithubOauthService,
                private msalAuthService: MsalAuthService,
                private socialUserService: SocialProviderService,
                private socialUserStorageService: SocialUserStorageService,
                private authenticationService: AuthenticationService
                /*private ngxLinkedinService: NgxLinkedinService,*/) {
    }

    /**
     * Process social login dependent on provider
     * @param socialProvider - social provider short name
     * @param action - user action (login, registration, connection)
     */
    processSocialLogin(socialProvider: string, action: string) {
        // reset social user information to prevent conflicts
        this.socialUserStorageService.clearSocialUser();
        this.accountAction = action;

        console.log(commonLibrarySocialProviders.includes(socialProvider));
        if (commonLibrarySocialProviders.includes(socialProvider)) {
            // in advance subscribes to subject when social user information arrives
            this.socialUserStorageService.socialUser.subscribe(
                this.handleArrivedSocialUser(socialProvider));
            // perform social sign in
            this.socialSingIn(socialProvider);
        }

        if (socialProvider === socialProviderNames.git) {
            this.socialUserStorageService.setGitOauthAction(action);
            setTimeout(null, 500);
            this.githubLogin();
        }
        if (socialProvider === socialProviderNames.ms) {
            this.microsoftLogin();
            this.msLoginAllowed = true;
            this.processMSCallback();
        }
        if (socialProvider === socialProviderNames.a) {
            this.socialUserStorageService.clearSocialUser();
            this.socialUserStorageService.socialUser.subscribe(this.handleAmazonUser());
        }
        /*if (socialProvider === this.socialProviders.linkedin.name) {
            const social = this.socialProviders.linkedin.short;

            this.linkedInLogin();
        }*/
    }

    /**
     * Process social service login form common providers
     * (with common library 'angularx-social-login-vk')
     */
    socialSingIn(socialProvider: string) {
        let socialPlatformProvider: string;

        switch (socialProvider) {
            case socialProviderNames.fb:
                socialPlatformProvider = FacebookLoginProvider.PROVIDER_ID;
                break;
            case socialProviderNames.g:
                socialPlatformProvider = GoogleLoginProvider.PROVIDER_ID;
                break;
            case socialProviderNames.vk:
                socialPlatformProvider = VKLoginProvider.PROVIDER_ID;
                break;
        }

        /*if (socialProvider === providerNames.linkedin.name) {
            socialPlatformProvider = LinkedInLoginProvider.PROVIDER_ID;
            social = providerNames.linkedin.short;
        }*/

        this.OAuth.signIn(socialPlatformProvider).then((socialUser) => {
            if (socialUser !== null) {
                this.socialUserStorageService.socialUser.next(socialUser);
            }
        });
    }

    /**
     * Initiates Github API user request
     * @param code - code for Github server OAuth
     */
    handleGithubOauthRequest(code: string) {
        this.socialUserStorageService.gitUser.subscribe(this.handleGitHubUser());
        this.githubAuthService.handleGithubOauthRequest(code);
    }

    /**
     * Login with Github account
     */
    githubLogin() {
        this.githubAuthService.loginWithGithub();
    }

    /**
     * Login with Microsoft account
     */
    microsoftLogin() {
        this.msalAuthService.msalLogin();
    }

    /**
     * Get Microsoft user profile
     */
    getMsProfile() {
        return this.msalAuthService.getProfile();
    }

    /**
     * Converts given social user to backend acceptable user model
     * @param socialUser - received user from social service
     * @param socialProvider - short social provider name
     */
    createUserFromSocialUser(socialUser: any, socialProvider: string): User {
        switch (socialProvider) {
            case socialProviderNames.fb:
                return this.socialUserStorageService.createFbUser(socialUser);
            case socialProviderNames.g:
                return this.socialUserStorageService.createGoogleUser(socialUser);
            case socialProviderNames.vk:
                return this.socialUserStorageService.createVkUser(socialUser);
            case socialProviderNames.git:
                return this.socialUserStorageService.createGitUser(socialUser);
            case socialProviderNames.ms:
                return this.socialUserStorageService.createMSUser(socialUser);
            case socialProviderNames.a:
                return this.socialUserStorageService.createAmazonUser(socialUser);
        }
    }

    /**
     * Process social service provider disconnect request
     * @param socialProvider - short social provider name
     */
    processSocialDisconnect(socialProvider: string) {
        this.sendSocialDisconnectRequest(
            new CommonSocialDisconnectRequest(
                this.authenticationService.getLoggedInUserName(),
                socialProviderNames[socialProvider]
            )
        );
    }

    /**
     * Clean user related data from browser storage
     */
    cleanGitAuth() {
        return this.socialUserStorageService.cleanGitAuth();
    }

    /**
     * Get Github OAuth 2.0 code from browser session storage
     */
    getGitOauthCode() {
        return this.socialUserStorageService.getGitOauthCode();
    }

    /**
     * Get Github OAuth 2.0 action from browser session storage
     */
    getGitOauthAction() {
        return this.socialUserStorageService.getGitOauthAction();
    }

    /**
     * Prepare social user login or registration request
     * @param socialProvider - target social provider
     */
    private handleArrivedSocialUser(socialProvider: string) {

        return (socialUser: any) => {
            if (socialUser != null) {
                if (this.accountAction === actions.login) {
                    // if id is provided
                    if (socialUser.id) {
                        this.sendSocialLoginRequest(
                            new CommonSocialAuthRequest(
                                socialUser.id,
                                socialProviderNames[socialProvider]
                            )
                        );
                    }
                }

                if (this.accountAction === actions.registration) {
                    const user = this.createUserFromSocialUser(
                        socialUser,
                        socialProvider
                    );
                    if (user.id) {
                        this.sendSocialSignUpRequest(
                            new CommonSocialSignUpRequest(
                                user.id,
                                user,
                                socialProviderNames[socialProvider]
                            )
                        );
                    }
                }

                if (this.accountAction === actions.connectSocial) {
                    const user = this.createUserFromSocialUser(
                        socialUser,
                        socialProvider
                    );
                    // if id is provided
                    if (user.id) {
                        this.sendSocialConnectRequest(
                            new CommonSocialConnectRequest(
                                user.id,
                                user,
                                this.authenticationService.getLoggedInUserName(),
                                socialProviderNames[socialProvider]
                            )
                        );
                    }
                }
            }
        };
    }

    /**
     * Process social login request with device information
     * (for region language and time targeting purpose)
     * @param socialRequest - target social provider login request
     */
    private sendSocialLoginRequest(socialRequest: SocialSignInRequest) {
        this.socialService
            .socialServiceLogin(socialRequest, DeviceInfoStorage.deviceInfo)
            .subscribe(
                this.handleSocialResponse(),
                this.handleSocialResponse()
            );
    }

    /**
     * Process social registration request with device information
     * (for region language and time targeting purpose)
     * @param socialRequest - target social provider registration request
     */
    private sendSocialSignUpRequest(socialRequest: SocialSignUpRequest) {
        this.socialService
            .socialServiceSignUp(socialRequest, DeviceInfoStorage.deviceInfo)
            .subscribe(
                this.handleSocialResponse(),
                this.handleSocialResponse()
            );
    }

    /**
     * Perform social connect request with device information
     * (for region language and time targeting purpose)
     * @param socialRequest - target social provider connect request
     */
    private sendSocialConnectRequest(socialRequest: SocialConnectRequest) {
        this.socialService
            .socialServiceConnect(socialRequest, DeviceInfoStorage.deviceInfo)
            .subscribe(
                this.handleSocialResponse(),
                this.handleSocialResponse()
            );
    }

    /**
     * Perform social connect request with device information
     * (for region language and time targeting purpose)
     * @param socialRequest - target social provider connect request
     */
    private sendSocialDisconnectRequest(socialRequest: SocialDisconnectRequest) {
        this.socialService
            .socialServiceDisconnect(socialRequest)
            .subscribe(
                this.handleSocialResponse(),
                this.handleSocialResponse()
            );
    }

    /**
     * Handle social login server response
     */
    private handleSocialResponse() {
        return (response: any) => {
            this.socialUserStorageService.socialServerResponse.next(response);
        };
    }

    /*private linkedInLogin() {
        this.ngxLinkedinService.signIn().subscribe((user: any) => {
            console.log('signIn', user);

            if (user.id) {
                this.sendSocialLoginRequest(new LinkedInAuthRequest(user.id));
            }
        });
    }*/

    /**
     * Prepare Microsoft social user login or registration request
     */
    private processMSCallback() {
        this.msAuthSubscription = this
            .getMsProfile()
            .subscribe((profile: any) => {
                if (this.msLoginAllowed) {
                    if (profile.id) {

                        if (this.accountAction === actions.login) {
                            this.sendSocialLoginRequest(
                                new MicrosoftAuthRequest(profile.id)
                            );
                        }

                        if (this.accountAction === actions.login) {
                            const user = this.createUserFromSocialUser(
                                profile,
                                socialProviderNames.ms
                            );

                            this.sendSocialSignUpRequest(
                                new MicrosoftSignUpRequest(
                                    user.id,
                                    user,
                                    socialProviderNames.microsoft
                                )
                            );
                        }
                    }
                }
            });
    }

    /**
     * Prepare Amazon social user login or registration request
     */
    private handleAmazonUser() {
        return (socialUser: any) => {
            if (socialUser != null) {

                let user;
                if (this.accountAction === actions.login) {
                    user = this.socialUserStorageService.createAmazonUser(
                        socialUser
                    );
                    this.sendSocialLoginRequest(new AmazonAuthRequest(user.id));
                }

                if (this.accountAction === actions.registration) {
                    user = this.socialUserStorageService.createAmazonUser(
                        socialUser
                    );

                    this.sendSocialSignUpRequest(
                        new AmazonSignUpRequest(
                            user.id,
                            user,
                            socialProviderNames.amazon
                        )
                    );
                }
            }
        };
    }

    /**
     * Prepare GitHub social user login request
     */
    private handleGitHubUser() {
        return (gitUser: any) => {
            if (gitUser != null) {
                if (gitUser.id) {
                    if (this.getGitOauthAction() === actions.login) {
                        this.sendSocialLoginRequest(
                            new GithubAuthRequest(gitUser.id)
                        );
                    }

                    if (this.getGitOauthAction() === actions.registration) {
                        const user = this.createUserFromSocialUser(
                            gitUser,
                            socialProviderNames.git
                        );

                        this.sendSocialSignUpRequest(
                            new GithubSignUpRequest(
                                user.id,
                                user,
                                socialProviderNames.github
                            )
                        );
                    }
                }
            }
        };
    }
}
