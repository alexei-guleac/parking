package com.isd.parking.models.users;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isd.parking.models.enums.AccountState;
import com.isd.parking.security.AccountConfirmationPeriods;
import com.isd.parking.utils.ReflectionMethods;
import lombok.Data;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static com.isd.parking.security.PasswordEncoding.CustomBcryptPasswordEncoder;
import static com.isd.parking.utils.AppDateUtils.isDateAfterNow;
import static com.isd.parking.utils.AppDateUtils.isDateBeforeNow;
import static com.isd.parking.utils.AppStringUtils.isCyrillicString;
import static com.isd.parking.utils.AppStringUtils.transliterateCyrillicToLatin;
import static com.isd.parking.utils.ReflectionMethods.setPropertyValue;
import static org.apache.commons.lang.StringUtils.strip;


@Entry(base = "ou=people",
    objectClasses = {"top", "person", "organizationalPerson", "inetOrgPerson"})
@Data
public class UserLdap {

    @JsonIgnore
    @Id
    private Name id;

    @JsonProperty()
    @JsonAlias({"uid"})
    private @Attribute(name = "uid")
    @DnAttribute(value = "uid")
    String uid;

    @JsonProperty()
    @JsonAlias({"accountState"})
    private @Attribute(name = "accountState")
    AccountState accountState;

    @JsonProperty()
    @JsonAlias({"cn"})
    private @Attribute(name = "cn")
    String cn;

    @JsonProperty()
    @JsonAlias({"sn"})
    private @Attribute(name = "sn")
    String sn;

    @JsonProperty()
    @JsonAlias({"email"})
    @Email
    private @Attribute(name = "email")
    String email;

    @JsonProperty()
    @JsonAlias({"userPassword"})
    private @Attribute(name = "userPassword")
    String userPassword;

    private @Attribute(name = "creationDate")
    LocalDateTime creationDate;

    private @Attribute(name = "updatedAt")
    LocalDateTime updatedAt;

    private @Attribute(name = "passwordUpdatedAt")
    LocalDateTime passwordUpdatedAt;

    /* Social id's*/

    @JsonProperty()
    @JsonAlias({"fbid"})
    private @Attribute(name = "fbid")
    String fbid;

    @JsonProperty()
    @JsonAlias({"gid"})
    private @Attribute(name = "gid")
    String gid;

    public final static ArrayList<String> userLdapClassAttributesList =
        (ArrayList<String>) new ReflectionMethods().getFieldsNames(UserLdap.class);

    @JsonProperty()
    @JsonAlias({"msid"})
    private @Attribute(name = "msid")
    String msid;

    /*@JsonProperty()
    @JsonAlias({"twid"})
    private @Attribute(name = "twid")
    String twid;*/

    @JsonProperty()
    @JsonAlias({"gitid"})
    private @Attribute(name = "gitid")
    String gitid;

    @JsonProperty()
    @JsonAlias({"aid"})
    private @Attribute(name = "aid")
    String aid;

    /*@JsonProperty()
    @JsonAlias({"linkid"})
    private @Attribute(name = "linkid")
    String linkid;*/

    @JsonProperty()
    @JsonAlias({"vkid"})
    private @Attribute(name = "vkid")
    String vkid;

    public UserLdap() {
    }

    public UserLdap(String uid, String userPassword) {
        this.uid = uid;
        this.userPassword = userPassword;
    }

    public UserLdap(UserLdap user, String userPassword) {
        this.id = user.id;
        this.uid = user.uid;
        this.cn = user.cn;
        this.sn = user.sn;
        this.email = user.email;
        this.userPassword = userPassword;
        setCreatedNow();
    }

    public UserLdap(String uid, String cn, String sn, String userPassword) {
        this.uid = uid;
        this.cn = cn;
        this.sn = sn;
        this.userPassword = userPassword;
    }

    public UserLdap(Name id, String uid, String cn, String sn, String email, String userPassword) {
        this.id = id;
        this.uid = uid;
        this.cn = cn;
        this.sn = sn;
        this.email = email;
        this.userPassword = userPassword;
        setCreatedNow();
    }

    public static void setUserLdapProperty(UserLdap user, String name, Object value) {
        ReflectionMethods.setPropertyValue(user, name, value);
    }

    private void setCreatedNow() {
        LocalDateTime now = LocalDateTime.now();
        this.creationDate = now;
        this.updatedAt = now;
        this.passwordUpdatedAt = now;
    }

    public void setUserRegistered() {
        if (this.userPassword != null) {
            this.userPassword = new CustomBcryptPasswordEncoder().encode(this.userPassword);
            System.out.println(" password QWERTY" + new CustomBcryptPasswordEncoder().encode(this.userPassword));
        }
        setCreatedNow();
        if (this.email != null) {
            setAccountState(AccountState.WAITING_CONFIRMATION);
        } else {
            setAccountState(AccountState.SOCIAL);
        }
    }

    public void setAccountConfirmed() {
        if (this.accountState == AccountState.WAITING_CONFIRMATION) {
            setAccountState(AccountState.ENABLED);
        }
        setUpdatedNow();
    }

    private void setUpdatedNow() {
        this.updatedAt = LocalDateTime.now();
    }

    public static void setUserLdapProperty(UserLdap user, String name, Attributes values) throws NamingException {
        // log.info(methodMsgStatic("set value " + values.get(name).get()));
        ReflectionMethods.setPropertyValue(user, name, values.get(name).get().toString());
    }

    public static Object getUserLdapProperty(UserLdap user, String name) {
        return ReflectionMethods.getPropertyValue(user, name);
    }

    private void checkCyrillicName() {
        if (this.cn != null) {
            if (isCyrillicString(strip(this.cn))) {
                this.cn = transliterateCyrillicToLatin(this.cn);
            }
        }
        if (this.sn != null) {
            if (isCyrillicString(strip(this.sn))) {
                this.sn = transliterateCyrillicToLatin(this.sn);
            }
        }
    }

    private void setSocialId(String id, String provider) {
        String social = provider + "id";
        setPropertyValue(this, social, id);
        // log.info(methodMsgStatic("getPropertyValue: " + getPropertyValue(this, social)));
    }

    public void prepareSocialUser(String id, String provider) {
        setUid("social-" + UUID.randomUUID().toString().split("-")[0]);
        setSocialId(id, provider);
        System.out.println();
        checkCyrillicName();
        System.out.println("prepareSocialUser");
    }

    public static String getUserLdapStringProperty(UserLdap user, String name) {
        return ReflectionMethods.getStringPropertyValue(user, name);
    }

    public String getFirstname() {
        if (this.cn != null) {
            return this.cn.split(" ")[0];
        } else return null;
    }

    public boolean accountConfirmationIsExpired() {
        return this.accountState == AccountState.WAITING_CONFIRMATION
            && isDateBeforeNow(getCreationDate().plusMinutes(AccountConfirmationPeriods.CONFIRM_TOKEN_EXP_IN_MINUTES), 0);
    }

    public boolean accountConfirmationValid() {
        return getAccountState() == AccountState.WAITING_CONFIRMATION
            && isDateAfterNow(getCreationDate().plusMinutes(AccountConfirmationPeriods.CONFIRM_TOKEN_EXP_IN_MINUTES), 0);
    }


}
