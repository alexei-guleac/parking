import { Injectable } from '@angular/core';
import { MissingTranslationHandler, MissingTranslationHandlerParams } from '@ngx-translate/core';


@Injectable({
    providedIn: 'root'
})
export class MissingTranslationService implements MissingTranslationHandler {

    handle(params: MissingTranslationHandlerParams) {
        const error = `WARN: '${params.key}' is missing in '${params.translateService.currentLang}' locale`;
        // console.error(error);
        return error;
    }

}
