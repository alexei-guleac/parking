import {TestBed} from '@angular/core/testing';

import {SocialAccountService} from './social-account.service';


describe('SocialAccountService', () => {
    beforeEach(() => TestBed.configureTestingModule({}));

    it('should be created', () => {
        const service: SocialAccountService = TestBed.get(SocialAccountService);
        expect(service).toBeTruthy();
    });
});
