package com.isd.parking.models;

import lombok.Data;

import javax.naming.Name;
import java.util.HashSet;
import java.util.Set;


@Data
public class Group {

    private String name;

    private Set<Name> members;

    public Group() {
    }

    public Group(String name, Set<Name> members) {
        this.name = name;
        this.members = members;
    }

    public Group(Name dn, String name, Set<Name> members) {
        this.name = name;
        this.members = members;
    }

    public void addMember(Name member) {
        if (this.members == null) {
            this.members = new HashSet<>();
        }
        members.add(member);
    }

    public void removeMember(Name member) {
        members.remove(member);
    }

    @Override
    public String toString() {
        return "Group{" +
            "name='" + name + '\'' +
            ", members=" + members +
            '}';
    }
}
