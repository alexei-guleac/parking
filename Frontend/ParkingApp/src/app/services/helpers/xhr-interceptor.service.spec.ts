import {TestBed} from '@angular/core/testing';

import {XhrInterceptor} from './xhr-interceptor.service';


describe('XhrInterceptorService', () => {
    beforeEach(() => TestBed.configureTestingModule({}));

    it('should be created', () => {
        const service: XhrInterceptor = TestBed.get(XhrInterceptor);
        expect(service).toBeTruthy();
    });
});
