import {Injectable} from '@angular/core';
import {BehaviorSubject, Subject} from 'rxjs';
import {User} from '../../../models/User';


@Injectable({
    providedIn: 'root'
})
export class SocialUserService {

    socialUser: Subject<any> = new BehaviorSubject<any>(null);

    gitUser: Subject<any> = new BehaviorSubject<any>(null);

    constructor() {
    }

    clearSocialUser() {
        this.socialUser = new BehaviorSubject<any>(null);
    }

    createFbUser(socialUser: any): User {
        const user = new User();
        user.id = socialUser.id;
        user.email = socialUser.email;
        user.fullname = socialUser.name;
        user.lastname = socialUser.lastName;
        return user;
    }

    createGoogleUser(socialUser: any): User {
        const user = new User();
        user.id = socialUser.id;
        user.email = socialUser.email;
        user.fullname = socialUser.name;
        user.lastname = socialUser.lastName;
        return user;
    }

    createVkUser(socialUser: any): User {
        const user = new User();
        user.id = socialUser.id;
        user.fullname = socialUser.name;
        user.lastname = socialUser.lastName;
        return user;
    }

    createGitUser(socialUser: any): User {
        const user = new User();
        user.id = socialUser.id;
        user.username = socialUser.login;
        user.email = socialUser.email;
        user.fullname = socialUser.name;
        return user;
    }

    createMSUser(socialUser: any): User {
        const user = new User();
        user.id = socialUser.id;
        user.email = socialUser.userPrincipalName;
        user.fullname = socialUser.displayName;
        user.lastname = socialUser.surname;
        return user;
    }

    createAmazonUser(socialUser: any): User {
        const user = new User();
        user.id = socialUser.CustomerId;
        user.email = socialUser.PrimaryEmail;
        user.fullname = socialUser.Name;
        return user;
    }

    cleanGitAuth() {
        sessionStorage.removeItem('git_oauth_code');
        sessionStorage.removeItem('action');
    }

    setGitOauthCode(code: string) {
        sessionStorage.setItem('git_oauth_code', code)
    }

    getGitOauthCode() {
        return sessionStorage.getItem('git_oauth_code')
    }

    setGitOauthAction(action: string) {
        sessionStorage.setItem('action', action)
    }

    getGitOauthAction() {
        return sessionStorage.getItem('action')
    }
}
