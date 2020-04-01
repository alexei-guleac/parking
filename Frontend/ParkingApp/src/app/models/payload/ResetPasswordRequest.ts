export class ResetPasswordRequest {

    confirmationToken: string;

    password: string;

    constructor(confirmationToken: string, password: string) {
        this.confirmationToken = confirmationToken;
        this.password = password;
    }
}
