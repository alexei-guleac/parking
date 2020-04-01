import {TestBed} from '@angular/core/testing';

import {DirectAccessGuardService} from './direct-access-guard.service';


describe('DirectAccessGuardService', () => {
    beforeEach(() => TestBed.configureTestingModule({}));

    it('should be created', () => {
        const service: DirectAccessGuardService = TestBed.get(
            DirectAccessGuardService
        );
        expect(service).toBeTruthy();
    });
});
