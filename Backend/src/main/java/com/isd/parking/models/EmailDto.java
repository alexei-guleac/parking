package com.isd.parking.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * Intermediate object needed to form a email message fro user information
 */
@Data
@NoArgsConstructor
public class EmailDto {

    @NotNull(message = "From email address cannot be null")
    @Email(message = "From email address is not valid")
    private String from;

    @NotEmpty(message = "To email address cannot be empty")
    @Email(message = "To email address is not valid")
    private String[] to;


    @Email(message = "Cc email address is not valid")
    private String[] cc;

    @Email(message = "Bcc email address is not valid")
    private String[] bcc;

    @NotNull(message = "Email subject cannot be null")
    private String subject;

    @NotNull(message = "Email message cannot be null")
    private String message;

    private boolean isHtml;

    private boolean isTemplate;

    private boolean hasAttachment;

    private String pathToAttachment;

    private String attachmentName;

    private String templateName;

    private String templateLocation;

    private @org.jetbrains.annotations.NotNull Map<String, Object> parameterMap = new HashMap<>();

    private @org.jetbrains.annotations.NotNull Map<String, Object> staticResourceMap = new HashMap<>();

    private String emailedMessage;

    public EmailDto(String from, @org.jetbrains.annotations.NotNull String toList, String subject, String message) {
        this();
        this.from = from;
        this.subject = subject;
        this.message = message;
        this.to = splitByComma(toList);
    }


    public EmailDto(String from, @org.jetbrains.annotations.NotNull String toList, @org.jetbrains.annotations.NotNull String ccList) {
        this();
        this.from = from;
        this.to = splitByComma(toList);
        this.cc = splitByComma(ccList);
    }

    public EmailDto(String from, @org.jetbrains.annotations.NotNull String toList) {
        this();
        this.from = from;
        this.to = splitByComma(toList);
    }

    public EmailDto(String from, @org.jetbrains.annotations.NotNull String toList, @org.jetbrains.annotations.NotNull String ccList, String subject, String message) {
        this();
        this.from = from;
        this.subject = subject;
        this.message = message;
        this.to = splitByComma(toList);
        this.cc = splitByComma(ccList);
    }

    private String[] splitByComma(@org.jetbrains.annotations.NotNull String toMultiple) {
        return toMultiple.split(",");
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public @org.jetbrains.annotations.NotNull String toString() {
        return "EmailDto [" + (from != null ? "from=" + from + ", " : "")
            + (to != null ? "to=" + Arrays.toString(to) + ", " : "")
            + (cc != null ? "cc=" + Arrays.toString(cc) + ", " : "")
            + (bcc != null ? "bcc=" + Arrays.toString(bcc) + ", " : "")
            + (subject != null ? "subject=" + subject + ", " : "")
            + (message != null ? "message=" + message + ", " : "") + "isHtml=" + isHtml + ", isTemplate="
            + isTemplate + ", hasAttachment=" + hasAttachment + ", "
            + (pathToAttachment != null ? "pathToAttachment=" + pathToAttachment + ", " : "")
            + (attachmentName != null ? "attachmentName=" + attachmentName + ", " : "")
            + (templateName != null ? "templateName=" + templateName + ", " : "")
            + (templateLocation != null ? "templateLocation=" + templateLocation + ", " : "")
            + (parameterMap != null ? "parameterMap=" + parameterMap + ", " : "")
            + (emailedMessage != null ? "emailedMessage=" + emailedMessage : "") + "]";
    }
}
