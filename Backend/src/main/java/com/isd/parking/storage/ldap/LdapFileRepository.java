package com.isd.parking.storage.ldap;

import com.isd.parking.models.users.UserLdap;
import com.unboundid.ldap.sdk.Attribute;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldif.LDIFException;
import com.unboundid.ldif.LDIFReader;
import com.unboundid.ldif.LDIFWriter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static com.isd.parking.storage.ldap.LdapConstants.*;
import static com.isd.parking.utils.AppStringUtils.convertArrayToCommaSeparatedString;
import static com.isd.parking.utils.ColorConsoleOutput.methodMsg;
import static org.apache.commons.lang.StringUtils.stripEnd;


/**
 * LDAP .ldif file repository operations
 */
@Slf4j
@Component
public class LdapFileRepository {

    /**
     * Write LDAP entry to .ldif file
     *
     * @param entry - target UnboundId SDK LDAP entry
     * @throws LdapMappingException
     */
    public static void writeEntryToLdifFile(Entry entry) throws LdapMappingException {
        // Write all of the matching entries to LDIF file
        LDIFWriter ldifWriter;
        try {
            ldifWriter = getLdifWriter(true);
            ldifWriter.writeEntry(entry);
            ldifWriter.close();
        } catch (IOException e) {
            throw new LdapMappingException("Error writing to .ldif file, try again", e);
        }
    }

    /**
     * Write all specified LDAP entries to .ldif file
     *
     * @param entries - target UnboundId SDK LDAP entries
     * @throws LdapMappingException
     */
    private static void writeEntriesToLdifFile(List<Entry> entries) throws LdapMappingException {
        // Write all of the matching entries to LDIF file
        LDIFWriter ldifWriter;
        try {
            ldifWriter = getLdifWriter(false);
            for (Entry entry : entries) {
                ldifWriter.writeEntry(entry);
            }
            ldifWriter.close();
        } catch (IOException e) {
            throw new LdapMappingException("Error writing to ldif file, try again", e);
        }
    }

    private static LDIFWriter getLdifWriter(boolean appendData) throws FileNotFoundException {
        return new LDIFWriter(new FileOutputStream(new File(LDIF_FILE_PATH), appendData));
    }

