package com.isd.parking.storage.ldap;


import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class LdapConstants {

    static String LDAP_BASE_DN;

    static String LDIF_FILE_PATH;

    public static final String[] personObjectClasses = {"top", "person", "organizationalPerson", "inetOrgPerson"};

    // LDAP user objectClass attribute name
    static final String OBJECT_CLASS = "objectClass";

    private static String LDIF_FILE_NAME;

    // LDAP user id attribute name
    public static final String USER_UID_ATTRIBUTE = "uid";

    @Value("${spring.ldap.embedded.base-dn}")
    public void setLdapBaseDn(String ldapBaseDn) {
        LDAP_BASE_DN = ldapBaseDn;
    }

    @Value("${ldap.passwordAttribute}")
    public void setLdapUserPasswordAttribute(String userPasswordAttribute) {
        USER_PASSWORD_ATTRIBUTE = userPasswordAttribute;
    }

    // LDAP query search time limit
    static final Integer SEARCH_TIME_LIMIT_MS = 3000;

    // LDAP user social id's attribute name
    public static final String USER_SOCIALS_ATTRIBUTE = "socialIds";

    // LDAP user password attribute name
    static String USER_PASSWORD_ATTRIBUTE = "userPassword";

    // LDAP user account state attribute name
    public static final String USER_ACCOUNT_STATE_ATTRIBUTE = "accountState";

    // LDAP user membership attribute name
    static final String USER_MEMBER_ATTRIBUTE = "memberOf";

    @Value("${spring.ldap.embedded.ldif}")
    public void setLdifFileName(@NotNull String ldifFileName) {
        LDIF_FILE_NAME = ldifFileName.split(":")[1].strip();
        LDIF_FILE_PATH = System.getProperty("user.dir") + "\\src\\main\\resources\\" + LDIF_FILE_NAME;
    }
}
