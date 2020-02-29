package com.isd.parking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;

/**
 * LDAP user entry
 */
@Entry(base = "ou=people", objectClasses = {"person", "inetOrgPerson", "top"})
public class User {

    @JsonIgnore
    @Id
    private Name id;

    //@JsonProperty("username")
    private @Attribute(name = "uid")
    @DnAttribute(value = "uid")
    String username;

    //@JsonProperty("firstName")
    private @Attribute(name = "cn")
    String firstName;

    //@JsonProperty("lastName")
    private @Attribute(name = "sn")
    String lastName;

    private @Attribute(name = "userPassword")
    String password;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Name getId() {
        return id;
    }

    public void setId(Name id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return username;
    }
}
