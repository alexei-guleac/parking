package com.isd.parking.repository;

import com.isd.parking.config.security.CustomPasswordEncoder;
import com.isd.parking.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQuery;

import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Slf4j
public class UserRepositoryImpl implements UserRepository {

    private final CustomPasswordEncoder passwordEncoder;

    private final LdapTemplate ldapTemplate;

    @Autowired
    public UserRepositoryImpl(@Qualifier(value = "ldapTemplate") LdapTemplate ldapTemplate, CustomPasswordEncoder passwordEncoder) {
        this.ldapTemplate = ldapTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findOne(LdapQuery ldapQuery) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(Name name) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Name name) {
        return false;
    }

    @Override
    public User findByUsername(String username) {
        return null;
    }

    public List<User> findByUsernameLikeIgnoreCase(String username) {
        return null;
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        return null;
    }

    public Boolean authenticate(String username, String password) {
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("uid", username));
        //log.info("executing {authenticate} " + username + " filter " + filter.encode() + " pass " + password);
        //log.info("executing {authenticate} " + username + " filter " + filter.encode() + " pass " + passwordEncoder.encode(password));

        return ldapTemplate.authenticate("ou=people", filter.encode(), passwordEncoder.encode(password));
    }

    /*
     * (non-Javadoc)
     *
     * @see ldap.advance.example.UserRepositoryIntf#authenticate(java.lang.String,
     * java.lang.String)
     */
    public boolean authenticate(String base, String userName, String password) {
        log.info("executing {authenticate}");
        return ldapTemplate.authenticate(base, "(uid=" + userName + ")", password);
    }

    public static javax.naming.Name bindDN(String _x) {
        @SuppressWarnings("deprecation")
        javax.naming.Name name = new DistinguishedName("uid=" + _x + ",ou=people");
        return name;
    }

    @Override
    public Iterable<User> findAllById(Iterable<Name> iterable) {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see ldap.advance.example.UserRepositoryIntf#getAllUsers()
     */
    @SuppressWarnings("deprecation")
    @Override
    public List<User> findAll() {
        log.info("executing {getAllUsers}");
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        return ldapTemplate.search(DistinguishedName.EMPTY_PATH, "(objectclass=person)", controls, new UserAttributesMapper());
    }

    @Override
    public Iterable<User> findAll(LdapQuery ldapQuery) {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see ldap.advance.example.UserRepositoryIntf#getUserDetails(java.lang.String)
     */
    public User getUserDetails(String userName) {
        log.info("executing {getUserDetails}");
        List<User> list = ldapTemplate.search(query().base("ou=people").where("uid").is(userName), new UserAttributesMapper());
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
        List<String> results = ldapTemplate.search(query().base("ou=people").where("uid").is(userName), new MultipleAttributesMapper());
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
        LdapQuery query = query().base("ou=people");
        List<String> list = ldapTemplate.list(query.base());
        log.info("Users -> " + list);
        return ldapTemplate.search(query().base("ou=people").where("objectClass").is("person"), new SingleAttributesMapper());
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ldap.advance.example.UserRepositoryIntf#createUser(ldap.advance.example.User)
     */
    public boolean createUser(User user) {
        log.info("executing {createUser}");
        Attribute objectClass = new BasicAttribute("objectClass");
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
        userAttributes.put("userPassword", user.getPassword().getBytes());
        ldapTemplate.bind(bindDN(user.getUsername()), null, userAttributes);
        return true;
    }

    @Override
    public <S extends User> S save(S s) {
        return null;
    }

    @Override
    public <S extends User> Iterable<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see ldap.advance.example.UserRepositoryIntf#remove(java.lang.String)
     */
    public boolean remove(String uid) {
        ldapTemplate.unbind(bindDN(uid));
        return true;
    }

    @Override
    public void deleteById(Name name) {
    }

    @Override
    public void delete(User user) {
    }

    @Override
    public void deleteAll(Iterable<? extends User> iterable) {
    }

    @Override
    public void deleteAll() {
    }

    /**
     * This class is responsible to prepare User object after ldap search.
     *
     * @author
     */
    private class UserAttributesMapper implements AttributesMapper<User> {

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

    /**
     * This class is responsible to print only cn .
     *
     * @author
     */
    private class SingleAttributesMapper implements AttributesMapper<String> {

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
    private class MultipleAttributesMapper implements AttributesMapper<String> {

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
