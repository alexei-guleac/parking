import { socialProviderNames } from "@app/services/account/social/social-user-storage.service";
import { User } from "../User";


/**
 * Different social service provider requests (sign in, sign up, connect/disconnect)
 */
export interface SocialSignInRequest {
    id: string;

    socialProvider: string;
}

export class CommonSocialAuthRequest implements SocialSignInRequest {
    id: string;

    socialProvider: string;

    constructor(id: string, socialProvider: string) {
        this.id = id;
        this.socialProvider = socialProvider;
    }
}

export class GithubAuthRequest implements SocialSignInRequest {
    id: string;

    socialProvider = socialProviderNames.github;

    constructor(id: string) {
        this.id = id;
    }
}

export class AmazonAuthRequest implements SocialSignInRequest {
    id: string;

    socialProvider = socialProviderNames.amazon;

    constructor(id: string) {
        this.id = id;
    }
}

export class MicrosoftAuthRequest implements SocialSignInRequest {
    id: string;

    socialProvider = socialProviderNames.microsoft;

    constructor(id: string) {
        this.id = id;
    }
}

export interface SocialSignUpRequest extends SocialSignInRequest {
    user: User;
}

export class CommonSocialSignUpRequest extends CommonSocialAuthRequest {
    user: User;

    constructor(id: string, user: User, socialProvider: string) {
        super(id, socialProvider);
        this.user = user;
    }
}

export class GithubSignUpRequest extends GithubAuthRequest {
    user: User;

    constructor(id: string, user: User, socialProvider: string) {
        super(id);
        this.user = user;
        this.socialProvider = socialProvider;
    }
}

export class MicrosoftSignUpRequest extends MicrosoftAuthRequest {
    user: User;

    constructor(id: string, user: User, socialProvider: string) {
        super(id);
        this.user = user;
        this.socialProvider = socialProvider;
    }
}

export class AmazonSignUpRequest extends AmazonAuthRequest {
    user: User;

    constructor(id: string, user: User, socialProvider: string) {
        super(id);
        this.user = user;
        this.socialProvider = socialProvider;
    }
}

export interface SocialConnectRequest {
    id: string;

    username: string;

    user: User;

    socialProvider: string;
}

export class CommonSocialConnectRequest extends CommonSocialSignUpRequest {
    username: string;

    constructor(id: string, user: User, username: string, socialProvider: string) {
        super(id, user, socialProvider);
        this.username = username;
    }
}

export interface SocialDisconnectRequest {

    username: string;

    socialProvider: string;
}

export class CommonSocialDisconnectRequest {
    username: string;

    socialProvider: string;

    constructor(username: string, socialProvider: string) {
        this.username = username;
        this.socialProvider = socialProvider;
    }
}
