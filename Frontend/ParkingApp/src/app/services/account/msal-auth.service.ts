import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {MsalService} from '@azure/msal-angular';
import {UserAgentApplication} from 'msal';


const GRAPH_ENDPOINT = 'https://graph.microsoft.com/v1.0/me';

@Injectable({
    providedIn: 'root'
})
export class MsalAuthService {

    profile;

    B2CTodoAccessTokenKey = 'b2c.access.token';

    tenantConfig = {
        // tenant: 'hellowrold.onmicrosoft.com',
        // Replace this with your client id
        clientID: 'f90657e8-cfae-450c-98b8-f595ca39a884',
        // signInPolicy: 'B2C_1_signin',
        // signUpPolicy: 'B2C_1_signup',
        redirectUri: 'http://localhost:4200',
        // b2cScopes: ['https://hellowrold.onmicrosoft.com/access-api/user_impersonation']
    };

    msalConfig = {
        auth: {
            clientId: 'f90657e8-cfae-450c-98b8-f595ca39a884',
            authority: 'https://login.microsoftonline.com/common/',
            validateAuthority: true,
            // redirectUri: environment.restUrl,
            // postLogoutRedirectUri: environment.restUrl,
            navigateToLoginRequestUrl: true,
        },
        cache: {
            // cacheLocation: 'localStorage',
            storeAuthStateInCookie: true, // set to true for IE 11
        },
        framework: {
            unprotectedResources: ['https://www.microsoft.com/en-us/'],
            // protectedResourceMap: new Map(protectedResourceMap)
        },
        system: {
            // logger: new Logger(loggerCallback, options)
        }
    };

    /*
     * B2C SignIn SignUp Policy Configuration
     */
    clientApplication = new UserAgentApplication(
        this.msalConfig
    );

    // Configure the authority for Azure AD B2C
    // authority = 'https://login.microsoftonline.com/tfp/' + this.tenantConfig.tenant + '/' + this.tenantConfig.signInPolicy;

    userData;

    userAgentApplication;

    constructor(private authService: MsalService, private http: HttpClient) {

        const applicationConfig = {
            auth: {
                clientId: 'f90657e8-cfae-450c-98b8-f595ca39a884',
                authority: 'https://login.microsoftonline.com/common/',
                validateAuthority: true,
                // redirectUri: environment.restUrl,
                // postLogoutRedirectUri: environment.restUrl,
                navigateToLoginRequestUrl: true,
            },
            cache: {
                // cacheLocation: 'localStorage',
                storeAuthStateInCookie: true, // set to true for IE 11
            },
            framework: {
                unprotectedResources: ['https://www.microsoft.com/en-us/'],
                // protectedResourceMap: new Map(protectedResourceMap)
            },
            system: {
                // logger: new Logger(loggerCallback, options)
            }
        };

        this.userAgentApplication = new UserAgentApplication(applicationConfig);
    }

    public login(): void {
        // this.clientApplication.authority = 'https://login.microsoftonline.com/tfp/' +
        //     this.tenantConfig.tenant + '/' + this.tenantConfig.signInPolicy;
        this.authenticate();
    }

    public signup(): void {
        // this.clientApplication.authority = 'https://login.microsoftonline.com/tfp/' +
        //     this.tenantConfig.tenant + '/' + this.tenantConfig.signUpPolicy;
        this.authenticate();
    }

    public authenticate(): void {
        // const _this = this;
        this.clientApplication.loginPopup().then((idToken: any) => {
            console.log('idToken' + idToken);
            this.clientApplication.acquireTokenSilent(this.tenantConfig).then(
                (accessToken: any) => {
                    console.log('accessToken' + accessToken);
                    this.saveAccessTokenToCache(accessToken);
                }, (error: any) => {
                    console.log('error' + error);
                    this.clientApplication.acquireTokenPopup(this.tenantConfig).then(
                        (accessToken: any) => {
                            console.log('accessToken' + accessToken);
                            this.saveAccessTokenToCache(accessToken);
                        }, (otherError: any) => {
                            console.log('error: ', otherError);
                        });
                })
        }, (error: any) => {
            console.log('error: ', error);
        });
    }

    saveAccessTokenToCache(accessToken: string): void {
        sessionStorage.setItem(this.B2CTodoAccessTokenKey, accessToken);
    }

    logout(): void {
        this.clientApplication.logout();
    }

    isLoggedIn(): boolean {
        return this.clientApplication.getAccount() != null;
    }

    getUserEmail(): string {
        return this.getUser().idToken.emails[0];
    }

    getUser() {
        return this.clientApplication.getAccount()
    }

