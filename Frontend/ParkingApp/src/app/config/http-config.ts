import { HttpHeaders } from "@angular/common/http";


/**
 * Adds 'Content-type': 'application/json' and Accept: 'application/json'
 * headers to HTTP request
 */
export function setAcceptJsonHeaders() {
    return new HttpHeaders({
        'Content-type': 'application/json',
        Accept: 'application/json'
    });
}
