import { Injectable } from "@angular/core";
import { JWTToken } from "@app/models/payload/JWTToken";
import { JwtHelperService } from "@auth0/angular-jwt";


/**
 * User auth information storage type
 */
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

/**
 * Browser storage user auth related keys
 */
export const storageKeys = {
    USER_NAME_SESSION_ATTRIBUTE_NAME: 'authenticatedUser',
    TOKEN_NAME: 'token',
};

/**
 * Browser storage user auth related type
 */
export const storageType = {
    local: 'local',
    session: 'session',
};

export const roleAdmin = 'ROLE_ADMIN';

/**
 * Service for interacting with browser storage in due to logged user information processing
 */
@Injectable({
    providedIn: 'root',
})
export class AccountSessionStorageService {
    constructor(
        private jwtHelper: JwtHelperService,
        private accountStorageTypeService: AccountStorageService
    ) {
    }

    /**
     * Save user login information in local browser storage
     * @param username - user username
     * @param token - JWT from server with related user data
     */
    localStoreToken(username, token) {
        this.clearLocalStorage();
        localStorage.setItem(
            storageKeys.USER_NAME_SESSION_ATTRIBUTE_NAME,
            username
        );
        localStorage.setItem(storageKeys.TOKEN_NAME, token);
        this.setLocalStorageType();
    }

    /**
     * Save user login information in session browser storage
     * @param username - user username
     * @param token - JWT from server with related user data
     */
    sessionStoreToken(username, token) {
        this.clearSessionStorage();
        sessionStorage.setItem(
            storageKeys.USER_NAME_SESSION_ATTRIBUTE_NAME,
            username
        );
        sessionStorage.setItem(storageKeys.TOKEN_NAME, token);
        this.setSessionStorageType();
    }

    /**
     * Clear all user login related data
     */
    clearAccountStorage() {
        this.clearSessionStorage();
        this.clearLocalStorage();
    }

    /**
     * Clear all user login related data from local storage
     */
    clearLocalStorage() {
        localStorage.removeItem(storageKeys.USER_NAME_SESSION_ATTRIBUTE_NAME);
        localStorage.removeItem(storageKeys.TOKEN_NAME);
    }

    /**
     * Clear all user login related data from session storage
     */
    clearSessionStorage() {
        sessionStorage.removeItem(storageKeys.USER_NAME_SESSION_ATTRIBUTE_NAME);
        sessionStorage.removeItem(storageKeys.TOKEN_NAME);
        window.sessionStorage.clear();
    }

    /**
     * Clear browser cache storage
     */
    clearCacheStorage() {
        window.caches.keys().then((keyList) =>
            Promise.all(
                keyList.map((key) => {
                    return caches.delete(key);
                })
            )
        );
    }

    /**
     * Set user account storage type to local
     */
    setLocalStorageType() {
        this.accountStorageTypeService.setType(storageType.local);
    }

    /**
     * Set user account storage type to session
     */
    setSessionStorageType() {
        this.accountStorageTypeService.setType(storageType.session);
    }

    /**
     * Check if JWT is expired
     */
    isJwtTokenExpired() {
        const token = getJwtToken();
        return this.jwtHelper.isTokenExpired(token);
    }

    /**
     * Retrieve user roles from JWT
     */
    getUserRolesFromJwt() {
        const token = getJwtToken();

        if (token) {
            const decodedToken = this.jwtHelper.decodeToken(token) as JWTToken;
            return decodedToken.roles;
        } else {
            return '';
        }
    }
}

/**
 * Get JWT from browser storage
 */
export function getJwtToken() {
    return (
        localStorage.getItem(storageKeys.TOKEN_NAME) ||
        sessionStorage.getItem(storageKeys.TOKEN_NAME) ||
        null
    );
}

/**
 * Get logged in username from browser storage
 * @param storeType - browser taget storage type
 */
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

/**
 * Set logged in username in browser storage
 * @param newUsername - new user username
 * @param storeType - browser target storage type
 */
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
