package com.isd.parking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;

/**
 * LDAP user entry
 */
@Entry(base = "ou=people", objectClasses = {"top", "person", "organizationalPerson", "inetOrgPerson"})
@Data
public final class User {

    @JsonIgnore
    @Id
    private Name id;

    @JsonProperty("username")
    private @Attribute(name = "uid")
    @DnAttribute(value = "uid")
    String username;

    @JsonProperty("fullname")
    private @Attribute(name = "cn")
    String fullname;

    @JsonProperty("lastname")
    private @Attribute(name = "sn")
    String lastname;

    @JsonProperty("email")
    private @Attribute(name = "email")
    String email;

    private @Attribute(name = "userPassword")
    String password;

    public User() {
    }

    public User(User user, String password) {
        this.id = user.id;
        this.username = user.username;
        this.fullname = user.fullname;
        this.lastname = user.lastname;
        this.email = user.email;
        this.password = password;
    }

    public User(Name id, String username, String fullname, String lastname, String email, String password) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public User(String username, String fullname, String lastname, String password) {
        this.username = username;
        this.fullname = fullname;
        this.lastname = lastname;
        this.password = password;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
