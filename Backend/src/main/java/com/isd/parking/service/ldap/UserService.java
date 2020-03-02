package com.isd.parking.service.ldap;

import com.isd.parking.config.security.CustomPasswordEncoder;
import com.isd.parking.model.User;
import com.isd.parking.repository.UserRepository;
import com.unboundid.ldap.sdk.DN;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldif.LDIFWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.LdapName;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.ldap.query.LdapQueryBuilder.query;


/**
 * User Service class for ldap storage repository
 * Contains methods for
 * authenticate user,
 * searchUser user by username,
 * create new user entry in ldap repository,
 * modify existed user,
 * get all users from ldap storage
 */
@Service
@Slf4j
public class UserService {

    private final LdapTemplate ldapTemplate;
    private final LdapName baseLdapPath = LdapUtils.newLdapName("dc=isd,dc=com");
    ;

    private final CustomPasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Value("${spring.ldap.base}")
    private String ldapSearchBase;

    @Value("${ldap.passwordAttribute}")
    private String ldapPasswordAttribute;

    @Autowired
    public UserService(@Qualifier(value = "ldapTemplate") LdapTemplate ldapTemplate, UserRepository userRepository, CustomPasswordEncoder passwordEncoder) {
        this.ldapTemplate = ldapTemplate;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    // --------- LDAp template in memory methods ---------

    /**
     * Method authenticates user with given credentials
     *
     * @param username - user name
     * @param password - user pass
     * @return - success or denied boolean status of user authentication
     */
    public Boolean authenticate(String username, String password) {
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("uid", username));
        //log.info("executing {authenticate} " + username + " filter " + filter.encode() + " pass " + password);
        //log.info("executing {authenticate} " + username + " filter " + filter.encode() + " pass " + passwordEncoder.encode(password));

        return ldapTemplate.authenticate(ldapSearchBase, filter.encode(), passwordEncoder.encode(password));
    }

    public static javax.naming.Name bindDN(String _x) {
        @SuppressWarnings("deprecation")
        javax.naming.Name name = new DistinguishedName("uid=" + _x + ",ou=people");
        return name;
    }

    private Name buildDn(User p) {
        return LdapNameBuilder.newInstance(baseLdapPath)
                .add("ou", "people")
                .add("uid", p.getId())
                .build();
    }

    public void createLdap(User p) {
        Name dn = buildDn(p);
        ldapTemplate.bind(dn, null, buildAttributes(p));
    }

    public void update(User p) {
        ldapTemplate.rebind(buildDn(p), null, buildAttributes(p));
    }

    public User findOne(String uid) {
        Name dn = LdapNameBuilder.newInstance(baseLdapPath)
                .add("ou", "people")
                .add("uid", uid)
                .build();
        return ldapTemplate.lookup(dn, new UserContextMapper());
    }

    private Attributes buildAttributes(User p) {
        Attributes attrs = new BasicAttributes();
        BasicAttribute ocAttr = new BasicAttribute("objectclass");
        ocAttr.add("top");
        ocAttr.add("person");
        attrs.put(ocAttr);
        attrs.put("ou", "people");
        attrs.put("uid", p.getId());
        attrs.put("cn", p.getFullName());
        attrs.put("sn", p.getLastName());
        return attrs;
    }

