import { async, ComponentFixture, TestBed } from "@angular/core/testing";

import { AccountEditModalFormComponent } from "./account-edit-modal-form.component";


describe("AccountEditModalContentComponent", () => {
    let component: AccountEditModalFormComponent;
    let fixture: ComponentFixture<AccountEditModalFormComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [AccountEditModalFormComponent]
        }).compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(AccountEditModalFormComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it("should create", () => {
        expect(component).toBeTruthy();
    });
});
