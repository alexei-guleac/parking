import {HttpHeaders} from '@angular/common/http';


export function setAcceptJsonHeaders() {
    return new HttpHeaders({
        'Content-type': 'application/json',
        Accept: 'application/json'
    });
}
