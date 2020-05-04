package com.isd.parking.storage.ldap;

import com.isd.parking.models.users.UserLdap;
import com.isd.parking.security.PasswordEncoding.CustomBcryptPasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.query.SearchScope;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.naming.Name;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import java.time.LocalDateTime;
import java.util.List;

import static com.isd.parking.storage.ldap.LdapAttributeMappers.*;
import static com.isd.parking.storage.ldap.LdapConstants.*;
import static org.springframework.ldap.query.LdapQueryBuilder.query;


/**
 * UserLdap Service class for LDAP storage
 * Contains methods for
 * - authenticate user,
 * - search user by uid and other attributes
 * - create new user entry in ldap repository,
 * - modify existed user attributes,
 * - delete user,
 * - get all users from LDAP storage etc.
 */
@Service
@Slf4j
public class UserLdapClient {

    private final LdapTemplate ldapTemplate;

    private final CustomBcryptPasswordEncoder passwordEncoder;

    @Value("${spring.ldap.base}")
    private String ldapSearchBase;

    @Value("${ldap.passwordAttribute}")
    private String ldapPasswordAttribute;

    @Autowired
    public UserLdapClient(@Qualifier(value = "ldapTemplate") LdapTemplate ldapTemplate,
                          CustomBcryptPasswordEncoder passwordEncoder) {
        this.ldapTemplate = ldapTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Method authenticates user with given credentials
     *
     * @param uid      - user name
     * @param password - user password
     * @return - success or denied status of user authentication
     */
    public Boolean authenticate(String uid, @NotNull String password) {
        @NotNull AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter(USER_UID_ATTRIBUTE, uid));

        return ldapTemplate.authenticate(ldapSearchBase, filter.encode(), passwordEncoder.encode(password));
    }

    /**
     * Build user LDAP server domain name
     * Used for bind user to LDAP server (using file access template or remote server)
     *
     * @param user - target user
     * @return LDAP server user domain name
     */
    private Name buildDn(@NotNull UserLdap user) {
        return LdapNameBuilder.newInstance()
            .add("ou", "people")
            .add(USER_UID_ATTRIBUTE, user.getUid())
            .build();
    }

    /**
     * Build user LDAP server domain name by user id
     * Used for bind user to LDAP server (using file access template or remote server)
     *
     * @param uid - target user id
     * @return LDAP server user domain name
     */
    private Name bindDnByUid(@NotNull String uid) {
        return LdapNameBuilder.newInstance()
            .add("ou", "people")
            .add(USER_UID_ATTRIBUTE, uid)
            .build();
    }

    /**
     * Create new user in LDAP server
     *
     * @param user - target user for creating
     * @return operation result
     */
    public boolean createUser(@NotNull UserLdap user) {
        ldapTemplate.bind(buildDn(user), null, buildAttributes(user));
        return true;
    }

    /**
     * Update user in LDAP server
     *
     * @param user - target user for updating
     * @return operation result
     */
    public boolean updateUser(@NotNull UserLdap user) {
        ldapTemplate.rebind(buildDn(user), null, buildAttributes(user));
        return true;
    }

    /**
     * Modify LDAP user attribute value
     *
     * @param uid              - target user id
     * @param attributeName    - specified attribute name
     * @param attributeValue   - specified attribute value
     * @param modificationType - modify or add directory context attribute
     * @return operation result
     */
    public boolean updateUser(@NotNull String uid,
                              @Nullable String attributeName,
                              @Nullable String attributeValue,
                              int modificationType) {
        Attribute attr = new BasicAttribute(attributeName, attributeValue);
        log.info(attributeName + " " + attributeValue);
        log.info(uid + " " + uid);
        @NotNull ModificationItem item = new ModificationItem(modificationType, attr);
        ldapTemplate.modifyAttributes(bindDnByUid(uid), new ModificationItem[]{item});

        return true;
    }

    /**
     * Modify LDAP user name (in this case uid)
     *
     * @param username    - target user unique username
     * @param newUsername - user new uid
     * @return operation result
     */
    public boolean updateUsername(@NotNull String username, @NotNull String newUsername) {
        ldapTemplate.rename(bindDnByUid(username), bindDnByUid(newUsername));
        return true;
    }

    /**
     * Modify LDAP user password
     *
     * @param user     - target LDAP user
     * @param password - target user password
     * @return operation result
     */
    public boolean updateUserPassword(@NotNull UserLdap user, String password) {
        this.updateUser(user.getUid(), USER_PASSWORD_ATTRIBUTE, password, DirContext.REPLACE_ATTRIBUTE);
        return true;
    }

