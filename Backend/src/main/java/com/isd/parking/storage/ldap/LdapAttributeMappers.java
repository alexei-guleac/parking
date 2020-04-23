package com.isd.parking.storage.ldap;

import com.isd.parking.models.enums.AccountState;
import com.isd.parking.models.users.UserLdap;
import com.isd.parking.utils.AppStringUtils;
import com.isd.parking.utils.ReflectionMethods;
import com.unboundid.ldap.sdk.DN;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.support.AbstractContextMapper;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.isd.parking.models.users.UserLdap.*;
import static com.isd.parking.storage.ldap.LdapConstants.*;


/**
 * Different attributes mappers for LDAP users objects and entities
 */
@Slf4j
class LdapAttributeMappers {

    /**
     * Builds LDAP attributes object from specified LDAP user entry
     * Used for user binding in LDAP template (create, update)
     *
     * @param user - specified LDAP user
     * @return LDAP attributes
     */
    static Attributes buildAttributes(UserLdap user) {
        Attribute objectClass = new BasicAttribute("objectClass");
        {
            for (String objClass : personObjectClasses) {
                objectClass.add(objClass);
            }
        }
        Attributes userAttributes = new BasicAttributes();
        userAttributes.put(objectClass);

        for (String field : userLdapClassFieldsList) {
            // write password in bytes (required by Spring LDAP)
            if (field.equals("userPassword")) {
                if (user.getUserPassword() != null) {
                    userAttributes.put("userPassword", user.getUserPassword().getBytes());
                }
            }
            String propertyValue = getUserLdapStringProperty(user, field);
            if (propertyValue != null) {
                userAttributes.put(field, propertyValue);
            }
        }

        return userAttributes;
    }

    /**
     * Builds user fields map from user
     * Used for map user to LDAP file entry
     *
     * @param user - specified LDAP user
     * @return map of user fields
     */
    private static LinkedHashMap<String, Object> buildFieldsMap(UserLdap user) {
        return new LinkedHashMap<>() {
            {
                for (String field : userLdapClassFieldsList) {
                    Object propertyValue = getUserLdapProperty(user, field);
                    if (propertyValue != null) {
                        if (field.equals("userPassword")) {
                            String pass = user.getUserPassword();
                            put(field, pass);
                        } else {
                            put(field, propertyValue);
                        }
                    }
                }
            }
        };
    }

    /**
     * Converts LDAP user to LDAP file entry
     *
     * @param user - target user
     * @return - LDAP file entry
     */
    public static Entry mapUserToEntry(UserLdap user) {
        Entry userEntry = null;

        try {
            userEntry = new Entry(new DN("uid=" + user.getUid() + ",ou=people," + LDAP_BASE_DN));
            userEntry.addAttribute(OBJECT_CLASS, personObjectClasses);
            // bind attributes
            for (Map.Entry<String, Object> entry : buildFieldsMap(user).entrySet()) {
                userEntry.addAttribute(entry.getKey(), String.valueOf(entry.getValue()));
            }
        } catch (LDAPException e) {
            e.printStackTrace();
        }
        return userEntry;
    }


    /**
     * Custom user attributes mapper, maps the attributes to the user POJO, map only user CN and SN
     */
    static class UserAttributesMapperShort implements AttributesMapper<UserLdap> {
        @Override
        public UserLdap mapFromAttributes(Attributes attrs) throws NamingException {
            UserLdap user = new UserLdap();

            Attribute cn = attrs.get("cn");
            if (cn != null) {
                user.setCn((String) cn.get());
            }
            Attribute sn = attrs.get("sn");
            if (sn != null) {
                user.setSn((String) sn.get());
            }
            return user;
        }
    }

    /**
     * This class is responsible to prepare UserLdap object after LDAP search.
     */
    static class UserAttributesMapper implements AttributesMapper<UserLdap> {

        @Override
        public UserLdap mapFromAttributes(Attributes attributes) throws NamingException {
            UserLdap user;
            if (attributes == null) {
                return null;
            }
            user = new UserLdap();

            for (String field : userLdapClassFieldsList) {
                if (attributes.get(field) != null) {
                    if (field.equals("accountState")) {
                        setUserLdapProperty(user, field, AccountState.valueOf(getAttributeStringValue(attributes, field)));
                    } else if (ReflectionMethods.getPropertyType(user, field) == LocalDateTime.class) {
                        setUserLdapProperty(user, field, LocalDateTime.parse(getAttributeStringValue(attributes, field)));
                    } else {
                        setUserLdapProperty(user, field, attributes);
                    }
                }
            }

            return user;
        }

        /**
         * Returns String representation of lDAP attribute
         *
         * @param attributes    - user LDAP attributes
         * @param attributeName - target attribute name
         * @return String representation of lDAP attribute
         * @throws NamingException
         */
        private String getAttributeStringValue(Attributes attributes, String attributeName) throws NamingException {
            return (String) attributes.get(attributeName).get();
        }
    }

    /**
     * Maps user from context
     */
    static class UserContextMapper extends AbstractContextMapper<UserLdap> {
        public UserLdap doMapFromContext(DirContextOperations context) {
            UserLdap user = new UserLdap();
            user.setCn(context.getStringAttribute("cn"));
            user.setSn(context.getStringAttribute("sn"));
            user.setUid(context.getStringAttribute("uid"));

            return user;
        }
    }

    /**
     * This class is responsible to print only cn.
     */
    static class SingleAttributesMapper implements AttributesMapper<String> {

        @Override
        public String mapFromAttributes(Attributes attrs) {
            Attribute cn = attrs.get("cn");
            return cn.toString();
        }
    }

    /**
     * This class is responsible to print all user LDAP attributes in string format.
     */
    static class MultipleAttributesMapper implements AttributesMapper<String> {

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

    /**
     * Maps LDAP user groups membership to string roles representation
     * Used to retrieve user roles from LDAP server file to include it in JWT
     */
    static class AuthoritiesAttributesMapper implements AttributesMapper<String> {

        @Override
        public String mapFromAttributes(Attributes attrs) throws NamingException {
            List<String> rolesList = new ArrayList<>();

            Attribute attr = attrs.get("memberOf");
            if (attr != null) {
                for (int i = 0; i < attr.size(); i++) {
                    String s = (String) attr.get(i);
                    String[] str = s.split(",");
                    str = str[0].split("=");
                    rolesList.add("ROLE_" + str[1]);
                }
            }
            return new AppStringUtils().collectionToString(rolesList);
        }
    }
}