    /**
     * Update entry in .ldif file
     *
     * @param uid            - target user id
     * @param attributeName  - target attribute name
     * @param attributeValue - target attribute value
     */
    public static void updateEntryInLdifFile(@NotNull String uid,
                                             @Nullable String attributeName,
                                             @Nullable String attributeValue) {
        // read all entries from file
        List<Entry> entries = readEntriesFromLdifFile();
        // get specified entry and it's index
        EntryContainer foundEntry = foundEntryByUid(uid, entries);

        int found = foundEntry.getIndex();
        if (found != -1) {
            Entry entry = foundEntry.getEntry();

            if (entry != null) {
                boolean retrievePassword = true;
                if (attributeName != null && attributeValue != null) {
                    entry.setAttribute(attributeName, attributeValue);

                    // if user id (in this case unique username that is included in user DN) is modified
                    // we need to update user LDAP domain name
                    if (attributeName.equals(USER_UID_ATTRIBUTE)) {
                        String[] entryDN = entry.getDN().split(",");
                        entryDN[0] = USER_UID_ATTRIBUTE + "=" + attributeValue;
                        entry.setDN(convertArrayToCommaSeparatedString(entryDN));
                    }
                    if (attributeName.equals(USER_PASSWORD_ATTRIBUTE)) {
                        retrievePassword = false;
                    }
                }
                // if password isn't updated we need to retrieve original password from LDAP entry
                // due to security restrictions
                if (retrievePassword) {
                    // rebind original password
                    String pass = getUserPassFromAttribute(entry);
                    if (pass != null) {
                        entry.setAttribute(USER_PASSWORD_ATTRIBUTE, pass);
                    }
                } else {
                    setPasswordUpdatedNow(entry);
                }
                setUpdatedNow(entry);
                entries.set(found, entry);
            }
        }
        try {
            writeEntriesToLdifFile(entries);
        } catch (LdapMappingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get all entries from .ldif file
     *
     * @return list of all entries
     */
    private static List<Entry> readEntriesFromLdifFile() {
        List<Entry> entries = null;
        try {
            entries = LDIFReader.readEntries(LDIF_FILE_PATH);
        } catch (IOException | LDIFException e) {
            e.printStackTrace();
        }
        return entries;
    }

    /**
     * Delete user entry from .ldif file by it's id
     *
     * @param uid - target user id
     */
    static void deleteEntryFromLdifFile(String uid) {
        List<Entry> entries = readEntriesFromLdifFile();

        EntryContainer foundEntry = foundEntryByUid(uid, entries);
        if (foundEntry.getIndex() != -1) {
            entries.remove(foundEntry.getIndex());
        }
        try {
            writeEntriesToLdifFile(entries);
        } catch (LdapMappingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete user entry from .ldif file
     *
     * @param user - target user
     */
    static void deleteEntryFromLdifFile(UserLdap user) {
        String email = user.getEmail();
        List<Entry> entries = readEntriesFromLdifFile();

        EntryContainer foundEntry = foundEntryByEmail(email, entries);
        if (foundEntry.getIndex() != -1) {
            entries.remove(foundEntry.getIndex());
        }
        try {
            writeEntriesToLdifFile(entries);
        } catch (LdapMappingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets user LDAP record date attribute updated now
     *
     * @param entry - target entry
     */
    private static void setUpdatedNow(Entry entry) {
        entry.setAttribute("updatedAt", String.valueOf(LocalDateTime.now()));
    }

    /**
     * Sets user LDAP record password attribute updated now
     *
     * @param entry - target entry
     */
    private static void setPasswordUpdatedNow(Entry entry) {
        entry.setAttribute("passwordUpdatedAt", String.valueOf(LocalDateTime.now()));
    }

    /**
     * Retrieves user password from LDAP file entry
     * Used for rebind original password when entry is updated
     * (performed due to Spring user LDAP mapping password security restrictions
     * (user password is hidden in user object gettin from LDAP))
     *
     * @param entry - LDAP file entry
     * @return user password
     */
    private static String getUserPassFromAttribute(Entry entry) {
        String pass = null;

        Attribute passAttr = entry.getAttribute(USER_PASSWORD_ATTRIBUTE);
        if (passAttr != null) {
            pass = passAttr.toString();
            log.info(methodMsg("PASS " + pass));
            pass = pass.split("\\s+")[1].split("=")[1].strip();
            pass = stripEnd(pass, "'})");
            pass = pass.substring(2);
        }

        return pass;
    }

    /**
     * Founds user lDAP entry in .ldif file entries by user id
     *
     * @param userAttributeValue - target user attribute value
     * @param entries            - all LDIF file entries set
     * @return container with found entry and it's index in file entries
     */
    private static EntryContainer foundEntryByUid(String userAttributeValue, List<Entry> entries) {
        return foundEntry("uid", userAttributeValue, entries);
    }

    /**
     * Founds user lDAP entry in .ldif file entries by user email
     *
     * @param userAttributeValue - target user attribute value
     * @param entries            - all LDIF file entries set
     * @return container with found entry and it's index in file entries
     */
    private static EntryContainer foundEntryByEmail(String userAttributeValue, List<Entry> entries) {
        return foundEntry("email", userAttributeValue, entries);
    }

    /**
     * Found user LDAP entry by specified attribute in file entries
     *
     * @param attrName           - target user attribute name
     * @param userAttributeValue - target user attribute value
     * @param entries            - all LDIF file entries set
     * @return container with found entry and it's index in file entries
     */
    private static EntryContainer foundEntry(String attrName, String userAttributeValue, List<Entry> entries) {
        int found = -1;

        for (int i = 0; i < entries.size(); i++) {
            String attrValue = entries.get(i).getAttributeValue(attrName);
            if (attrValue != null) {
                if (attrValue.equals(userAttributeValue)) {
                    found = i;
                    break;
                }
            }
        }

        return new EntryContainer(found, found != -1 ? entries.get(found) : null);
    }

    /**
     * Auxiliary wrapping class for user LDAP entry and it index in file entries
     */
    @Data
    private static class EntryContainer {

        private final int index;

        private final Entry entry;

        EntryContainer(int found, Entry entry) {
            this.index = found;
            this.entry = entry;
        }
    }
}