    public tokenReceivedCallback(errorDesc, token, error, tokenType) {
        if (token) {
            this.userData = token;

            console.log('Token: ' + token)
        } else {
            console.log(error + ':' + errorDesc);
        }
    }

    public microsoftSignIn() {
        const graphScopes = {scopes: ['user.read']};

        this.userAgentApplication.loginPopup(graphScopes).then(idToken => {
            // Login Success
            console.log('idToken' + ':' + JSON.stringify(idToken));
            this.userAgentApplication.acquireTokenSilent(graphScopes).then(accessToken => {

                console.log('accessToken TOKENNNNNNNNNNN' + JSON.stringify(accessToken));
                // AcquireTokenSilent Success
                const headers = new Headers();
                const bearer = 'Bearer ' + accessToken.accessToken;
                headers.append('Authorization', bearer);
                headers.append('Content-type', 'application/json');
                headers.append('Accept', 'application/json');
                const options = {
                    method: 'POST',
                    headers,
                    body: JSON.stringify(accessToken)
                };
                const graphEndpoint = 'https://graph.microsoft.com/v1.0/me';

                fetch(graphEndpoint, options)
                    .then(response => {

                        response.json().then(data => {
                            this.userData = data;

                            console.log('DAATATATATAT: ' + JSON.stringify(data));
                        })
                    })
            }, error => {
                // AcquireTokenSilent Failure, send an interactive request.
                this.userAgentApplication.acquireTokenPopup(graphScopes).then(accessToken => {
                    // updateUI();
                }, (errorr) => {
                    console.log(errorr);
                });
            })
        }, (error) => {
            // login failure
            console.log(error);
        });

        const token = {
            uniqueId: '',
            tenantId: '',
            tokenType: 'access_token',
            idToken: {
                // tslint:disable-next-line:max-line-length
                // tslint:disable-next-line:max-line-length
                // tslint:disable-next-line:max-line-length
                // tslint:disable-next-line:max-line-length
                // tslint:disable-next-line:max-line-length
                rawIdToken: 'eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6IjFMVE16YWtpaGlSbGFfOHoyQkVKVlhlV01xbyJ9.eyJ2ZXIiOiIyLjAiLCJpc3MiOiJodHRwczovL2xvZ2luLm1pY3Jvc29mdG9ubGluZS5jb20vOTE4ODA0MGQtNmM2Ny00YzViLWIxMTItMzZhMzA0YjY2ZGFkL3YyLjAiLCJzdWIiOiJBQUFBQUFBQUFBQUFBQUFBQUFBQUFBMGdkNFJmS1VfUHZ6bjRJVGtWT2QwIiwiYXVkIjoiZjkwNjU3ZTgtY2ZhZS00NTBjLTk4YjgtZjU5NWNhMzlhODg0IiwiZXhwIjoxNTg1MTU1NjAzLCJpYXQiOjE1ODUwNjg5MDMsIm5iZiI6MTU4NTA2ODkwMywibmFtZSI6ItCQ0LvQtdC60YHQtdC5INCT0YPQu9GP0LoiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGVrczc5MDBAb3V0bG9vay5jb20iLCJvaWQiOiIwMDAwMDAwMC0wMDAwLTAwMDAtNjE4NC0yMDBhMzc4ZGUwYzMiLCJ0aWQiOiI5MTg4MDQwZC02YzY3LTRjNWItYjExMi0zNmEzMDRiNjZkYWQiLCJub25jZSI6Ijc4ZGI0YjM3LTM5YjQtNGUzNy1hYTA5LWMzNDQwMmY1Zjg2MCIsImFpbyI6IkRWaFltRzJzTGhlcEUwMGwhYlVHS3l0c21MVUdYMUVEVTl5NlJzSkNrRTN6TXlndSFXSWhoaGdYT1Q5eFlvNVBtOFdkSlUwQiFWYzlGVTlFNDczOW9OUHk3Q05pQlAyS1dPV2gzdDhOSnI1cE94cHNTOGFVSGJwbkN4MHRsMFlhbGd0bUpic2gqMmVoUEhha1pCQjN4aFUkIn0.hsDZO4YcOwnC62a9TgbtdJUMHo6P8pTjfC8mqJ153YtBK-G-D7Te1K7J5jCnRAvv-Ds0PXBYq-vJIma3jM6-1kwocBHQSyOE3d2JHPdoHI1wSPQEUgRSt0GlhMNO93rmzADfS44JMDHYZ8O2MHoJG6HXHa-2aAEK3k9GCjrCZ_UQB39bTy5_YzZXO8XDTNQOaC5D1AR8WfjWgQZPzVUXGNCZYgfUHWyCkI3YcSpRLw72MVEH7iw6xSp2ACKZlgM5-RPAarvsg-O_8-noWI-Nx_Ychsg0DIJzyM6wgQ_WLipt2_uGSowg-I8QdUPxuTev2bAhcq8dBVC09YYjSHqFow',
                claims: {
                    ver: '2.0',
                    iss: 'https://login.microsoftonline.com/9188040d-6c67-4c5b-b112-36a304b66dad/v2.0',
                    sub: 'AAAAAAAAAAAAAAAAAAAAAA0gd4RfKU_Pvzn4ITkVOd0',
                    aud: 'f90657e8-cfae-450c-98b8-f595ca39a884',
                    exp: 1585155603,
                    iat: 1585068903,
                    nbf: 1585068903,
                    name: 'Алексей Гуляк',
                    preferred_username: 'aleks7900@outlook.com',
                    oid: '00000000-0000-0000-6184-200a378de0c3',
                    tid: '9188040d-6c67-4c5b-b112-36a304b66dad',
                    nonce: '78db4b37-39b4-4e37-aa09-c34402f5f860',
                    aio: 'DVhYmG2sLhepE00l!bUGKytsmLUGX1EDU9y6RsJCkE3zMygu!WIhhhgXOT9xYo5Pm8WdJU0B!Vc9FU9E4739oNPy7CNiBP2KWOWh3t8NJr5pOxpsS8aUHbpnCx0tl0YalgtmJbsh*2ehPHakZBB3xhU$'
                },
                issuer: 'https://login.microsoftonline.com/9188040d-6c67-4c5b-b112-36a304b66dad/v2.0',
                objectId: '00000000-0000-0000-6184-200a378de0c3',
                subject: 'AAAAAAAAAAAAAAAAAAAAAA0gd4RfKU_Pvzn4ITkVOd0',
                tenantId: '9188040d-6c67-4c5b-b112-36a304b66dad',
                version: '2.0',
                preferredName: 'aleks7900@outlook.com',
                name: 'Алексей Гуляк',
                nonce: '78db4b37-39b4-4e37-aa09-c34402f5f860',
                expiration: 1585155603
            },
            idTokenClaims: {
                ver: '2.0',
                iss: 'https://login.microsoftonline.com/9188040d-6c67-4c5b-b112-36a304b66dad/v2.0',
                sub: 'AAAAAAAAAAAAAAAAAAAAAA0gd4RfKU_Pvzn4ITkVOd0',
                aud: 'f90657e8-cfae-450c-98b8-f595ca39a884',
                exp: 1585155603,
                iat: 1585068903,
                nbf: 1585068903,
                name: 'Алексей Гуляк',
                preferred_username: 'aleks7900@outlook.com',
                oid: '00000000-0000-0000-6184-200a378de0c3',
                tid: '9188040d-6c67-4c5b-b112-36a304b66dad',
                nonce: '78db4b37-39b4-4e37-aa09-c34402f5f860',
                aio: 'DVhYmG2sLhepE00l!bUGKytsmLUGX1EDU9y6RsJCkE3zMygu!WIhhhgXOT9xYo5Pm8WdJU0B!Vc9FU9E4739oNPy7CNiBP2KWOWh3t8NJr5pOxpsS8aUHbpnCx0tl0YalgtmJbsh*2ehPHakZBB3xhU$'
            },
            accessToken: 'EwCAA8l6BAAUO9chh8cJscQLmU+LSWpbnr0vmwwAAbCunu6WFvCXugrhIxnfNFitdUaUqVeFbvoNnr1TyX6HcoV1JRPnAUC1+s0P3RRUF0v9T6UMJqSUvx802CaTW9xcdVHZhD20kDW00TKCHqeK+YaQZBJmPIYIXuWvifnZZRaqYHd6GeZu1fsFYXUfnuThTpqe2P3kDSjLwFoknRNkkS1RbH/nY9QA7zFE6koNaS/LpPimPe8oZrGQpdV6fCWRN2DpgukSP2EmLaoky08iH/Qn9mBiscTS/D8X05JPvxEduXqB69mHVKBi/XFq2ZJVjSauTzp6s0HB2KC6EF8pLeQbBoXPYlzaDKtZ1FUozICq5bshpZZOoQgBEpVKf3oDZgAACCPsUUh0I7QmUAKMIVWcP7ZZdXDA/XwXg5kDFsHygO1aKMAflQoq7DOcNJbtrPoEV3heoAwfjKNoDzUX366yDYQqlDGdlUwNAR5MubG3LiQusyNaGeaOvaR9cMLlxmXwj3dYYi3pmlxcGjR4MgOlP6cBVQNMjPWGxnRmZjOsX/lZQ1MfWwckIeQCVnL8OUdXZo0pQh1tGgNYHF0VUYnpiLMGC2OzENARKfPVZQ76iJY63L0x3V8//Be/qkfjbtmIwWCYIUjrA2I7ir0dV//9rTdRgIDhmN6TU8C9nOpr/eV7Sz2XkidPIv8c+qh1KrXHNSlDeqGPAt33X2B9jLdMDAwZeKmVQMAjpO21tCif6m6Xv+P+jtrtI54d7+CJv8OvqDQO0dvHChLy4kV0BOWBaZZehcwOhbks/irfhinxWbDGWiG1gv5pJpeksPGdeOVsWfMq2+f/ZjLDU15XDd2RmhzCQZrxJFKrKWnE4j3W+mWhLnnbl723eq0PzRgclmC2K3+SqpG5fa3aBNC6n02MXA3qxcPR95IZjooFFeO5eqnOnYhreX2+KHeEg6MG9zNHWs7+cEACnb2A8q0NA8EWqcAHt/UdEGQ28oJpNJhLxqQr2iRGZQul2RzmnGe5Ye0V5i3dXugAOgRNFelML54tumhm2P+bHlKrDgmtlp3wSmha+ot2NhB/RmiLbfgl5Pl4HFwuhyXQHncvult2HRC7zBhpLpMu9pUgjwqhCX36OgTIXDa3lLX+c3vndicS7lGjMq7KXeX8IsuizVTwDcsPZyKXNdTYm5kOKYpTiAI=',
            scopes: ['User.Read', 'openid', 'profile'],
            expiresOn: '2020-03-24T18:00:06.000Z',
            account: {
                accountIdentifier: '00000000-0000-0000-6184-200a378de0c3',
                // tslint:disable-next-line:max-line-length
                homeAccountIdentifier: 'MDAwMDAwMDAtMDAwMC0wMDAwLTYxODQtMjAwYTM3OGRlMGMz.OTE4ODA0MGQtNmM2Ny00YzViLWIxMTItMzZhMzA0YjY2ZGFk',
                userName: 'aleks7900@outlook.com',
                name: 'Алексей Гуляк',
                idToken: {
                    ver: '2.0',
                    iss: 'https://login.microsoftonline.com/9188040d-6c67-4c5b-b112-36a304b66dad/v2.0',
                    sub: 'AAAAAAAAAAAAAAAAAAAAAA0gd4RfKU_Pvzn4ITkVOd0',
                    aud: 'f90657e8-cfae-450c-98b8-f595ca39a884',
                    exp: 1585157287,
                    iat: 1585070587,
                    nbf: 1585070587,
                    name: 'Алексей Гуляк',
                    preferred_username: 'aleks7900@outlook.com',
                    oid: '00000000-0000-0000-6184-200a378de0c3',
                    tid: '9188040d-6c67-4c5b-b112-36a304b66dad',
                    nonce: '29d2b8d6-641b-4962-8349-d82e235698d0',
                    aio: 'DefJQseKYY2Qj7qR0*HDkdsabQtNFrwZzOB4HtkRhe9oHmgdA9cs7osFUuht4*LiQPe848svj!ZxVMPzMALj96ebOLrlzvn102IOZKDc5BOxI2mNg9N*20qxxif413!rgr2rWOP2du8rljcQV2lnSLA$'
                },
                idTokenClaims: {
                    ver: '2.0',
                    iss: 'https://login.microsoftonline.com/9188040d-6c67-4c5b-b112-36a304b66dad/v2.0',
                    sub: 'AAAAAAAAAAAAAAAAAAAAAA0gd4RfKU_Pvzn4ITkVOd0',
                    aud: 'f90657e8-cfae-450c-98b8-f595ca39a884',
                    exp: 1585157287,
                    iat: 1585070587,
                    nbf: 1585070587,
                    name: 'Алексей Гуляк',
                    preferred_username: 'aleks7900@outlook.com',
                    oid: '00000000-0000-0000-6184-200a378de0c3',
                    tid: '9188040d-6c67-4c5b-b112-36a304b66dad',
                    nonce: '29d2b8d6-641b-4962-8349-d82e235698d0',
                    aio: 'DefJQseKYY2Qj7qR0*HDkdsabQtNFrwZzOB4HtkRhe9oHmgdA9cs7osFUuht4*LiQPe848svj!ZxVMPzMALj96ebOLrlzvn102IOZKDc5BOxI2mNg9N*20qxxif413!rgr2rWOP2du8rljcQV2lnSLA$'
                },
                environment: 'https://login.microsoftonline.com/9188040d-6c67-4c5b-b112-36a304b66dad/v2.0'
            },
            accountState: 'f92abd42-1302-4769-94da-4c974a2d82f0',
            fromCache: true
        }
    }


}
