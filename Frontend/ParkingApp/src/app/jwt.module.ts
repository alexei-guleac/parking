import { JwtModule, JwtModuleOptions } from "@auth0/angular-jwt";
import { getJwtToken } from "./services/account/account-session-storage.service";


/**
 * JWT module config
 */
const jwtModuleOptions: JwtModuleOptions = {
    config: {
        tokenGetter: () => {
            return getJwtToken();
        },
        whitelistedDomains: []
    }
};

export const JwtAppModule = JwtModule.forRoot(jwtModuleOptions);
