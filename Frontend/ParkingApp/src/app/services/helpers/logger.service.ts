import {Injectable} from '@angular/core';
import {NGXLogger} from "ngx-logger";


@Injectable({
    providedIn: 'root'
})
export class LoggerService {

    constructor(
        private logger: NGXLogger) {
    }

    logMethod(message: string, ...variables) {
        const currentMethodname = this.getMethodName();
        this.logger.log('-->');
        console.log(this.getMessage(message, currentMethodname));
    }

    debugMethod(message: string, ...variables) {
        const currentMethodname = this.getMethodName();
        this.logger.log('-->');
        // tslint:disable-next-line:no-console
        console.debug(this.getMessage(message, currentMethodname));
    }

    warnMethod(message: string, ...variables) {
        const currentMethodname = this.getMethodName();
        this.logger.log('-->');
        console.warn(this.getMessage(message, currentMethodname));
    }

    errorMethod(message: string, ...variables) {
        const currentMethodname = this.getMethodName();
        this.logger.log('-->');
        console.error(this.getMessage(message, currentMethodname));
    }

    getMethodName() {
        const err = new Error();
        // we want the 3nd method in the call stack
        return /at \w+\.(\w+)/.exec(err.stack.split('\n')[3])[1];
    }

    private getMessage(message: string, currentMethodname) {
        // return c`${message}.black ${'executing in'}.green ${'{' + currentMethodname + '}'}`.rgb(22, 110, 215);
        return message;
    }
}
