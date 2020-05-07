/**
 * User model
 */
export class User {

    id: string;

    username: string;

    email: string;

    password: string;

    firstname: string;

    lastname: string;

    socialIds: any;

    constructor(
        id?: string,
        name?: string,
        email?: string,
        password?: string,
        firstname?: string,
        lastname?: string
    ) {
        this.id = id;
        this.username = name;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    /**
     * Forms user object from HTTP response
     * @param user - HTTP response user
     */
    static fromHttp(user: any) {
        return new User(
            user.id,
            user.username,
            user.email,
            null,
            user.fullname
        );
    }
}
