import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Observable } from 'rxjs';
import { localization } from '../../../environments/localization';


@Injectable({
    providedIn: 'root'
})
export class AcceptLanguageInterceptorService implements HttpInterceptor {

    constructor(private translate: TranslateService) {
    }

    /**
     * Modify default 'Accept-Language' header provided by ngx translate library
     * conform user selected language
     *
     * Header example - Accept-Language: ru,en-US;q=0.9,en;q=0.8,ro;q=0.7
     *
     * @param req - server request
     * @param next - next chain interceptor
     */
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        const languages = this.translate.getLangs();
        const currentLang = this.translate.currentLang;
        let initialQuantity = 1.0;
        const quantityPrefix = ';q=';
        let header;

        // append current language with specified locale
        header = currentLang;
        initialQuantity -= 0.1;
        header += ',' + localization.localesDetailed[currentLang] + quantityPrefix + initialQuantity;

        // append other available languages
        languages.forEach((lang) => {
            if (lang !== currentLang) {
                initialQuantity -= 0.1;
                header += ',' + lang + quantityPrefix + initialQuantity;
            }
        });

        // Clone the request to add the new header
        const clonedRequest = req.clone({
            headers: req.headers.set('Accept-Language', header)
        });

        // Pass the cloned request instead of the original request to the next handle
        return next.handle(clonedRequest);
    }
}
