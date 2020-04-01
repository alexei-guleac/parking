import {providerNames} from '../../services/account/social/social-account.service';
import {User} from '../User';


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

    socialProvider = providerNames.github;

    constructor(id: string) {
        this.id = id;
    }
}

export class AmazonAuthRequest implements SocialSignInRequest {
    id: string;

    socialProvider = providerNames.amazon;

    constructor(id: string) {
        this.id = id;
    }
}

export class MicrosoftAuthRequest implements SocialSignInRequest {
    id: string;

    socialProvider = providerNames.microsoft;

    constructor(id: string) {
        this.id = id;
    }
}

export interface SocialSignUpRequest {
    id: string;

    user: User;

    socialProvider: string;
}

export class CommonSocialSignUpRequest implements SocialSignUpRequest {
    id: string;

    user: User;

    socialProvider: string;

    constructor(id: string, user: User, socialProvider: string) {
        this.id = id;
        this.user = user;
        this.socialProvider = socialProvider;
    }
}

export class GithubSignUpRequest extends GithubAuthRequest {
    user: User;

    constructor(id: string, user: User, socialProvider: string) {
        super(id);
        this.id = id;
        this.user = user;
        this.socialProvider = socialProvider;
    }
}

export class MicrosoftSignUpRequest extends MicrosoftAuthRequest {
    user: User;

    constructor(id: string, user: User, socialProvider: string) {
        super(id);
        this.id = id;
        this.user = user;
        this.socialProvider = socialProvider;
    }
}

export class AmazonSignUpRequest extends AmazonAuthRequest {
    user: User;

    constructor(id: string, user: User, socialProvider: string) {
        super(id);
        this.id = id;
        this.user = user;
        this.socialProvider = socialProvider;
    }
}
