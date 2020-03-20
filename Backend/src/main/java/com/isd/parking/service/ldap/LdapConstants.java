package com.isd.parking.service.ldap;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public
class LdapConstants {

    static String LDAP_BASE_DN;
    static String LDIF_FILE_PATH;
    static String USER_PASSWORD_ATTRIBUTE = "userPassword";
    private static String LDIF_FILE_NAME;

    @Value("${spring.ldap.embedded.ldif}")
    public void setLdifFileName(String ldifFileName) {
        LDIF_FILE_NAME = ldifFileName.split(":")[1].strip();
        log.info(" LDIF_FILE_NAME " + LDIF_FILE_NAME);
        LDIF_FILE_PATH = System.getProperty("user.dir") + "\\src\\main\\resources\\" + LDIF_FILE_NAME;
    }

    @Value("${spring.ldap.embedded.base-dn}")
    public void setLdapBaseDn(String ldapBaseDn) {
        LDAP_BASE_DN = ldapBaseDn;
    }

    @Value("${ldap.passwordAttribute}")
    public void setLdapUserPasswordAttribute(String userPasswordAttribute) {
        USER_PASSWORD_ATTRIBUTE = userPasswordAttribute;
    }

    static final String OBJECT_CLASS = "objectClass";

    static final String[] personObjectClasses = {"top", "person", "organizationalPerson", "inetOrgPerson"};

    static final Integer SEARCH_TIME_LIMIT_MS = 3000;

}
