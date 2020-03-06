export class User {
    id: string;
    username: string;
    email: string;
    password: string;
    fullname: string;
    lastname: string;

    constructor(id?: string, name?: string, email?: string, password?: string, fullname?: string, lastname?: string) {
        console.log(id + ' ' + name + ' ' + email + ' ' + password + ' ' + fullname + ' ' + lastname);

        this.id = id;
        this.username = name;
        this.email = email;
        this.password = password;
        this.fullname = fullname;
        this.lastname = lastname;
    }
}
