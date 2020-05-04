import { MsalModule } from "@azure/msal-angular";
import { Logger, LogLevel } from "msal";


export const protectedResourceMap: [string, string[]][] = [
    ['https://graph.microsoft.com/v1.0/me', ['user.read']]
];

export const isIE =
    window.navigator.userAgent.indexOf('MSIE ') > -1 ||
    window.navigator.userAgent.indexOf('Trident/') > -1;

function loggerCallback(logLevel, message, containsPii) {
    // console.log(message);
}

/**
 * Microsoft authentication library config
 */
export const MsalAppModule = MsalModule.forRoot(
    {
        auth: {
            clientId: 'f90657e8-cfae-450c-98b8-f595ca39a884',
            authority: 'https://login.microsoftonline.com/common/',
            validateAuthority: true,
            redirectUri: 'http://localhost:4200/account',
            postLogoutRedirectUri: 'http://localhost:4200/account',
            navigateToLoginRequestUrl: true
        },
        cache: {
            cacheLocation: 'localStorage',
            storeAuthStateInCookie: isIE // set to true for IE 11
        },
        system: {
            logger: new Logger(loggerCallback, {
                level: LogLevel.Verbose,
                piiLoggingEnabled: false,
                correlationId: '1234'
            })
        }
    },
    {
        popUp: !isIE,
        consentScopes: [
            "user.read",
            "openid",
            "profile",
            "api://a88bb933-319c-41b5-9f04-eff36d985612/access_as_user"
        ],
        unprotectedResources: ['https://www.microsoft.com/en-us/'],
        protectedResourceMap,
        extraQueryParameters: {}
    }
);