    public void updateLastName(User p) {
        Attribute attr = new BasicAttribute("sn", p.getLastName());
        ModificationItem item = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr);
        ldapTemplate.modifyAttributes(buildDn(p), new ModificationItem[]{item});
    }

    public void delete(User p) {
        ldapTemplate.unbind(buildDn(p));
    }

    /**
     * Method search user by given username
     *
     * @param username - user name
     * @return - List of user names equals with given
     */

    public List<String> searchUser(final String username) {
        log.info("executing {searchUser}");
        return ldapTemplate.search(
                ldapSearchBase,
                "uid=" + username,
                (AttributesMapper<String>) attrs -> (String) attrs
                        .get("cn")
                        .get());
    }

    /**
     * Method create user with given username and password
     *
     * @param username - user name
     * @param password - user pass
     */
    public void create(final String username, final String password) {
        log.info("executing {create}");
        User newUser = new User(username, CustomPasswordEncoder.digestSHA(password));
        newUser.setId(LdapUtils.emptyLdapName());
        //newUser.setId(LdapUtils.newLdapName(new DistinguishedName("uid=" + username + ",ou=people")));

        userRepository.save(newUser);
    }


    public void modify(final String username, final String password) {
        Name dn = LdapNameBuilder
                .newInstance()
                .add("ou", "people")
                .add("uid", username)
                .build();
        DirContextOperations context = ldapTemplate.lookupContext(dn);

        context.setAttributeValues("objectclass", new String[]{"top", "person", "organizationalPerson", "inetOrgPerson"});
        context.setAttributeValue("cn", username);
        context.setAttributeValue("sn", username);
        context.setAttributeValue(ldapPasswordAttribute, CustomPasswordEncoder.digestSHA(password));

        ldapTemplate.modifyAttributes(context);
    }

    /*
     * (non-Javadoc)
     *
     * @see ldap.advance.example.UserRepositoryIntf#getAllUsers()
     */

    /**
     * Get all users request method
     *
     * @return - list of all users
     */
    @SuppressWarnings("deprecation")
    public List<User> findAll() {
        log.info("executing {getAllUsers}");
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        return ldapTemplate.search(DistinguishedName.EMPTY_PATH, "(objectclass=person)", controls, new UserAttributesMapper());

        //return userRepository.findAll();
    }

    /*
     * (non-Javadoc)
     *
     * @see ldap.advance.example.UserRepositoryIntf#getUserDetails(java.lang.String)
     */
    public User getUserDetails(String userName) {
        log.info("executing {getUserDetails}");
        List<User> list = ldapTemplate.search(query().base(ldapSearchBase).where("uid").is(userName), new UserAttributesMapper());
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see ldap.advance.example.UserRepositoryIntf#getUserDetail(java.lang.String)
     */
    public String getUserDetail(String userName) {
        log.info("executing {getUserDetails}");
        List<String> results = ldapTemplate.search(query().base(ldapSearchBase).where("uid").is(userName), new MultipleAttributesMapper());
        if (results != null && !results.isEmpty()) {
            return results.get(0);
        }
        return " userDetails for " + userName + " not found .";
    }

    /*
     * (non-Javadoc)
     *
     * @see ldap.advance.example.UserRepositoryIntf#getAllUserNames()
     */
    public List<String> getAllUserNames() {
        log.info("executing {getAllUserNames}");
        LdapQuery query = query().base(ldapSearchBase);
        List<String> list = ldapTemplate.list(query.base());
        log.info("Users -> " + list);
        return ldapTemplate.search(query().base(ldapSearchBase).where("objectClass").is("person"), new SingleAttributesMapper());
    }

    public List<User> getAllUsers() {
        return ldapTemplate.search(query()
                .where("objectclass").is("person"), new UserAttributesMapper());
    }

    public User findUser(String dn) {
        log.info("executing {findUser}");
        return ldapTemplate.lookup(dn, new UserAttributesMapper());
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ldap.advance.example.UserRepositoryIntf#createUser(ldap.advance.example.User)
     */
    public boolean createUser(User user) {
        log.info("executing {createUser}");
        /*Attribute objectClass = new BasicAttribute("objectClass");
        {
            objectClass.add("top");
            objectClass.add("uidObject");
            objectClass.add("person");
            objectClass.add("organizationalPerson");
        }
        Attributes userAttributes = new BasicAttributes();
        userAttributes.put(objectClass);
        userAttributes.put("cn", user.getFirstName());
        userAttributes.put("sn", user.getLastName());
        userAttributes.put("uid", user.getUsername());
        userAttributes.put(ldapPasswordAttribute, user.getPassword().getBytes());
        ldapTemplate.bind(bindDN(user.getUsername()), null, userAttributes);*/

        Entry entry = null;
        try {
            entry = new Entry(new DN("uid=" + user.getUsername() + ",ou=people,dc=isd,dc=com"));
        } catch (LDAPException e) {
            e.printStackTrace();
        }
        entry.addAttribute("objectClass", "top", "domain", "extensibleObject");
        entry.addAttribute("cn", user.getFirstName());
        entry.addAttribute("sn", user.getLastName());
        entry.addAttribute("uid", user.getUsername());
        entry.addAttribute(ldapPasswordAttribute, user.getPassword().getBytes());

        File file = new File("N:\\Programming\\IFC\\Diplome\\Latest_backend\\Backend\\src\\main\\resources\\ldap-server-test.ldif");

        // Write all of the matching entries to LDIF.
        LDIFWriter ldifWriter;
        try {
            ldifWriter = new LDIFWriter(file);
            ldifWriter.writeEntry(entry);
            ldifWriter.close();
        } catch (IOException e) {
            //throw new LdapMappingException("Error writing to file, try again", e);
        }

        create(user.getUsername(), user.getPassword());

        return true;
    }

    /*
     * (non-Javadoc)
     * @see ldap.advance.example.UserRepositoryIntf#remove(java.lang.String)
     */
    public boolean remove(String uid) {
        ldapTemplate.unbind(bindDN(uid));
        return true;
    }

    // --------- LDAp repository methods ---------

    public List<String> search(final String username) {
        log.info("executing {search}");
        List<User> userList = userRepository.findByUsernameLikeIgnoreCase(username);
        if (userList == null) {
            return Collections.emptyList();
        }
        //return userList;
        return userList.stream()
                .map(User::getFullName)
                .collect(Collectors.toList());
    }

    /**
     * Method update user with given username and password
     *
     * @param username - user name
     * @param password - user pass
     */
    /*public void modify(final String username, final String password) {
        User user = userRepository.findByUsername(username);
        user.setPassword(password);

        userRepository.save(user);
    }*/


    /**
     * This class is responsible to prepare User object after ldap search.
     *
     * @author
     */
    private static class UserAttributesMapper implements AttributesMapper<User> {

        @Override
        public User mapFromAttributes(Attributes attributes) throws NamingException {
            User user;
            if (attributes == null) {
                return null;
            }
            user = new User();
            user.setFirstName(attributes.get("cn").get().toString());

            if (attributes.get("userPassword") != null) {
                String userPassword;
                userPassword = new String((byte[]) attributes.get("userPassword").get(), StandardCharsets.UTF_8);
                user.setPassword(userPassword);
            }
            if (attributes.get("uid") != null) {
                user.setUsername(attributes.get("uid").get().toString());
            }
            if (attributes.get("sn") != null) {
                user.setLastName(attributes.get("sn").get().toString());
            }
            return user;
        }
    }

    private static class UserContextMapper extends AbstractContextMapper<User> {
        public User doMapFromContext(DirContextOperations context) {
            User person = new User();
            person.setFirstName(context.getStringAttribute("cn"));
            person.setLastName(context.getStringAttribute("sn"));
            person.setUsername(context.getStringAttribute("uid"));
            return person;
        }
    }

    /**
     * This class is responsible to print only cn .
     *
     * @author
     */
    private static class SingleAttributesMapper implements AttributesMapper<String> {

        @Override
        public String mapFromAttributes(Attributes attrs) {
            Attribute cn = attrs.get("cn");
            return cn.toString();
        }
    }

    /**
     * This class is responsible to print all the content in string format.
     *
     * @author
     */
    private static class MultipleAttributesMapper implements AttributesMapper<String> {

        @Override
        public String mapFromAttributes(Attributes attrs) throws NamingException {
            NamingEnumeration<? extends Attribute> all = attrs.getAll();
            StringBuilder result = new StringBuilder();
            result.append("\n Result { \n");
            while (all.hasMore()) {
                Attribute id = all.next();
                result.append(" \t |_  #").append(id.getID()).append("= [ ").append(id.get()).append(" ]  \n");
                log.info(id.getID() + "\t | " + id.get());
            }
            result.append("\n } ");
            return result.toString();
        }
    }
}
