import { TestBed } from '@angular/core/testing';

import { GithubOauthService } from './github-oauth.service';


describe('GithubOauthService', () => {
    beforeEach(() => TestBed.configureTestingModule({}));

    it('should be created', () => {
        const service: GithubOauthService = TestBed.get(GithubOauthService);
        expect(service).toBeTruthy();
    });
});
