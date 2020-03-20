package com.isd.parking.service.ldap;

import com.isd.parking.models.UserLdap;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldif.LDIFException;
import com.unboundid.ldif.LDIFReader;
import com.unboundid.ldif.LDIFWriter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static com.isd.parking.service.ldap.LdapConstants.LDIF_FILE_PATH;
import static com.isd.parking.service.ldap.LdapConstants.USER_PASSWORD_ATTRIBUTE;
import static com.isd.parking.utils.ColorConsoleOutput.methodMsgStatic;
import static org.apache.commons.lang.StringUtils.stripEnd;


@Slf4j
@Component
public class LdapFileUtils {

    public static void writeEntryToLdifFile(Entry entry) throws LdapMappingException {
        // log.info(methodMsgStatic("Entry " + entry)));

        // Write all of the matching entries to LDIF.
        LDIFWriter ldifWriter;
        try {
            log.info(" LDIF_FILE_NAME " + LDIF_FILE_PATH);
            ldifWriter = new LDIFWriter(new FileOutputStream(new File(LDIF_FILE_PATH), true));
            ldifWriter.writeEntry(entry);
            ldifWriter.close();
        } catch (IOException e) {
            throw new LdapMappingException("Error writing to ldif  file, try again", e);
        }
    }

    private static void writeEntriesToLdifFile(List<Entry> entries) throws LdapMappingException {
        // Write all of the matching entries to LDIF.
        LDIFWriter ldifWriter;
        try {
            log.info(" LDIF_FILE_NAME " + LDIF_FILE_PATH);
            ldifWriter = new LDIFWriter(new FileOutputStream(new File(LDIF_FILE_PATH), false));
            for (Entry entry : entries) {
                ldifWriter.writeEntry(entry);
            }
            ldifWriter.close();
        } catch (IOException e) {
            throw new LdapMappingException("Error writing to ldif file, try again", e);
        }
    }

    public static void updateEntryInLdifFile(UserLdap user, @Nullable String attributeName, @Nullable String attributeValue) {
        log.info(methodMsgStatic("USER " + user));
        String uid = user.getUid();
        log.info(methodMsgStatic("uid searching in " + uid));
        List<Entry> entries = getEntriesFromLdifFile();
        log.info(methodMsgStatic("entries " + entries));

        EntryContainer foundEntry = foundEntryByUid(uid, entries);
        int found = foundEntry.getIndex();
        log.info(methodMsgStatic("AFTER foundEntry found " + found));
        log.info(methodMsgStatic("AFTER foundEntry " + user));
        if (found != -1) {
            log.info(methodMsgStatic("ENTRY before map " + user));
            // Entry entry = mapUserToEntry(user);
            Entry entry = foundEntry.getEntry();

            if (entry != null) {
                if (attributeName != null && attributeValue != null) {
                    entry.setAttribute(attributeName, attributeValue);
                }
                if (!attributeName.equals(USER_PASSWORD_ATTRIBUTE)) {
                    // rebind original password
                    String pass = getUserPassFromAttribute(entries, found);
                    entry.setAttribute(USER_PASSWORD_ATTRIBUTE, pass);
                } else {
                    setPasswordUpdatedNow(entry);
                }
                setUpdatedNow(entry);
                log.info(methodMsgStatic("ENTRY after map " + entry));
                entries.set(found, entry);
            }
        }
        try {
            writeEntriesToLdifFile(entries);
        } catch (LdapMappingException e) {
            e.printStackTrace();
        }
    }

    private static void setUpdatedNow(Entry entry) {
        entry.setAttribute("updatedAt", String.valueOf(LocalDateTime.now()));
    }

    private static void setPasswordUpdatedNow(Entry entry) {
        entry.setAttribute("passwordUpdatedAt", String.valueOf(LocalDateTime.now()));
    }

    public static void updateEntryPassword(UserLdap user, String password) {
        updateEntryInLdifFile(user, USER_PASSWORD_ATTRIBUTE, password);
    }

    /*public static void updateEntryUsername(UserLdap user, String username) {
        updateEntryInLdifFile(user, "username", username);
    }*/

    private static String getUserPassFromAttribute(List<Entry> entries, int found) {
        String pass = entries.get(found).getAttribute(USER_PASSWORD_ATTRIBUTE).toString();
        log.info(methodMsgStatic("PASS " + pass));
        pass = pass.split("\\s+")[1].split("=")[1].strip();
        pass = stripEnd(pass, "'})");
        pass = pass.substring(2);
        return pass;
    }

    private static EntryContainer foundEntry(String attrName, String userAttributeValue, List<Entry> entries) {
        int found = -1;
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getDN().contains(attrName)) {
                String attrValue = entries.get(i).getAttributeValue(attrName);
                if (attrValue != null) {
                    log.info(methodMsgStatic("ENTRY attttrr " + attrValue));
                    if (attrValue.equals(userAttributeValue)) {
                        found = i;
                        break;
                    }
                }
            }
        }
        return new EntryContainer(found, entries.get(found));
    }

    private static EntryContainer foundEntryByUid(String userAttributeValue, List<Entry> entries) {
        return foundEntry("uid", userAttributeValue, entries);
    }

    private static EntryContainer foundEntryByEmail(String userAttributeValue, List<Entry> entries) {
        return foundEntry("email", userAttributeValue, entries);
    }

    private static List<Entry> getEntriesFromLdifFile() {
        List<Entry> entries = null;
        try {
            entries = LDIFReader.readEntries(LDIF_FILE_PATH);
        } catch (IOException | LDIFException e) {
            e.printStackTrace();
        }
        return entries;
    }

    static void deleteEntryFromLdifFile(UserLdap user) {
        String email = user.getEmail();
        log.info(methodMsgStatic("email searching in " + email));

        List<Entry> entries = getEntriesFromLdifFile();
        // log.info(methodMsgStatic("entries " + entries);
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
