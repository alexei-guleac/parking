import { AfterViewChecked, Component, QueryList, ViewChildren } from "@angular/core";
import { ComponentWithErrorMsg } from "@app/components/account/forms/account-form/account-form.component";
import { SocialToggleComponent } from "@app/components/account/modals/social-connection-modal/social-toogle/social-toggle.component";
import { SocialAccountService } from "@app/services/account/social/social-account.service";
import { SocialProviderService } from "@app/services/account/social/social-provider.service";
import {
    socialProviderNames,
    SocialUserStorageService,
    usedSocialProviders
} from "@app/services/account/social/social-user-storage.service";
import { handleHttpErrorResponse } from "@app/services/helpers/global-http-interceptor-service.service";
import { actions } from "@app/services/navigation/app.endpoints";

import { capitalize } from "@app/utils/string-utils";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { Subject } from "rxjs";


/**
 * Social providers account connection modal window
 */
@Component({
    selector: "app-social-connection-modal",
    templateUrl: "./social-connection-modal.component.html",
    styleUrls: ["./social-connection-modal.component.scss"]
})
export class SocialConnectionModalComponent
    implements AfterViewChecked, ComponentWithErrorMsg {

    // subject for notifying parent component that all children components are loaded
    loaded: Subject<any> = new Subject<any>();

    usedSocialProviders = usedSocialProviders;

    errorMessage: string;

    invalidConnect = false;

    private socialProvider: any;

    private socialProviders = socialProviderNames;

    private socialLogoPath = `../../../../../../assets/img/socials-logo/`;

    @ViewChildren(SocialToggleComponent)
    private socialToggleComponentChildren: QueryList<SocialToggleComponent>;

    constructor(public activeModal: NgbActiveModal,
                private socialService: SocialProviderService,
                private socialAccountService: SocialAccountService,
                private socialUserStorageService: SocialUserStorageService) {
    }

    /**
     * Uppercase first string letter method
     * @param socialName - target name
     */
    capitalize(socialName: string) {
        return capitalize(socialName);
    }

    /**
     * Called after the default change detector has completed checking a component's view for changes
     */
    ngAfterViewChecked(): void {
        // assert that all children components are loaded
        this.loaded.next(true);
    }

    /**
     * Handles social connection with specified social provider
     * (simplified way with common library 'angularx-social-login-vk')
     * and with other specified social providers
     * (full Oauth 2.0 flow or other separate social provider specific libraries)
     */
    socialConnect(socialProvider) {
        this.socialProvider = socialProvider;

        // disable pressed button until a response comes from the server
        this.disablePressedToggle();
        // in advance subscribes to subject when server authentication response arrives
        this.subscribeToSocialActionResponse();
        this.socialService.processSocialLogin(this.socialProvider, actions.connectSocial);
    }

    /**
     * Handles social disconnection from specified social provider
     */
    socialDisconnect(socialProvider) {
        this.socialProvider = socialProvider;

        // disable pressed button until a response comes from the server
        this.disablePressedToggle();
        // in advance subscribes to subject when server authentication response arrives
        this.subscribeToSocialActionResponse();
        this.socialService.processSocialDisconnect(socialProvider);
    }

    private subscribeToSocialActionResponse() {
        this.socialUserStorageService.clearSocialResponse();
        this.socialUserStorageService.socialServerResponse.subscribe(
            this.handleSocialResponse()
        );
    }

    /**
     * Disable checkbox after press to prevent abuse
     */
    private disablePressedToggle() {
        this.socialToggleComponentChildren.forEach(
            (child) => {
                if (child.socialProvider === this.socialProvider) {
                    child.enabled = false;
                }
            });
    }

    /**
     * Sign in with specified social provider
     * (simplified way with common library 'angularx-social-login-vk')
     * and with other specified social providers
     * (full Oauth 2.0 flow or other separate social provider specific libraries)
     */
    private handleSocialResponse() {
        return (response: any) => {
            if (response.success) {
                this.handleSocialConnectResponse(response);
            }
            if (response.error) {
                this.handleSocialLoginError(response);
            }
        };
    }

    /**
     * Handle social connect server response
     */
    private handleSocialConnectResponse(response: any) {
        if (response.success) {
            this.invalidConnect = false;

            if (response.connect) {
                this.socialToggleComponentChildren.forEach(
                    (child) => {
                        if (child.socialProvider === this.socialProviders[this.socialProvider]) {
                            child.checked = true;
                            child.enabled = false;
                            // allow repeat connect attempt
                            child.clicked = false;
                        }
                    });
            }
            if (response.disconnect) {
                this.socialToggleComponentChildren.forEach(
                    (child) => {
                        if (child.socialProvider === this.socialProviders[this.socialProvider]) {

                            // allow repeat connect attempt
                            child.clicked = false;

                            setTimeout(() => {
                                // child.checked = true;
                                // child.checked = false;
                                child.enabled = false;
                            }, 150);
                        }
                    });

                let countConnected = 0;
                let provider;
                this.socialToggleComponentChildren.forEach(
                    (child) => {
                        if (child.checked === true) {
                            countConnected++;
                            provider = child.socialProvider;
                            console.log(provider);
                        }
                    });
                // if remains only one social connect keep it for account save reasons
                console.log(countConnected);
                if (countConnected === 1) {
                    this.socialToggleComponentChildren.forEach(
                        (child) => {
                            if (child.socialProvider === this.socialProviders[this.socialProvider]) {
                                child.enabled = false;
                            }
                        });
                }
            }

            // remove temporary Github auth data
            this.socialService.cleanGitAuth();
        }
    }

    /**
     * Handle server response error
     */
    private handleSocialLoginError(error) {
        this.invalidConnect = true;
        alert("Social connection failed.");
        handleHttpErrorResponse(error, this);

        this.socialToggleComponentChildren.forEach(
            (child) => {
                if (child.socialProvider === this.socialProviders[this.socialProvider]) {
                    child.checked = null;
                    child.enabled = true;

                    // allow repeat connect attempt
                    child.clicked = false;
                    // setTimeout(() => child.checked = null, 200);
                }
            });

        // remove temporary Github auth data
        this.socialService.cleanGitAuth();
    }
}
