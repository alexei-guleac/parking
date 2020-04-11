package com.isd.parking.models.users;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isd.parking.utils.ReflectionMethods;
import lombok.Data;

import javax.naming.Name;
import javax.validation.constraints.Email;
import java.util.ArrayList;


@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class User {

    public static final ArrayList<String> userClassAttributesList =
        (ArrayList<String>) new ReflectionMethods().getFieldsNames(User.class);

    @JsonProperty()
    @JsonAlias({"username"})
    private String username;

    @JsonProperty()
    @JsonAlias({"email"})
    @Email
    private String email;

    @JsonProperty()
    @JsonAlias({"firstname"})
    private String firstname;

    @JsonProperty()
    @JsonAlias({"lastname"})
    private String lastname;

    @JsonProperty()
    @JsonAlias({"password"})
    private String password;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(User user, String password) {
        this.username = user.username;
        this.firstname = user.firstname;
        this.lastname = user.lastname;
        this.email = user.email;
        this.password = password;
    }

    public User(String username, String firstname, String lastname, String password) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
    }

    public User(Name id, String username, String firstname, String lastname, String email, String password) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public static Object getUserProperty(User user, String name) {
        return ReflectionMethods.getPropertyValue(user, name);
    }

    public String getFullname() {
        if (this.firstname != null && this.lastname != null) {
            return this.firstname + " " + this.lastname;
        } else return null;
    }
}
