/**
 * Backend server error message model
 */
export class ServerErrorMessage {
    message: string;

    code: number;

    detail: string;

    constructor(message?: string, code?: number, detail?: string) {
        if (message) {
            this.message = message;
            this.code = code;
            this.detail = detail;
        }
    }
}
