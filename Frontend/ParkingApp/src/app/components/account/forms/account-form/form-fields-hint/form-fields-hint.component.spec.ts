import { async, ComponentFixture, TestBed } from "@angular/core/testing";

import { FormFieldsHintComponent } from "./form-fields-hint.component";


describe("FormFieldsHintComponent", () => {
    let component: FormFieldsHintComponent;
    let fixture: ComponentFixture<FormFieldsHintComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [FormFieldsHintComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(FormFieldsHintComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it("should create", () => {
        expect(component).toBeTruthy();
    });
});
