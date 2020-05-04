package com.isd.parking.models.users;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isd.parking.models.enums.AccountState;
import com.isd.parking.security.AccountConfirmationPeriods;
import com.isd.parking.utilities.ReflectionMethods;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.isd.parking.security.PasswordEncoding.CustomBcryptPasswordEncoder;
import static com.isd.parking.utilities.AppDateUtils.isDateAfterNow;
import static com.isd.parking.utilities.AppDateUtils.isDateBeforeNow;
import static com.isd.parking.utilities.AppStringUtils.isCyrillicString;
import static com.isd.parking.utilities.AppStringUtils.transliterateCyrillicToLatin;
import static org.apache.commons.lang.StringUtils.strip;


/**
 * LDAP database user representation
 */
@Entry(base = "ou=people",
    objectClasses = {"top", "person", "organizationalPerson", "inetOrgPerson"})
@Data
@ApiModel(description = "LDAP user entry model. ")
public class UserLdap {

    public final static ArrayList<String> userLdapClassFieldsList =
        (ArrayList<String>) new ReflectionMethods().getFieldsNames(UserLdap.class);

    @JsonIgnore
    @Id
    @ApiModelProperty(notes = "Unique LDAP processing ID")
    private Name id;

    @JsonProperty()
    @JsonAlias({"uid"})
    @ApiModelProperty(notes = "User server uid")
    private @Attribute(name = "uid")
    @DnAttribute(value = "uid")
    String uid;

    @JsonProperty()
    @JsonAlias({"accountState"})
    @ApiModelProperty(notes = "User account state (enabled, disabled, waiting confirmation)")
    private @Attribute(name = "accountState")
    AccountState accountState;

    @JsonProperty()
    @JsonAlias({"cn"})
    @ApiModelProperty(notes = "User full name")
    private @Attribute(name = "cn")
    String cn;

    @JsonProperty()
    @JsonAlias({"sn"})
    @ApiModelProperty(notes = "User lastname")
    private @Attribute(name = "sn")
    String sn;

    @JsonProperty()
    @JsonAlias({"email"})
    @ApiModelProperty(notes = "User email")
    @Email
    private @Attribute(name = "email")
    String email;

    @JsonProperty()
    @JsonAlias({"userPassword"})
    @ApiModelProperty(notes = "User password")
    private @Attribute(name = "userPassword")
    String userPassword;

    @ApiModelProperty(notes = "User account creation date")
    private @Attribute(name = "creationDate")
    LocalDateTime creationDate;

    @ApiModelProperty(notes = "User account last updated at date")
    private @Attribute(name = "updatedAt")
    LocalDateTime updatedAt;

    @ApiModelProperty(notes = "User password last updated at date")
    private @Attribute(name = "passwordUpdatedAt")
    LocalDateTime passwordUpdatedAt;

    /* Social id's*/
    @JsonProperty()
    @JsonAlias({"social_ids"})
    @ApiModelProperty(notes = "Map of user social id's by social service providers")
    private Map<String, String> socialIds = new HashMap<>();

    public UserLdap() {
    }

    public UserLdap(String uid, String userPassword) {
        this.uid = uid;
        this.userPassword = userPassword;
    }

    public UserLdap(@NotNull UserLdap user, String userPassword) {
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

    public static void setUserLdapProperty(UserLdap user, String name, @NotNull Attributes values) throws NamingException {
        ReflectionMethods.setPropertyValue(user, name, values.get(name).get().toString());
    }

    public void setUserRegistered() {
        if (this.userPassword != null) {
            this.userPassword = new CustomBcryptPasswordEncoder().encode(this.userPassword);
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

    public static @Nullable String getUserLdapStringProperty(UserLdap user, String name) {
        return ReflectionMethods.getStringPropertyValue(user, name);
    }

    public static Object getUserLdapProperty(UserLdap user, String name) {
        return ReflectionMethods.getPropertyValue(user, name);
    }

    /**
     * Check user name if is in Cyrillic symbols and transliterate it to Latin representation
     */
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

    private void setCreatedNow() {
        @NotNull LocalDateTime now = LocalDateTime.now();
        this.creationDate = now;
        this.updatedAt = now;
        this.passwordUpdatedAt = now;
    }

    /**
     * Creates random social user UID and check user name necessity to transliterate
     *
     * @param id       - social service ID
     * @param provider - social provider
     */
    public void prepareSocialUser(String id, String provider) {
        setUid("social-" + UUID.randomUUID().toString().split("-")[0]);
        setSocialId(id, provider);
        checkCyrillicName();
    }

    private void setSocialId(String id, String provider) {
        // @NotNull String social = provider + "id";
        this.socialIds.put(provider, id);
    }

    public @Nullable String getFirstname() {
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