    /**
     * Delete user from LDAP server
     *
     * @param user - target user
     * @return operation result
     */
    public boolean deleteUser(@NotNull UserLdap user) {
        ldapTemplate.unbind(buildDn(user));
        return true;
    }

    /**
     * Delete user by id from LDAP server
     *
     * @param uid - target user id
     * @return operation result
     */
    public boolean deleteUserById(@NotNull String uid) {
        ldapTemplate.unbind(bindDnByUid(uid));
        return true;
    }

    /**
     * Method get user names list by given uid
     *
     * @param uid - user name
     * @return - List of user names equals with given
     */
    public List<String> getUserNameById(final String uid) {
        return getAttributes(uid, "cn");
    }

    /**
     * Retrieve user membership authorities by user id
     * Used for setting JWT
     *
     * @param uid - target user id
     * @return string representation of user membership authorities
     */
    public @org.jetbrains.annotations.Nullable String getAuthoritiesMembershipById(final String uid) {
        List<String> rolesList = ldapTemplate.search(
            ldapSearchBase, USER_UID_ATTRIBUTE + "=" + uid, new AuthoritiesAttributesMapper());
        if (rolesList != null && !rolesList.isEmpty()) {
            return rolesList.get(0);
        }
        return null;
    }

    /**
     * Search user by id (make sure it is available on server)
     *
     * @param uid - target user id
     * @return operation result
     */
    public boolean searchUser(final String uid) {
        return !getUserNameById(uid).isEmpty();
    }

    /**
     * Get user by id
     *
     * @param uid - target user id
     * @return user found
     */
    public @org.jetbrains.annotations.Nullable UserLdap findById(@NotNull String uid) {
        UserLdap user;
        try {
            user = ldapTemplate.lookup(bindDnByUid(uid), new UserAttributesMapper());
        } catch (org.springframework.ldap.NameNotFoundException e) {
            return null;
        }
        return user;
    }

    /**
     * Get user by domain name
     *
     * @param dn - target user domain name
     * @return user found
     */
    public @org.jetbrains.annotations.Nullable UserLdap findByDn(String dn) {
        UserLdap user;
        try {
            user = ldapTemplate.lookup(dn, new UserAttributesMapper());
        } catch (org.springframework.ldap.NameNotFoundException e) {
            return null;
        }
        return user;
    }

    /**
     * Get all users request method
     *
     * @return - list of all users
     */
    public List<UserLdap> findAll() {
        return ldapTemplate.search(query()
            .where(OBJECT_CLASS).is("person"), new UserAttributesMapper());
    }

    /**
     * Gets string representation of all LDAP server users
     *
     * @return string representation of all LDAP server users
     */
    public @org.jetbrains.annotations.Nullable List<String> getAllUsersDetails() {
        List<String> list = ldapTemplate.list(
            query().base());
        if (list != null && !list.isEmpty()) {
            return list;
        }
        return null;
    }

    /**
     * Gets string representation of specified LDAP server user by it's id
     *
     * @param uid - target user id
     * @return string representation of specified LDAP server user by it's id
     */
    public String getUserDetails(String uid) {
        List<String> results = ldapTemplate.search(
            query().base(ldapSearchBase)
                .where(USER_UID_ATTRIBUTE).is(uid), new MultipleAttributesMapper());
        if (results != null && !results.isEmpty()) {
            return results.get(0);
        }
        return " UserDetails for " + uid + " not found .";
    }

    /**
     * Get all LDAP server users names
     *
     * @return list of existing users names
     */
    public List<String> getAllUserNames() {
        return ldapTemplate.search(
            query().base(ldapSearchBase).where(OBJECT_CLASS).is("person"), new SingleAttributesMapper());
    }

    /* Custom search */

    /**
     * Search user by social id (make sure it is available on server)
     *
     * @param id     - target usr social id
     * @param social - social service provider
     * @return operation result
     */
    public boolean searchUsersBySocialId(final String id, String social) {
        return !getUsersBySocialId(id, social).isEmpty();
    }

    /**
     * Search user by email (make sure it is available on server)
     *
     * @param email - target user's email
     * @return operation result
     */
    public boolean searchUsersByEmail(final String email) {
        return !getUsersByEmail(email).isEmpty();
    }

    /**
     * Get all users by social provider id
     * (In this application case not used, LDAP specification allows multiple identical secondary attributes)
     *
     * @param id     - target usr social id
     * @param social - social service provider
     * @return list of users found
     */
    public List<UserLdap> getUsersBySocialId(String id, String social) {
        LdapQuery query = getUserBySocialIdLdapQuery(id, social);

        return ldapTemplate.search(query, new UserAttributesMapper());
    }

