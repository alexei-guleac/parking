import {TestBed} from '@angular/core/testing';

import {ResetGuard} from './reset-guard.service';


describe('ResetGuard', () => {
    beforeEach(() => TestBed.configureTestingModule({}));

    it('should be created', () => {
        const service: ResetGuard = TestBed.get(ResetGuard);
        expect(service).toBeTruthy();
    });
});
