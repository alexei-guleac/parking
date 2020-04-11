import {Injectable} from '@angular/core';
import {JWTToken} from '@app/models/payload/JWTToken';
import {JwtHelperService} from '@auth0/angular-jwt';


@Injectable({
    providedIn: 'root',
})
export class AccountStorageService {
    private storeType: string = storageType.local;

    public getType(): string {
        return this.storeType;
    }

    public setType(value: string) {
        this.storeType = value;
    }
}

export const storageKeys = {
    USER_NAME_SESSION_ATTRIBUTE_NAME: 'authenticatedUser',
    TOKEN_NAME: 'token',
};

export const storageType = {
    local: 'local',
    session: 'session',
};

export const roleAdmin = 'ROLE_ADMIN';

@Injectable({
    providedIn: 'root',
})
export class SessionStorageService {
    constructor(
        private jwtHelper: JwtHelperService,
        private accountStorageTypeService: AccountStorageService
    ) {
    }

    localStoreCredentials(username: string, password: string) {
        this.clearLocalStorage();
        localStorage.setItem(
            storageKeys.USER_NAME_SESSION_ATTRIBUTE_NAME,
            username
        );
        localStorage.setItem(
            storageKeys.TOKEN_NAME,
            btoa(username + ':' + password)
        );
        this.setLocalStorageType();
    }

    sessionStoreCredentials(username: string, password: string) {
        this.clearSessionStorage();
        sessionStorage.setItem(
            storageKeys.USER_NAME_SESSION_ATTRIBUTE_NAME,
            username
        );
        sessionStorage.setItem(
            storageKeys.TOKEN_NAME,
            btoa(username + ':' + password)
        );
        this.setSessionStorageType();
    }

    localStoreToken(username, token) {
        this.clearLocalStorage();
        localStorage.setItem(
            storageKeys.USER_NAME_SESSION_ATTRIBUTE_NAME,
            username
        );
        localStorage.setItem(storageKeys.TOKEN_NAME, token);
        this.setLocalStorageType();
    }

    sessionStoreToken(username, token) {
        this.clearSessionStorage();
        sessionStorage.setItem(
            storageKeys.USER_NAME_SESSION_ATTRIBUTE_NAME,
            username
        );
        sessionStorage.setItem(storageKeys.TOKEN_NAME, token);
        this.setSessionStorageType();
    }

    clearAccountStorage() {
        this.clearSessionStorage();
        this.clearLocalStorage();
    }

    clearLocalStorage() {
        localStorage.removeItem(storageKeys.USER_NAME_SESSION_ATTRIBUTE_NAME);
        localStorage.removeItem(storageKeys.TOKEN_NAME);
    }

    clearSessionStorage() {
        sessionStorage.removeItem(storageKeys.USER_NAME_SESSION_ATTRIBUTE_NAME);
        sessionStorage.removeItem(storageKeys.TOKEN_NAME);
        window.sessionStorage.clear();
    }

    clearCacheStorage() {
        window.caches.keys().then((keyList) =>
            Promise.all(
                keyList.map((key) => {
                    return caches.delete(key);
                })
            )
        );
    }

    setLocalStorageType() {
        this.accountStorageTypeService.setType(storageType.local);
    }

    setSessionStorageType() {
        this.accountStorageTypeService.setType(storageType.session);
    }

    isJwtTokenExpired() {
        const token = getJwtToken();

        const decodedToken = this.jwtHelper.decodeToken(token);
        console.log(`decodedToken ` + JSON.stringify(decodedToken));
        const expirationDate = this.jwtHelper.getTokenExpirationDate(token);
        console.log(`expirationDate ${expirationDate}`);
        const isExpired = this.jwtHelper.isTokenExpired(token);
        console.log(`isExpired ${isExpired}`);

        return isExpired;
    }

    getUserRolesFromJwt() {
        const token = getJwtToken();

        if (token) {
            const decodedToken = this.jwtHelper.decodeToken(token) as JWTToken;
            // console.log(`decodedToken ` + JSON.stringify(decodedToken));

            return decodedToken.roles;
        } else {
            return '';
        }
    }
}

export function getJwtToken() {
    return (
        localStorage.getItem(storageKeys.TOKEN_NAME) ||
        sessionStorage.getItem(storageKeys.TOKEN_NAME) ||
        null
    );
}

export function getUsername(storeType: string) {
    if (storeType === storageType.local) {
        return localStorage.getItem(
            storageKeys.USER_NAME_SESSION_ATTRIBUTE_NAME
        );
    }
    if (storeType === storageType.session) {
        return sessionStorage.getItem(
            storageKeys.USER_NAME_SESSION_ATTRIBUTE_NAME
        );
    }
}

export function setUsername(newUsername: string, storeType: string) {
    if (storeType === storageType.local) {
        return localStorage.setItem(
            storageKeys.USER_NAME_SESSION_ATTRIBUTE_NAME,
            newUsername
        );
    }
    if (storeType === storageType.session) {
        return sessionStorage.setItem(
            storageKeys.USER_NAME_SESSION_ATTRIBUTE_NAME,
            newUsername
        );
    }
}
