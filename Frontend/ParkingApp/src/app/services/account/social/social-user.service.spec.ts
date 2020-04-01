import {TestBed} from '@angular/core/testing';

import {SocialUserService} from './social-user.service';


describe('SocialUserService', () => {
    beforeEach(() => TestBed.configureTestingModule({}));

    it('should be created', () => {
        const service: SocialUserService = TestBed.get(SocialUserService);
        expect(service).toBeTruthy();
    });
});
