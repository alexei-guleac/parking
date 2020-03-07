package com.isd.parking.service.ldap;

import com.isd.parking.config.security.CustomPasswordEncoder;
import com.isd.parking.model.User;
import com.isd.parking.utils.ColorConsoleOutput;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

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
public class UserLdapClient {

    @Value("${spring.ldap.base}")
    private String ldapSearchBase;

    @Value("${ldap.passwordAttribute}")
    private String ldapPasswordAttribute;

    // value error create
    private final String ldapBaseDn = "dc=isd,dc=com";

    private final String[] objectClasses = {"top", "person", "organizationalPerson", "inetOrgPerson"};

    // private final String ldifFilePath = FileUtils.readPropertiesFiles("ldap-server.ldif");
    private final String ldifFilePath = "N:\\Programming\\IFC\\Diplome\\Diplome\\Back\\Backend\\src\\main\\resources\\ldap-server.ldif";
    //private final String ldifFilePath = FileUtils.readPropertiesFiles("ldap-server.ldif");

    private final LdapTemplate ldapTemplate;

    private final LdapName baseLdapPath = LdapUtils.newLdapName(ldapBaseDn);

    private final CustomPasswordEncoder passwordEncoder;

    private final ColorConsoleOutput console;

    @Autowired
    public UserLdapClient(@Qualifier(value = "ldapTemplate") LdapTemplate ldapTemplate, CustomPasswordEncoder passwordEncoder, ColorConsoleOutput console) {
        this.ldapTemplate = ldapTemplate;
        this.passwordEncoder = passwordEncoder;
        this.console = console;
    }

    // --------- LDAp in memory template methods ---------

    /**
     * Method authenticates user with given credentials
     *
     * @param username - user name
     * @param password - user pass
     * @return - success or denied boolean status of user authentication
     */
    public Boolean authenticate(String username, String password) {
        log.info(console.methodMsg(""));
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("uid", username));

