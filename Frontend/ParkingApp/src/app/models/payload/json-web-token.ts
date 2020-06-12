/**
 * JSON Web Token for user authorization in system
 */
export class JsonWebToken {
    sub: string;

    aud: string;

    roles: string;

    exp: Date;

    iat: Date;

    constructor(sub: string, aud: string, roles: string, exp: Date, iat: Date) {
        this.sub = sub;
        this.aud = aud;
        this.roles = roles;
        this.exp = exp;
        this.iat = iat;
    }
}
