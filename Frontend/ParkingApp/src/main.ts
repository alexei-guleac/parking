import { enableProdMode } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule } from '@app/app.module';
import { environment } from '@env';
import 'hammerjs';


if (environment.production) {
    enableProdMode();

    // disable logs in production mode
    if (window) {
        // tslint:disable-next-line:only-arrow-functions
        window.console.log = function() {
        };
    }
}

platformBrowserDynamic()
    .bootstrapModule(AppModule)
    .catch(err => console.error(err));
