package com.isd.parking.models.users;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.naming.Name;
import javax.validation.constraints.Email;


@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class User {

    @JsonProperty()
    @JsonAlias({"username"})
    String username;

    @JsonProperty()
    @JsonAlias({"email"})
    @Email
    String email;

    @JsonProperty()
    @JsonAlias({"fullname"})
    String fullname;

    @JsonProperty()
    @JsonAlias({"lastname"})
    String lastname;

    @JsonProperty()
    @JsonAlias({"password"})
    String password;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(User user, String password) {
        this.username = user.username;
        this.fullname = user.fullname;
        this.lastname = user.lastname;
        this.email = user.email;
        this.password = password;
    }

    public User(String username, String fullname, String lastname, String password) {
        this.username = username;
        this.fullname = fullname;
        this.lastname = lastname;
        this.password = password;
    }

    public User(Name id, String username, String fullname, String lastname, String email, String password) {
        this.username = username;
        this.fullname = fullname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }
}
