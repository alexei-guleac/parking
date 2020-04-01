import {TestBed} from '@angular/core/testing';

import {MsalAuthService} from './msal-auth.service';


describe('MsalService', () => {
    beforeEach(() => TestBed.configureTestingModule({}));

    it('should be created', () => {
        const service: MsalAuthService = TestBed.get(MsalAuthService);
        expect(service).toBeTruthy();
    });
});
