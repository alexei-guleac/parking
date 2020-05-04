import { TestBed } from "@angular/core/testing";

import { ObjectsSortService } from "./objects-sort.service";


describe("ObjectsSortService", () => {
    beforeEach(() => TestBed.configureTestingModule({}));

    it("should be created", () => {
        const service: ObjectsSortService = TestBed.get(ObjectsSortService);
        expect(service).toBeTruthy();
    });
});
