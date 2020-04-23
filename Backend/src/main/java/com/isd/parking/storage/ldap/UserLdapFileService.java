package com.isd.parking.storage.ldap;

import com.isd.parking.models.users.UserLdap;
import com.unboundid.ldap.sdk.Entry;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import static com.isd.parking.storage.ldap.LdapAttributeMappers.mapUserToEntry;
import static com.isd.parking.storage.ldap.LdapConstants.USER_PASSWORD_ATTRIBUTE;
import static com.isd.parking.storage.ldap.LdapFileRepository.*;


/**
 * Service for LDIF file interaction
 */
@Service
public class UserLdapFileService {

    public boolean save(UserLdap user) {
        Entry entry = mapUserToEntry(user);
        try {
            writeEntryToLdifFile(entry);
        } catch (LdapMappingException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean update(UserLdap user) {
        updateEntryInLdifFile(user.getUid(), null, null);
        return true;
    }

    public boolean update(@NotNull String uid,
                          @Nullable String attributeName,
                          @Nullable String attributeValue) {
        updateEntryInLdifFile(uid, attributeName, attributeValue);
        return true;
    }

    public boolean updateUserPassword(String uid, String password) {
        updateEntryInLdifFile(uid, USER_PASSWORD_ATTRIBUTE, password);
        return true;
    }

    public boolean deleteUser(UserLdap user) {
        deleteEntryFromLdifFile(user);
        return true;
    }

    public boolean deleteUserById(String uid) {
        deleteEntryFromLdifFile(uid);
        return true;
    }
}
