package com.isd.parking.repository;

import com.isd.parking.models.users.UserLdap;
import com.isd.parking.service.ldap.LdapMappingException;
import com.unboundid.ldap.sdk.Entry;
import org.springframework.stereotype.Repository;

import javax.annotation.Nullable;

import static com.isd.parking.service.ldap.LdapAttributeMappers.mapUserToEntry;
import static com.isd.parking.service.ldap.LdapFileUtils.*;


@Repository
public class UserLdapFileRepository {

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

    public boolean update(String uid, @Nullable String attributeName, @Nullable String attributeValue) {
        updateEntryInLdifFile(uid, attributeName, attributeValue);
        return true;
    }

    public boolean updatePassword(String uid, String password) {
        updateEntryPassword(uid, password);
        return true;
    }
}
