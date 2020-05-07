import { TestBed } from "@angular/core/testing";

import { SocialProviderService } from "./social-provider.service";


describe("SocialProviderService", () => {
    beforeEach(() => TestBed.configureTestingModule({}));

    it("should be created", () => {
        const service: SocialProviderService = TestBed.get(SocialProviderService);
        expect(service).toBeTruthy();
    });
});
