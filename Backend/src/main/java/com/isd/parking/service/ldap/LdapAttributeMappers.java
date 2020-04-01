package com.isd.parking.service.ldap;

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

import static com.isd.parking.service.ldap.LdapConstants.*;
import static com.isd.parking.utils.ColorConsoleOutput.methodMsgStatic;


@Slf4j
public class LdapAttributeMappers {

    private final static ArrayList<String> userLdapClassAttributesList =
        (ArrayList<String>) new ReflectionMethods().getFieldsNames(UserLdap.class);

    static Attributes buildAttributes(UserLdap user) {
        Attribute objectClass = new BasicAttribute("objectClass");
        {
            for (String objClass : personObjectClasses) {
                objectClass.add(objClass);
            }
        }
        Attributes userAttributes = new BasicAttributes();
        userAttributes.put(objectClass);

        log.info("fields " + userLdapClassAttributesList);
        for (String attribute : userLdapClassAttributesList) {
            if (attribute.equals("userPassword")) {
                if (user.getUserPassword() != null) {
                    userAttributes.put("userPassword", user.getUserPassword().getBytes());
                }
            }
            String propertyValue = getUserLdapStringProperty(user, attribute);

            log.info(methodMsgStatic("propertyValue " + propertyValue));
            if (propertyValue != null) {
                userAttributes.put(attribute, propertyValue);
            }
        }
        log.info("userAttributes " + userAttributes);

        return userAttributes;
    }

    private static LinkedHashMap<String, Object> buildAttributesMap(UserLdap user) {
        return new LinkedHashMap<>() {
            {
                log.info("fields " + userLdapClassAttributesList);
                for (String attribute : userLdapClassAttributesList) {
                    Object propertyValue = getUserLdapProperty(user, attribute);
                    if (propertyValue != null) {
                        if (attribute.equals("userPassword")) {
                            String pass = user.getUserPassword();
                            log.info(methodMsgStatic("userPassword buildAttributesMap VVVVV " + pass));
                            put(attribute, pass);
                        } else {
                            put(attribute, propertyValue);
                        }
                    }
                }
            }
        };
    }

    public static Entry mapUserToEntry(UserLdap user) {
        Entry userEntry = null;
        log.info(methodMsgStatic("ENTRRRRRRRRRy "));
        try {
            userEntry = new Entry(new DN("uid=" + user.getUid() + ",ou=people," + LDAP_BASE_DN));
            userEntry.addAttribute(OBJECT_CLASS, personObjectClasses);

            // bind attributes
            log.info(methodMsgStatic("mapped " + buildAttributesMap(user).entrySet()));
            for (Map.Entry<String, Object> entry : buildAttributesMap(user).entrySet()) {
                userEntry.addAttribute(entry.getKey(), String.valueOf(entry.getValue()));
            }
        } catch (LDAPException e) {
            e.printStackTrace();
        }
        log.info(methodMsgStatic("ENTRRRRRRRRRy " + userEntry));
        return userEntry;
    }

    private static void setUserLdapProperty(UserLdap user, String name, Object value) {
        ReflectionMethods.setPropertyValue(user, name, value);
    }

    private static void setUserLdapProperty(UserLdap user, String name, Attributes values) throws NamingException {
        log.info(methodMsgStatic("set value " + values.get(name).get()));
        ReflectionMethods.setPropertyValue(user, name, values.get(name).get().toString());
    }

    private static Object getUserLdapProperty(UserLdap user, String name) {
        return ReflectionMethods.getPropertyValue(user, name);
    }

    private static String getUserLdapStringProperty(UserLdap user, String name) {
        return ReflectionMethods.getStringPropertyValue(user, name);
    }

    /**
     * Custom user attributes mapper, maps the attributes to the person POJO
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
     * This class is responsible to prepare UserLdap object after ldap search.
     *
     * @author
     */
    static class UserAttributesMapper implements AttributesMapper<UserLdap> {

        @Override
        public UserLdap mapFromAttributes(Attributes attributes) throws NamingException {
            UserLdap user;
            if (attributes == null) {
                return null;
            }
            user = new UserLdap();
            log.info(methodMsgStatic("mapFromAttributes " + attributes));
            for (String attributeName : userLdapClassAttributesList) {
                if (attributes.get(attributeName) != null) {
                    if (attributeName.equals("accountState")) {
                        log.info(methodMsgStatic("acc state value " + attributes.get(attributeName).get()));
                        setUserLdapProperty(user, attributeName, AccountState.valueOf(getAttributeStringValue(attributes, attributeName)));
                    } else if (ReflectionMethods.getPropertyType(user, attributeName) == LocalDateTime.class) {
                        setUserLdapProperty(user, attributeName, LocalDateTime.parse(getAttributeStringValue(attributes, attributeName)));
                    } else {
                        setUserLdapProperty(user, attributeName, attributes);
                    }
                }
            }

            return user;
        }

        private String getAttributeStringValue(Attributes attributes, String attributeName) throws NamingException {
            return (String) attributes.get(attributeName).get();
        }
    }

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
     * This class is responsible to print only cn .
     *
     * @author
     */
    static class SingleAttributesMapper implements AttributesMapper<String> {

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
