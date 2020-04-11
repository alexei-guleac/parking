import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {setAcceptJsonHeaders} from '@app/config/http-config';


@Injectable({
    providedIn: 'root',
})
export class HttpClientService {
    constructor(private http: HttpClient) {
    }

    postRequest<T>(url, body, options) {
        return this.http.post<T>(url, body, options);
    }

    postJsonRequest<T>(url, body) {
        return this.http.post<T>(url, body, {
            headers: setAcceptJsonHeaders(),
        });
    }

    getRequest<T>(url, body, options) {
        return this.http.get<T>(url, options);
    }

    getJsonRequest<T>(url) {
        return this.http.get<T>(url, {
            headers: setAcceptJsonHeaders(),
        });
    }
}