        return ldapTemplate.authenticate(ldapSearchBase, filter.encode(), passwordEncoder.encode(password));
    }

    public static javax.naming.Name bindDN(String _x) {
        @SuppressWarnings("deprecation")
        javax.naming.Name name = new DistinguishedName("uid=" + _x + ",ou=people");
        return name;
    }

    private Name buildDn(User user) {
        return LdapNameBuilder.newInstance(baseLdapPath)
                .add("ou", "people")
                .add("uid", user.getId())
                .build();
    }

    private Name bindDnByUid(String uid) {
        return LdapNameBuilder.newInstance(baseLdapPath)
                .add("ou", "people")
                .add("uid", uid)
                .build();
    }

    public void createLdap(User user) {
        ldapTemplate.bind(buildDn(user), null, buildAttributes(user));
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ldap.advance.example.UserRepositoryIntf#createUser(ldap.advance.example.User)
     */
    public boolean createUser(User user) {
        log.info(console.methodMsg(""));
        Attributes userAttributes = mapUserAttributes(user);

        // save in-memory server
        ldapTemplate.bind(bindDN(user.getUsername()), null, userAttributes);

        // write to .ldif file
        writeEntryToLdifFile(user);

        return true;
    }

    private void writeEntryToLdifFile(User user) {
        Entry entry;
        try {
            entry = new Entry(new DN("uid=" + user.getUsername() + ",ou=people,dc=isd,dc=com"));
            mapUserToEntry(user, entry);
            log.info(console.methodMsg("Entry " + entry));

            // Write all of the matching entries to LDIF.
            LDIFWriter ldifWriter;
            try {
                ldifWriter = new LDIFWriter(new FileOutputStream(new File(ldifFilePath), true));
                ldifWriter.writeEntry(entry);
                ldifWriter.close();
            } catch (IOException e) {
                //throw new LdapMappingException("Error writing to file, try again", e);
            }
        } catch (LDAPException e) {
            e.printStackTrace();
        }
    }

    private void mapUserToEntry(User user, Entry entry) {
        entry.addAttribute("objectClass", objectClasses);
        entry.addAttribute("cn", user.getFullname());
        entry.addAttribute("sn", user.getLastname());
        entry.addAttribute("email", user.getEmail());
        entry.addAttribute("creationDate", String.valueOf(new Date(System.currentTimeMillis())));
        entry.addAttribute("uid", user.getUsername());
        entry.addAttribute(ldapPasswordAttribute, user.getPassword().getBytes());
    }

    private Attributes mapUserAttributes(User user) {
        Attribute objectClass = new BasicAttribute("objectClass");
        {
            for (String objClass : objectClasses) {
                objectClass.add(objClass);
            }
        }
        Attributes userAttributes = new BasicAttributes();
        userAttributes.put(objectClass);
        userAttributes.put("uid", user.getUsername());
        userAttributes.put("cn", user.getFullname());
        userAttributes.put("sn", user.getLastname());
        //userAttributes.put("email", user.getEmail());
        userAttributes.put(ldapPasswordAttribute, user.getPassword().getBytes());

        return userAttributes;
    }

    public void update(User user) {
        log.info(console.methodMsg(""));
        ldapTemplate.rebind(buildDn(user), null, buildAttributes(user));
    }

    public void updateLastName(User user) {
        log.info(console.methodMsg(""));
        Attribute attr = new BasicAttribute("sn", user.getLastname());
        ModificationItem item = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr);
        ldapTemplate.modifyAttributes(buildDn(user), new ModificationItem[]{item});
    }

    public void modify(final String username, final String password) {
        log.info(console.methodMsg(""));
        DirContextOperations context = ldapTemplate.lookupContext(bindDnByUid(username));

        context.setAttributeValues("objectclass", new String[]{"top", "person", "organizationalPerson", "inetOrgPerson"});
        context.setAttributeValue("cn", username);
        context.setAttributeValue("sn", username);
        context.setAttributeValue(ldapPasswordAttribute, passwordEncoder.encode(password));

        ldapTemplate.modifyAttributes(context);
    }

    private Attributes buildAttributes(User user) {
        Attributes attrs = new BasicAttributes();
        BasicAttribute ocAttr = new BasicAttribute("objectclass");
        ocAttr.add("top");
        ocAttr.add("person");
        ocAttr.add("inetOrgPerson");
        ocAttr.add("organizationalPerson");

        attrs.put(ocAttr);
        attrs.put("ou", "people");
        attrs.put("uid", user.getId());
        attrs.put("cn", user.getFullname());
        attrs.put("sn", user.getLastname());
        return attrs;
    }

    public void delete(User user) {
        log.info(console.methodMsg(""));
        ldapTemplate.unbind(buildDn(user));
    }

    /*
     * (non-Javadoc)
     * @see ldap.advance.example.UserRepositoryIntf#remove(java.lang.String)
     */
    public boolean deleteById(String uid) {
        log.info(console.methodMsg(""));
        ldapTemplate.unbind(bindDnByUid(uid));
        return true;
    }

    /**
     * Method search user by given username
     *
     * @param username - user name
     * @return - List of user names equals with given
     */

    public List<String> searchUser(final String username) {
        log.info(console.methodMsg(""));
        return ldapTemplate.search(
                ldapSearchBase,
                "uid=" + username,
                (AttributesMapper<String>) attrs -> (String) attrs
                        .get("cn")
                        .get());
    }

    public User findById(String uid) {
        return ldapTemplate.lookup(bindDnByUid(uid), new UserContextMapper());
    }

    public User findByDn(String dn) {
        log.info(console.methodMsg(""));
        return ldapTemplate.lookup(dn, new UserAttributesMapper());
    }

    /**
     * Get all users request method
     *
     * @return - list of all users
     */
    @SuppressWarnings("deprecation")
    public List<User> findAll() {
        log.info(console.methodMsg(""));
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        return ldapTemplate.search(DistinguishedName.EMPTY_PATH, "(objectclass=person)", controls, new UserAttributesMapper());
    }

    /*
     * (non-Javadoc)
     *
     * @see ldap.advance.example.UserRepositoryIntf#getUserDetails(java.lang.String)
     */
    public User getUserDetails(String userName) {
        log.info(console.methodMsg(""));
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
        log.info(console.methodMsg(""));
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
        log.info(console.methodMsg(""));
        LdapQuery query = query().base(ldapSearchBase);
        List<String> list = ldapTemplate.list(query.base());
        log.info("Users -> " + list);
        return ldapTemplate.search(query().base(ldapSearchBase).where("objectClass").is("person"), new SingleAttributesMapper());
    }

    public List<User> getAllUsers() {
        log.info(console.methodMsg(""));
        return ldapTemplate.search(query()
                .where("objectclass").is("person"), new UserAttributesMapper());
    }

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
            user.setFullname(attributes.get("cn").get().toString());

            if (attributes.get("userPassword") != null) {
                String userPassword;
                userPassword = new String((byte[]) attributes.get("userPassword").get(), StandardCharsets.UTF_8);
                user.setPassword(userPassword);
            }
            if (attributes.get("uid") != null) {
                user.setUsername(attributes.get("uid").get().toString());
            }
            if (attributes.get("sn") != null) {
                user.setLastname(attributes.get("sn").get().toString());
            }
            return user;
        }
    }

    private static class UserContextMapper extends AbstractContextMapper<User> {
        public User doMapFromContext(DirContextOperations context) {
            User user = new User();
            user.setFullname(context.getStringAttribute("cn"));
            user.setLastname(context.getStringAttribute("sn"));
            user.setUsername(context.getStringAttribute("uid"));

            return user;
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
