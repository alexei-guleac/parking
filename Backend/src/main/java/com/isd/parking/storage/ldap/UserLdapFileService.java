package com.isd.parking.storage.ldap;

import com.isd.parking.models.users.UserLdap;
import com.unboundid.ldap.sdk.Entry;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.naming.directory.DirContext;
import javax.validation.constraints.NotNull;

import static com.isd.parking.storage.ldap.LdapAttributeMappers.mapUserToEntry;
import static com.isd.parking.storage.ldap.LdapConstants.USER_PASSWORD_ATTRIBUTE;
import static com.isd.parking.storage.ldap.LdapFileRepository.*;


/**
 * Service for LDIF file interaction
 */
@Service
public class UserLdapFileService {

    public boolean save(@org.jetbrains.annotations.NotNull UserLdap user) {
        Entry entry = mapUserToEntry(user);
        try {
            assert entry != null;
            writeEntryToLdifFile(entry);
        } catch (LdapMappingException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean update(@org.jetbrains.annotations.NotNull UserLdap user) {
        updateEntryInLdifFile(user.getUid(), null, null, -1);
        return true;
    }

    public boolean update(@NotNull String uid,
                          @Nullable String attributeName,
                          @Nullable String attributeValue) {
        updateEntryInLdifFile(uid, attributeName, attributeValue, DirContext.REPLACE_ATTRIBUTE);
        return true;
    }

    public boolean updateUserPassword(String uid, String password) {
        updateEntryInLdifFile(uid, USER_PASSWORD_ATTRIBUTE, password, DirContext.REPLACE_ATTRIBUTE);
        return true;
    }

    public boolean deleteUser(@org.jetbrains.annotations.NotNull UserLdap user) {
        deleteEntryFromLdifFile(user);
        return true;
    }

    public boolean deleteUserById(String uid) {
        deleteEntryFromLdifFile(uid);
        return true;
    }

    /**
     * Connect given social provider id to user account
     *
     * @param id             - target user social id
     * @param socialProvider - social service provider
     * @return operation result
     */
    public boolean connectSocialProvider(String uid, String socialProvider, String id) {
        updateEntryInLdifFile(uid, socialProvider, id, DirContext.ADD_ATTRIBUTE);
        return true;
    }

    /**
     * Disonnect given social provider from user account
     *
     * @param socialProvider - social service provider
     * @return operation result
     */
    public boolean dicconnectSocialProvider(String uid, String socialProvider) {
        updateEntryInLdifFile(uid, socialProvider, null, DirContext.REMOVE_ATTRIBUTE);
        return true;
    }
}
