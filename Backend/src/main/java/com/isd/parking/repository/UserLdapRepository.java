package com.isd.parking.repository;

import com.isd.parking.models.UserLdap;
import com.isd.parking.service.ldap.LdapMappingException;
import com.unboundid.ldap.sdk.Entry;
import org.springframework.stereotype.Repository;

import static com.isd.parking.service.ldap.LdapAttributeMappers.mapUserToEntry;
import static com.isd.parking.service.ldap.LdapFileUtils.*;


@Repository
public class UserLdapRepository {

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
        updateEntryInLdifFile(user, null, null);
        return true;
    }

    public boolean updatePassword(UserLdap user, String password) {
        updateEntryPassword(user, password);
        return true;
    }
}
