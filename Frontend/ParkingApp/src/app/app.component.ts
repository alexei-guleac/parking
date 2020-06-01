import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { TranslateCacheService } from 'ngx-translate-cache';
import { localization } from '../environments/localization';
import { routerTransition } from './components/animations/animations';
import { actions } from './services/navigation/app.endpoints';


/**
 * Main application component
 */
@Component({
    selector: 'app-root',
    animations: [routerTransition],
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
    actions = actions;

    private action: string;

    constructor(private route: ActivatedRoute,
                private translate: TranslateService,
                private translateCacheService: TranslateCacheService
    ) {
        translate.addLangs(localization.locales);
        // this language will be used as a fallback when a i18n isn't found in the current language
        translate.setDefaultLang(localization.defaultLocale);

        // the lang to use, if the lang isn't available, it will use the current loader to get them
        const browserLang = translate.getBrowserLang();
        const userLanguage = localStorage.getItem('lang');
        if (userLanguage) {
            translate.use(userLanguage);
        } else {
            translate.use(browserLang.match(/en|ro|ru/) ? browserLang : localization.defaultLocale);
        }
        translateCacheService.init();
    }

    ngOnInit() {
        this.processUrlParams();
    }

    processUrlParams() {
        this.route.queryParams.subscribe(
            // tslint:disable-next-line: no-string-literal
            (params) => (this.action = params['action'])
        );
    }

    getState(outlet) {
        return outlet.activatedRouteData.state;
    }
}
