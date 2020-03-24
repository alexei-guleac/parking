import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {GithubOauthComponent} from './github-oauth.component';


describe('GithubOauthComponent', () => {
    let component: GithubOauthComponent;
    let fixture: ComponentFixture<GithubOauthComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [GithubOauthComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(GithubOauthComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
