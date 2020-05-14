import { TestBed } from '@angular/core/testing';

import { SocialUserStorageService } from './social-user-storage.service';


describe('SocialUserStorageService', () => {
    beforeEach(() => TestBed.configureTestingModule({}));

    it('should be created', () => {
        const service: SocialUserStorageService = TestBed.get(SocialUserStorageService);
        expect(service).toBeTruthy();
    });
});
