import {Injectable} from '@angular/core';
import {User} from '@app/models/User';
import {storageKeys} from '@app/services/navigation/app.endpoints';
import {BehaviorSubject, Subject} from 'rxjs';


@Injectable({
    providedIn: 'root',
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
        user.firstname = this.getFirstname(socialUser.name);
        user.lastname = socialUser.lastName;
        return user;
    }

    createGoogleUser(socialUser: any): User {
        const user = new User();
        user.id = socialUser.id;
        user.email = socialUser.email;
        user.firstname = this.getFirstname(socialUser.name);
        user.lastname = socialUser.lastName;
        return user;
    }

    createVkUser(socialUser: any): User {
        const user = new User();
        user.id = socialUser.id;
        user.firstname = this.getFirstname(socialUser.name);
        user.lastname = socialUser.lastName;
        return user;
    }

    createGitUser(socialUser: any): User {
        const user = new User();
        user.id = socialUser.id;
        user.username = socialUser.login;
        user.email = socialUser.email;
        user.firstname = this.getFirstname(socialUser.name);
        return user;
    }

    createMSUser(socialUser: any): User {
        const user = new User();
        user.id = socialUser.id;
        user.email = socialUser.userPrincipalName;
        user.firstname = this.getFirstname(socialUser.displayName);
        user.lastname = socialUser.surname;
        return user;
    }

    createAmazonUser(socialUser: any): User {
        const user = new User();
        user.id = socialUser.CustomerId;
        user.email = socialUser.PrimaryEmail;
        user.firstname = this.getFirstname(socialUser.Name);
        return user;
    }

    cleanGitAuth() {
        sessionStorage.removeItem(storageKeys.gitCode);
        sessionStorage.removeItem(storageKeys.action);
    }

    setGitOauthCode(code: string) {
        sessionStorage.setItem(storageKeys.gitCode, code);
    }

    getGitOauthCode() {
        return sessionStorage.getItem(storageKeys.gitCode);
    }

    setGitOauthAction(action: string) {
        sessionStorage.setItem(storageKeys.action, action);
    }

    getGitOauthAction() {
        return sessionStorage.getItem(storageKeys.action);
    }

    private getFirstname(fullname: string) {
        return fullname.split(' ')[0];
    }
}
