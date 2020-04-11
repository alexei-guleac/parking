import {Injectable} from '@angular/core';
import {User} from '@app/models/User';
import {HttpClientService} from '@app/services/helpers/http-client.service';
import {api} from '@app/services/navigation/app.endpoints';
import {environment} from '@env';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';


@Injectable({
    providedIn: 'root',
})
export class UserService {
    constructor(private http: HttpClientService) {
    }

    getUserByUsername(username: string): Observable<User> {
        return this.http
            .postJsonRequest<User>(environment.restUrl + api.profile, username)
            .pipe(
                map((user) => {
                    console.log('USER' + JSON.stringify(user));
                    return user;
                })
            );
    }

    getAllUsers(): Observable<Array<User>> {
        return this.http
            .getJsonRequest<Array<User>>(environment.restUrl + api.users)
            .pipe(map((data) => data.map((user) => User.fromHttp(user))));
    }

    saveUser(user: User, username: string) {
        return this.http.postJsonRequest<any>(
            environment.restUrl + api.profileUpdate,
            {user, username}
        );
    }

    deleteUser(username: string) {
        return this.http.postJsonRequest<any>(
            environment.restUrl + api.profileDelete,
            username
        );
    }
}
