import { async, ComponentFixture, TestBed } from "@angular/core/testing";

import { SocialConnectionModalComponent } from "./social-connection-modal.component";


describe("SocialConnectionModalComponent", () => {
    let component: SocialConnectionModalComponent;
    let fixture: ComponentFixture<SocialConnectionModalComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [SocialConnectionModalComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(SocialConnectionModalComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it("should create", () => {
        expect(component).toBeTruthy();
    });
});
