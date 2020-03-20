package com.isd.parking.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isd.parking.models.enums.AccountState;
import com.isd.parking.security.AccountConfirmationPeriods;
import lombok.Data;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;

import static com.isd.parking.security.PasswordEncoding.CustomBcryptPasswordEncoder;
import static com.isd.parking.utils.AppDateUtils.isAfterNow;
import static com.isd.parking.utils.AppDateUtils.isBeforeNow;


@Entry(base = "ou=people",
    objectClasses = {"top", "person", "organizationalPerson", "inetOrgPerson"})
@Data
public class UserLdap extends BaseEmailUser {

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
    private @Attribute(name = "mail")
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

    /*@JsonProperty()
    @JsonAlias({"fbid"})
    private @Attribute(name = "fbid")
    String fbid;

    @JsonProperty()
    @JsonAlias({"gid"})
    private @Attribute(name = "gid")
    String gid;

    @JsonProperty()
    @JsonAlias({"twid"})
    private @Attribute(name = "twid")
    String twid;

    @JsonProperty()
    @JsonAlias({"mid"})
    private @Attribute(name = "mid")
    String mid;

    @JsonProperty()
    @JsonAlias({"gitid"})
    private @Attribute(name = "gitid")
    String gitid;

    @JsonProperty()
    @JsonAlias({"instid"})
    private @Attribute(name = "instid")
    String instid;

    @JsonProperty()
    @JsonAlias({"aid"})
    private @Attribute(name = "aid")
    String aid;

    @JsonProperty()
    @JsonAlias({"vkid"})
    private @Attribute(name = "vkid")
    String vkid;

    @JsonProperty()
    @JsonAlias({"linkid"})
    private @Attribute(name = "linkid")
    String linkid;*/

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

    private void setCreatedNow() {
        LocalDateTime now = LocalDateTime.now();
        this.creationDate = now;
        this.updatedAt = now;
        this.passwordUpdatedAt = now;
    }

    public void setUserRegistered() {
        setUserPassword(new CustomBcryptPasswordEncoder().encode(getUserPassword()));
        System.out.println(" password" + new CustomBcryptPasswordEncoder().encode(getUserPassword()));
        setCreatedNow();
        setAccountState(AccountState.WAITING_CONFIRMATION);
    }

    public void setAccountEnabled() {
        setAccountState(AccountState.ENABLED);
        setUpdatedNow();
    }

    private void setUpdatedNow() {
        this.updatedAt = LocalDateTime.now();
    }

    public boolean accountConfirmationIsExpired() {

        // log.info(methodMsgStatic("getAccountState() == AccountState.WAITING_CONFIRMATION " + (getAccountState() == AccountState.WAITING_CONFIRMATION)));
        // log.info(methodMsgStatic("isBeforeNow(getCreationDate().plusDays(AccountConfirmationPeriods.CONFIRM_TOKEN_EXP_IN_MINUTES - 1) " + isBeforeNow(getCreationDate().plusDays(AccountConfirmationPeriods.CONFIRM_TOKEN_EXP_IN_MINUTES),1)));
        // log.info(methodMsgStatic("getCreationDate().plusDays(AccountConfirmationPeriods.CONFIRM_TOKEN_EXP_IN_MINUTES - 1) " + isBeforeNow(getCreationDate().plusDays(AccountConfirmationPeriods.CONFIRM_TOKEN_EXP_IN_MINUTES),1)));
        // log.info(methodMsgStatic("isBeforeNow(getCreationDate().plusDays(AccountConfirmationPeriods.CONFIRM_TOKEN_EXP_IN_MINUTES - 1) " + isBeforeNow(getCreationDate().plusDays(AccountConfirmationPeriods.CONFIRM_TOKEN_EXP_IN_MINUTES - 1),1)));

        return getAccountState() == AccountState.WAITING_CONFIRMATION
            && isBeforeNow(getCreationDate().plusMinutes(AccountConfirmationPeriods.CONFIRM_TOKEN_EXP_IN_MINUTES), 0);
    }

    public boolean accountConfirmationValid() {
        return getAccountState() == AccountState.WAITING_CONFIRMATION
            && isAfterNow(getCreationDate().plusMinutes(AccountConfirmationPeriods.CONFIRM_TOKEN_EXP_IN_MINUTES), 0);
    }
}
