import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { getJwtToken } from '../account/account-session-storage.service';


/**
 * JWT HTTP chain interceptor
 */
@Injectable()
export class TokenInterceptor implements HttpInterceptor {

    /**
     * Add JWT if it is present in browser storage
     * @param request - HTTP server request
     * @param next - newt request handler
     */
    intercept(
        request: HttpRequest<any>,
        next: HttpHandler
    ): Observable<HttpEvent<any>> {
        if (getJwtToken()) {
            request = this.addToken(request, getJwtToken());
        }

        return next.handle(
            request
        );
    }

    /**
     * Add JWT to HTTP request
     * @param request - HTTP server request
     * @param token - logged in user JWT
     */
    private addToken(request: HttpRequest<any>, token: string) {
        return request.clone({
            setHeaders: {
                Authorization: `Bearer ${token}`
            }
        });
    }
}
