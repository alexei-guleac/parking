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
@Entry(base = "ou=people", objectClasses = {"person", "inetOrgPerson", "top"})
@Data
public final class User {

    @JsonIgnore
    @Id
    private Name id;

    @JsonProperty("username")
    private @Attribute(name = "uid")
    @DnAttribute(value = "uid")
    String username;

    @JsonProperty("firstName")
    private @Attribute(name = "cn")
    String firstName;

    @JsonProperty("lastName")
    private @Attribute(name = "sn")
    String lastName;

    private @Attribute(name = "userPassword")
    String password;

    public User() {
    }

    public User(String username, String firstName, String lastName, String password) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
