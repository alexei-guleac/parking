import {TestBed} from '@angular/core/testing';

import {GlobalHttpErrorInterceptorService} from './global-http-interceptor-service.service';


describe('GlobalHttpInterceptorServiceService', () => {
    beforeEach(() => TestBed.configureTestingModule({}));

    it('should be created', () => {
        const service: GlobalHttpErrorInterceptorService = TestBed.get(
            GlobalHttpErrorInterceptorService
        );
        expect(service).toBeTruthy();
    });
});
