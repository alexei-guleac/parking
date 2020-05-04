import { TestBed } from "@angular/core/testing";

import { AccountSessionStorageService } from "./account-session-storage.service";


describe('SessionStorageService', () => {
    beforeEach(() => TestBed.configureTestingModule({}));

    it('should be created', () => {
        const service: AccountSessionStorageService = TestBed.get(
            AccountSessionStorageService
        );
        expect(service).toBeTruthy();
    });
});
