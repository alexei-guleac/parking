package com.isd.parking.models.users;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isd.parking.utilities.ReflectionMethods;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.naming.Name;
import javax.validation.constraints.Email;
import java.util.ArrayList;


/**
 * User model class
 * is used for map client web application request
 */
@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@ApiModel(description = "User model. ")
public class User {

    public static final ArrayList<String> userClassAttributesList =
        (ArrayList<String>) new ReflectionMethods().getFieldsNames(User.class);

    @JsonProperty()
    @JsonAlias({"username"})
    @ApiModelProperty(notes = "User username", required = true)
    private String username;

    @JsonProperty()
    @JsonAlias({"email"})
    @ApiModelProperty(notes = "User email")
    @Email
    private String email;

    @JsonProperty()
    @JsonAlias({"firstname"})
    @ApiModelProperty(notes = "User firstname")
    private String firstname;

    @JsonProperty()
    @JsonAlias({"lastname"})
    @ApiModelProperty(notes = "User lastname")
    private String lastname;

    @JsonProperty()
    @JsonAlias({"password"})
    @ApiModelProperty(notes = "User password")
    private String password;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(@NotNull User user, String password) {
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

    @Nullable String getFullname() {
        if (this.firstname != null && this.lastname != null) {
            return this.firstname + " " + this.lastname;
        } else return null;
    }
}
