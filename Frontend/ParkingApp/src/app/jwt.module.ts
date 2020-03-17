import {JwtModule, JwtModuleOptions} from '@auth0/angular-jwt';
import {getJwtToken} from './services/account/session-storage.service';


const jwtModuleOptions: JwtModuleOptions = {
    config: {
        tokenGetter: () => {
            return getJwtToken();
        },
        whitelistedDomains: []
    }
};

export const JwtAppModule = JwtModule.forRoot(jwtModuleOptions);
