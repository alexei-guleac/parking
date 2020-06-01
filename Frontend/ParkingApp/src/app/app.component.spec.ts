import { HttpClient } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { async, TestBed } from '@angular/core/testing';
import { HttpLoaderFactory } from '@app/services/internalization/translation.service';
import { TranslateLoader, TranslateModule, TranslateService } from '@ngx-translate/core';
import { AppComponent } from './app.component';


const TRANSLATIONS_EN = require('../assets/i18n/en.json');
const TRANSLATIONS_RU = require('../assets/i18n/ru.json');

describe('AppComponent', () => {

    let translate: TranslateService;
    let http: HttpTestingController;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [
                AppComponent
            ],
            imports: [
                HttpClientTestingModule,
                TranslateModule.forRoot({
                    loader: {
                        provide: TranslateLoader,
                        useFactory: HttpLoaderFactory,
                        deps: [HttpClient]
                    }
                })
            ],
            providers: [TranslateService]
        }).compileComponents();
        translate = TestBed.get(TranslateService);
        http = TestBed.get(HttpTestingController);
    }));

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [AppComponent]
        }).compileComponents();
    }));

    it('should create the app', () => {
        const fixture = TestBed.createComponent(AppComponent);
        const app = fixture.debugElement.componentInstance;
        expect(app).toBeTruthy();
    });

    it(`should have as title 'ParkingApp'`, () => {
        const fixture = TestBed.createComponent(AppComponent);
        const app = fixture.debugElement.componentInstance;
        expect(app.title).toEqual('ParkingApp');
    });

    it('should render title', () => {
        const fixture = TestBed.createComponent(AppComponent);
        fixture.detectChanges();
        const compiled = fixture.debugElement.nativeElement;
        expect(compiled.querySelector('.content span').textContent).toContain(
            'ParkingApp app is running!'
        );
    });

    it('should load translations', async(() => {
        spyOn(translate, 'getBrowserLang').and.returnValue('ru');
        const fixture = TestBed.createComponent(AppComponent);
        const compiled = fixture.debugElement.nativeElement;

        // the DOM should be empty for now since the translations haven't been rendered yet
        expect(compiled.querySelector('h2').textContent).toEqual('');

        http.expectOne('/assets/i18n/ru.json').flush(TRANSLATIONS_RU);
        http.expectNone('/assets/i18n/en.json');

        // Finally, assert that there are no outstanding requests.
        http.verify();

        fixture.detectChanges();
        // the content should be translated to english now
        expect(compiled.querySelector('h2').textContent).toEqual(TRANSLATIONS_RU.HOME.TITLE);

        translate.use('en');
        http.expectOne('/assets/i18n/en.json').flush(TRANSLATIONS_EN);

        // Finally, assert that there are no outstanding requests.
        http.verify();

        // the content has not changed yet
        expect(compiled.querySelector('h2').textContent).toEqual(TRANSLATIONS_RU.HOME.TITLE);

        fixture.detectChanges();
        // the content should be translated to french now
        expect(compiled.querySelector('h2').textContent).toEqual(TRANSLATIONS_EN.HOME.TITLE);
    }));
});
