import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ParkingLayoutOldComponent} from './parking-layout-old.component';

describe('ParkingLayoutComponent', () => {
    let component: ParkingLayoutOldComponent;
    let fixture: ComponentFixture<ParkingLayoutOldComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [ParkingLayoutOldComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(ParkingLayoutOldComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
