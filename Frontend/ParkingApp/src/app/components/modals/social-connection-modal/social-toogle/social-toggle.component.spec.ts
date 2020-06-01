import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SocialToggleComponent } from './social-toggle.component';


describe('SocialToogleComponent', () => {
    let component: SocialToggleComponent;
    let fixture: ComponentFixture<SocialToggleComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [SocialToggleComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(SocialToggleComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
