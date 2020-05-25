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

import javax.naming.directory.DirContext;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.isd.parking.storage.ldap.LdapConstants.*;
import static com.isd.parking.utilities.AppStringUtils.convertArrayToCommaSeparatedString;
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
    public static void writeEntryToLdifFile(@org.jetbrains.annotations.NotNull Entry entry)
        throws LdapMappingException {
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
    private static void writeEntriesToLdifFile(@org.jetbrains.annotations.NotNull List<Entry> entries)
        throws LdapMappingException {
        // Write all of the matching entries to LDIF file
        LDIFWriter ldifWriter;
        try {
            ldifWriter = getLdifWriter(false);
            for (@org.jetbrains.annotations.NotNull Entry entry : entries) {
                ldifWriter.writeEntry(entry);
            }
            ldifWriter.close();
        } catch (IOException e) {
            throw new LdapMappingException("Error writing to ldif file, try again", e);
        }
    }

    private static @org.jetbrains.annotations.NotNull LDIFWriter getLdifWriter(boolean appendData)
        throws FileNotFoundException {
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
                                             @Nullable String attributeValue,
                                             int modificationType) {
        // read all entries from file
        @org.jetbrains.annotations.Nullable List<Entry> entries = readEntriesFromLdifFile();
        // get specified entry and it's index
        assert entries != null;
        @org.jetbrains.annotations.NotNull EntryContainer foundEntry = foundEntryByUid(uid, entries);

        int found = foundEntry.getIndex();
        if (found != -1) {
            Entry entry = foundEntry.getEntry();

            if (entry != null) {
                boolean retrievePassword = true;
                if (attributeName != null) {
                    if (attributeValue != null) {
                        if (entry.hasAttribute(attributeName) && modificationType == DirContext.REPLACE_ATTRIBUTE) {
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
                            entry.removeAttribute(USER_SOCIALS_ATTRIBUTE);
                        }

                        // else add attribute
                        if (!entry.hasAttribute(attributeName)) {
                            entry.addAttribute(attributeName, attributeValue);
                        }
                    }

                    // else delete attribute
                    if (entry.hasAttribute(attributeName) && modificationType == DirContext.REMOVE_ATTRIBUTE) {
                        entry.removeAttribute(attributeName);
                    }
                }

                // if password isn't updated we need to retrieve original password from LDAP entry
                // due to Spring Boot security restrictions
                if (retrievePassword) {
                    // rebind original password
                    @org.jetbrains.annotations.Nullable String pass = getUserPassFromAttribute(entry);
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
    private static @org.jetbrains.annotations.Nullable List<Entry> readEntriesFromLdifFile() {
        @org.jetbrains.annotations.Nullable List<Entry> entries = null;
        try {
            entries = LDIFReader.readEntries(LDIF_FILE_PATH);
        } catch (@org.jetbrains.annotations.NotNull IOException | LDIFException e) {
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
        @org.jetbrains.annotations.Nullable List<Entry> entries = readEntriesFromLdifFile();

        assert entries != null;
        @org.jetbrains.annotations.NotNull EntryContainer foundEntry = foundEntryByUid(uid, entries);
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
    static void deleteEntryFromLdifFile(@org.jetbrains.annotations.NotNull UserLdap user) {
        String email = user.getEmail();
        @org.jetbrains.annotations.Nullable List<Entry> entries = readEntriesFromLdifFile();

        assert entries != null;
        @org.jetbrains.annotations.NotNull EntryContainer foundEntry = foundEntryByEmail(email, entries);
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
    private static void setUpdatedNow(@org.jetbrains.annotations.NotNull Entry entry) {
        entry.setAttribute("updatedAt", String.valueOf(LocalDateTime.now()));
    }

    /**
     * Sets user LDAP record password attribute updated now
     *
     * @param entry - target entry
     */
    private static void setPasswordUpdatedNow(@org.jetbrains.annotations.NotNull Entry entry) {
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
    private static @org.jetbrains.annotations.Nullable String getUserPassFromAttribute(@org.jetbrains.annotations.NotNull Entry entry) {
        @org.jetbrains.annotations.Nullable String pass = null;

        Attribute passAttr = entry.getAttribute(USER_PASSWORD_ATTRIBUTE);
        if (passAttr != null) {
            pass = passAttr.toString();
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
    private static @org.jetbrains.annotations.NotNull EntryContainer foundEntryByUid(String userAttributeValue, @org.jetbrains.annotations.NotNull List<Entry> entries) {
        return Objects.requireNonNull(foundEntry(USER_UID_ATTRIBUTE, userAttributeValue, entries));
    }

    /**
     * Founds user lDAP entry in .ldif file entries by user email
     *
     * @param userAttributeValue - target user attribute value
     * @param entries            - all LDIF file entries set
     * @return container with found entry and it's index in file entries
     */
    private static @org.jetbrains.annotations.NotNull EntryContainer foundEntryByEmail(String userAttributeValue, @org.jetbrains.annotations.NotNull List<Entry> entries) {
        return Objects.requireNonNull(foundEntry("email", userAttributeValue, entries));
    }

    /**
     * Found user LDAP entry by specified attribute in file entries
     *
     * @param attrName           - target user attribute name
     * @param userAttributeValue - target user attribute value
     * @param entries            - all LDIF file entries set
     * @return container with found entry and it's index in file entries
     */
    private static @org.jetbrains.annotations.NotNull EntryContainer foundEntry(String attrName, String userAttributeValue, @org.jetbrains.annotations.NotNull List<Entry> entries) {
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
