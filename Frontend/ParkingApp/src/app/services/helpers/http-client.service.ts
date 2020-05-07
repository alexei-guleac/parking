import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { setAcceptJsonHeaders } from "@app/config/http-config";


/**
 * Additional HTTP client wrapper for performing POST/GET server requests (including JSON accepted)
 */
@Injectable({
    providedIn: "root"
})
export class HttpClientService {

    constructor(private http: HttpClient) {
    }

    /**
     * Server POST request
     * @param url - target URL
     * @param body - target request body
     * @param options - request options
     */
    postRequest<T>(url, body, options) {
        return this.http.post<T>(url, body, options);
    }

    /**
     * Server POST JSON accepted request
     * @param url - target URL
     * @param body - target request body
     */
    postJsonRequest<T>(url, body) {
        return this.http.post<T>(url, body, {
            headers: setAcceptJsonHeaders()
        });
    }

    /**
     * Server GET request
     * @param url - target URL
     * @param options - request options
     */
    getRequest<T>(url, options) {
        return this.http.get<T>(url, options);
    }

    /**
     * Server GET JSON accepted request
     * @param url - target URL
     */
    getJsonRequest<T>(url) {
        return this.http.get<T>(url, {
            headers: setAcceptJsonHeaders()
        });
    }
}
