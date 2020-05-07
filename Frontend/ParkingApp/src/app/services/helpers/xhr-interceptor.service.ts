import { HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";


/**
 * Interceptor for XHR requests
 */
@Injectable({
    providedIn: "root"
})
export class XhrInterceptor implements HttpInterceptor {

    /**
     * Add 'X-Requested-With', 'XMLHttpRequest' request headers
     * @param req - request
     * @param next - next HTTP request handler
     */
    intercept(req: HttpRequest<any>, next: HttpHandler) {
        const xhr = req.clone({
            headers: req.headers.set("X-Requested-With", "XMLHttpRequest")
        });
        return next.handle(xhr);
    }
}
