export class User {
    id: string;

    username: string;

    email: string;

    password: string;

    firstname: string;

    lastname: string;

    constructor(
        id?: string,
        name?: string,
        email?: string,
        password?: string,
        firstname?: string,
        lastname?: string
    ) {
        console.log(
            id +
            ' ' +
            name +
            ' ' +
            email +
            ' ' +
            password +
            ' ' +
            firstname +
            ' ' +
            lastname
        );
        this.id = id;
        this.username = name;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }

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
