package com.isd.parking.repository;

import com.isd.parking.models.Group;
import com.isd.parking.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.core.support.BaseLdapNameAware;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Repository;

import javax.naming.Name;
import javax.naming.ldap.LdapName;
import java.util.List;

@Repository
public class GroupRepository implements BaseLdapNameAware {

    private final LdapTemplate ldapTemplate;

    private LdapName baseLdapPath;

    @Autowired
    public GroupRepository(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    public void setBaseLdapPath(LdapName baseLdapPath) {
        this.baseLdapPath = baseLdapPath;
    }

    public List<Group> findAll() {
        return ldapTemplate.search(
                LdapQueryBuilder.query().where("objectclass").is("groupOfUniqueNames"),
                new GroupContextMapper());
    }

    public void addMemberToGroup(String groupName, User p) {
        Name groupDn = buildGroupDn(groupName);
        Name personDn = buildUserDn(p);

        DirContextOperations ctx = ldapTemplate.lookupContext(groupDn);
        ctx.addAttributeValue("uniqueMember", personDn);

        ldapTemplate.modifyAttributes(ctx);
    }

    public void removeMemberFromGroup(String groupName, User p) {
        Name groupDn = buildGroupDn(groupName);
        Name personDn = buildUserDn(p);

        DirContextOperations ctx = ldapTemplate.lookupContext(groupDn);
        ctx.removeAttributeValue("uniqueMember", personDn);

        ldapTemplate.modifyAttributes(ctx);
    }

    private Name buildGroupDn(String groupName) {
        return LdapNameBuilder.newInstance(baseLdapPath)
                .add("ou", "groups")
                .add("cn", groupName)
                .build();
    }

    private Name buildUserDn(User user) {
        return LdapNameBuilder.newInstance(baseLdapPath)
                .add("ou", "people")
                .add("uid", user.getId())
                .build();
    }

    private static class GroupContextMapper extends AbstractContextMapper<Group> {
        public Group doMapFromContext(DirContextOperations context) {
            Group group = new Group();
            group.setName(context.getStringAttribute("cn"));
            Object[] members = context.getObjectAttributes("uniqueMember");
            for (Object member : members) {
                Name memberDn = LdapNameBuilder.newInstance(String.valueOf(member)).build();
                group.addMember(memberDn);
            }
            return group;
        }
    }
}
