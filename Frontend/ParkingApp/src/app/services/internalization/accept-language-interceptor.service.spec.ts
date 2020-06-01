import { TestBed } from '@angular/core/testing';

import { AcceptLanguageInterceptorService } from './accept-language-interceptor.service';


describe('AcceptLanguageInterceptorService', () => {
    beforeEach(() => TestBed.configureTestingModule({}));

    it('should be created', () => {
        const service: AcceptLanguageInterceptorService = TestBed.get(AcceptLanguageInterceptorService);
        expect(service).toBeTruthy();
    });
});