    /**
     * Get user by given social provider id
     *
     * @param id     - target user social id
     * @param social - social service provider
     * @return user found
     */
    public @org.jetbrains.annotations.Nullable UserLdap getUserBySocialId(String id, String social) {
        LdapQuery query = getUserBySocialIdLdapQuery(id, social);
        List<UserLdap> results = ldapTemplate.search(query, new UserAttributesMapper());

        if (results != null && !results.isEmpty()) {
            return results.get(0);
        }
        return null;
    }

    /**
     * Get user with specified email
     *
     * @param email - target user email
     * @return user found
     */
    public @org.jetbrains.annotations.Nullable UserLdap getUserByEmail(String email) {
        List<UserLdap> users = getUsersByEmail(email);

        if (users != null && !users.isEmpty()) {
            return users.get(0);
        } else {
            return null;
        }
    }

    /**
     * Get all users with specified email
     * (in this application not allowed multiple users with same email,
     * but this method implemented for check reasons)
     *
     * @param email - target user email
     * @return list of found users
     */
    public List<UserLdap> getUsersByEmail(String email) {

        LdapQuery query = getBaseLdapQuery()
            .where(OBJECT_CLASS).is("person")
            .and("email").like(email)
            .and(USER_UID_ATTRIBUTE).isPresent();

        return ldapTemplate.search(query, new UserAttributesMapper());
    }

    /**
     * Forms social user get by id and social service provider LDAP query
     *
     * @param id     - target user social id
     * @param social - social service provider
     * @return social search query
     */
    private LdapQuery getUserBySocialIdLdapQuery(String id, String social) {
        return getBaseLdapQuery()
            .where(OBJECT_CLASS).is("person")
            .and(social + "id").like(id)
            .and(USER_UID_ATTRIBUTE).isPresent();
    }

    /**
     * Retrieve all LDAP server user full names
     *
     * @param id     - target user social id
     * @param social - social service provider
     * @return list of found user with full names
     */
    public List<UserLdap> getFullnameBySocialId(String id, String social) {
        LdapQuery query = getUserBySocialIdLdapQuery(id, social);

        return ldapTemplate.search(query, new UserAttributesMapperShort());
    }

    /**
     * Retrieve all LDAP server user names
     *
     * @param lastName - target user lastname
     * @return list of found users with names
     */
    public List<UserLdap> getUserNamesByLastName(String lastName) {

        LdapQuery query = getBaseLdapQuery()
            .attributes("cn")
            .where(OBJECT_CLASS).is("person")
            .and("sn").like(lastName)
            .and(USER_UID_ATTRIBUTE).isPresent();

        return ldapTemplate.search(query, new UserAttributesMapperShort());
    }

    /**
     * Forms standard base LDAP query
     *
     * @return standard base LDAp query
     */
    private @NotNull LdapQueryBuilder getBaseLdapQuery() {
        return query()
            .searchScope(SearchScope.SUBTREE)
            .timeLimit(SEARCH_TIME_LIMIT_MS)
            .countLimit(3)
            .base(LdapUtils.emptyLdapName());
    }

    /**
     * Get passwordUpdatedAt user LDAp entry attribute value
     *
     * @param uid - target user id
     * @return password updated at date
     */
    public @org.jetbrains.annotations.Nullable LocalDateTime getPasswordUpdateAt(String uid) {

        List<String> attributes = getAttributes(uid, "passwordUpdatedAt");

        if (!attributes.isEmpty()) {
            return LocalDateTime.parse(attributes.get(0));
        } else {
            return null;
        }
    }

    /**
     * Get specified LDAP user attribute value by user id
     *
     * @param uid           - target user id
     * @param attributeName - target attribute name
     * @return list of found users correspond attribute values
     */
    private List<String> getAttributes(String uid, String attributeName) {

        return ldapTemplate.search(
            ldapSearchBase,
            USER_UID_ATTRIBUTE + "=" + uid,
            (AttributesMapper<String>) attrs -> (String) attrs
                .get(attributeName)
                .get());
    }

    /**
     * Connect given social provider id to user account
     *
     * @param uid            - target user username
     * @param id             - target user social id
     * @param socialProvider - social service provider
     * @return operation result
     */
    public boolean connectSocialProvider(String uid, String socialProvider, String id) {
        this.updateUser(uid, socialProvider, id, DirContext.ADD_ATTRIBUTE);
        return true;
    }

    /**
     * Disconnect given social provider id to user account
     *
     * @param uid            - target user username
     * @param socialProvider - social service provider
     * @return operation result
     */
    public boolean disconnectSocialProvider(String uid, String socialProvider) {
        this.updateUser(uid, socialProvider, null, DirContext.REMOVE_ATTRIBUTE);
        return true;
    }
}
