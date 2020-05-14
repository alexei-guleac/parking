import { TestBed } from '@angular/core/testing';

import { DirectAccessGuard } from './direct-access-guard.service';


describe('DirectAccessGuardService', () => {
    beforeEach(() => TestBed.configureTestingModule({}));

    it('should be created', () => {
        const service: DirectAccessGuard = TestBed.get(DirectAccessGuard);
        expect(service).toBeTruthy();
    });
});
