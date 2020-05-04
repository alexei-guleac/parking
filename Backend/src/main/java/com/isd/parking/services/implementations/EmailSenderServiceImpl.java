package com.isd.parking.services.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isd.parking.models.AccountOperation;
import com.isd.parking.models.ConfirmationRecord;
import com.isd.parking.models.EmailDto;
import com.isd.parking.models.users.UserLdap;
import com.isd.parking.security.AccountConfirmationPeriods;
import com.isd.parking.services.EmailSenderService;
import com.isd.parking.web.rest.payload.DeviceInfo;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.isd.parking.web.rest.ApiEndpoints.confirmAction;
import static com.isd.parking.web.rest.ApiEndpoints.confirmReset;


@Service("emailSenderService")
@Slf4j
public class EmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    @Value("${spring.mail.from.email}")
    private String from;

    @Value("${front.url}")
    private String frontUrl;

    @Autowired
    public EmailSenderServiceImpl(@Qualifier("gmail") JavaMailSender mailSender,
                                  @Qualifier("templateEngine") TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    /**
     * Send user registration confirmation e-mail
     *
     * @param emailUser          - target user
     * @param confirmationRecord - correspond confirmation record
     * @param deviceInfo         - target user device info (for setting region purpose)
     * @throws IOException
     * @throws MessagingException
     */
    @Async
    @Override
    public void sendRegistrationConfirmMail(@NotBlank @NonNull @NotNull UserLdap emailUser,
                                            @NotBlank @NonNull @NotNull ConfirmationRecord confirmationRecord,
                                            @NotBlank @NonNull DeviceInfo deviceInfo) throws IOException, MessagingException {
        final @NotNull String subject = "Complete Registration!";
        final @NotNull String templateName = "confirm_account_mail.html";
        @NotNull EmailDto emailDto = createHtmlEmailData(emailUser, confirmationRecord, deviceInfo, subject, templateName);
        sendHtmlEmail(emailDto);
    }

    /**
     * Send user password reset confirmation e-mail
     *
     * @param emailUser          - target user
     * @param confirmationRecord - correspond confirmation record
     * @param deviceInfo         - target user device info (for setting region purpose)
     * @throws IOException
     * @throws MessagingException
     */
    @Async
    @Override
    public void sendPassResetMail(@NotBlank @NonNull @NotNull UserLdap emailUser,
                                  @NotBlank @NonNull @NotNull ConfirmationRecord confirmationRecord,
                                  @NotBlank @NonNull DeviceInfo deviceInfo) throws IOException, MessagingException {
        final @NotNull String subject = "Complete Password Reset!";
        final @NotNull String templateName = "reset_password_mail.html";
        @NotNull EmailDto emailDto = createHtmlEmailData(emailUser, confirmationRecord, deviceInfo, subject, templateName);

        sendHtmlEmail(emailDto);
    }

    /**
     * Prepare intermediate email data transfer object from all necessary data
     *
     * @param emailUser          - target user
     * @param confirmationRecord - correspond confirmation record
     * @param deviceInfo         - user device info
     * @param subject            - email subject
     * @param templateName       - email template name
     * @return email data transfer object
     */
    private @NotNull EmailDto createHtmlEmailData(@NotBlank @NonNull @NotNull UserLdap emailUser,
                                                  @NotBlank @NonNull @NotNull ConfirmationRecord confirmationRecord,
                                                  @NotBlank @NonNull DeviceInfo deviceInfo,
                                                  @NotBlank @NonNull String subject,
                                                  @NotBlank @NonNull String templateName) {
        @NotNull EmailDto emailDto = createEmailDtoFromUserLdap(emailUser, subject, templateName);
        @NotNull HashMap<String, Object> parametersMap = createTemplateParametersMap(emailUser, confirmationRecord, deviceInfo);
        emailDto.setParameterMap(parametersMap);

        return emailDto;
    }

    /**
     * Prepare template parameters map from user data
     *
     * @param emailUser          - target user
     * @param confirmationRecord - correspond confirmation record
     * @param deviceInfo         - user device info
     * @return template parameters map
     */
    private @NotNull HashMap<String, Object> createTemplateParametersMap(@NotBlank @NonNull @NotNull UserLdap emailUser,
                                                                         @NotBlank @NonNull @NotNull ConfirmationRecord confirmationRecord,
                                                                         @NotBlank @NonNull DeviceInfo deviceInfo) {
        @NotNull ObjectMapper oMapper = new ObjectMapper();
        @NotNull HashMap<String, Object> deviceInfoMap = (HashMap<String, Object>) oMapper.convertValue(deviceInfo, Map.class);
        @NotNull HashMap<String, Object> parametersMap = new HashMap<>(deviceInfoMap);

        @NotNull String username = emailUser.getUid().substring(0, 1).toUpperCase() + emailUser.getUid().substring(1);
        parametersMap.put("username", username);

        @NotNull String path = confirmationRecord.getOperationType() == AccountOperation.ACCOUNT_CONFIRMATION ? confirmAction : confirmReset;
        @NotNull String confirmLink = frontUrl + path + "?confirmation_token="
            + confirmationRecord.getConfirmationToken();
        parametersMap.put("confirmation_link", confirmLink);
        parametersMap.put("confirmation_token_valid_period", AccountConfirmationPeriods.CONFIRM_TOKEN_EXP_IN_MINUTES);
        parametersMap.put("email", emailUser.getEmail());

        return parametersMap;
    }

    /**
     * Creates email data transfer object from LDAP user
     *
     * @param emailUser    - target user
     * @param subject      - email subject
     * @param templateName - email template name
     * @return intermediate email data transfer object
     */
    private @NotNull EmailDto createEmailDtoFromUserLdap(@NotBlank @NonNull @NotNull UserLdap emailUser,
                                                         String subject,
                                                         String templateName) {
        @NotNull EmailDto emailDto = new EmailDto();

        emailDto.setTo(new String[]{emailUser.getEmail()});
        emailDto.setFrom(from);
        emailDto.setSubject(subject);
        emailDto.setHtml(true);
        emailDto.setTemplateName(templateName);

        return emailDto;
    }

    /**
     * Creates simple email message
     *
     * @param emailUser - target user
     * @return simple email message object
     */
    private @NotNull SimpleMailMessage createSimpleMailMessage(@NotNull UserLdap emailUser) {
        @NotNull SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(emailUser.getEmail());
        mailMessage.setFrom(from);

        return mailMessage;
    }

    /**
     * Send simple mail message
     *
     * @param email - target email message for send
     */
    @Async
    @Override
    public void sendEmail(@NotNull SimpleMailMessage email) {
        email.setFrom(from);
        mailSender.send(email);
    }

    /**
     * Send email using Text template
     *
     * @param emailDto - email data transfer object
     * @return EmailDto of send email
     * @throws IOException
     * @throws MessagingException
     */
    @Override
    public @NotNull EmailDto sendTextTemplateEmail(@NotNull EmailDto emailDto)
        throws IOException, MessagingException {

        // Prepare email context
        @NotNull Context ctx = prepareContext(emailDto);
        // Prepare message
        @NotNull MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        // Prepare message using a Spring helper
        @NotNull MimeMessageHelper message = prepareMessage(mimeMessage, emailDto);
        // Create email message using TEXT template
        String textContent = this.templateEngine.process(emailDto.getTemplateName(), ctx); // text/email-text\"
        emailDto.setEmailedMessage(textContent);
        message.setText(textContent);

        // Send email
        this.mailSender.send(mimeMessage);
        return emailDto;
    }

    /**
     * Send email with html template found in classpath resource
     *
     * @param emailDto - email data transfer object
     * @return EmailDto of send email
     * @throws MessagingException
     * @throws IOException
     */
    @Override
    public @NotNull EmailDto sendHtmlEmail(@NotNull EmailDto emailDto) throws MessagingException, IOException {

        // Prepare the evaluation context
        @NotNull Context ctx = prepareContext(emailDto);
        // Prepare message using a Spring helper
        @NotNull MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        @NotNull MimeMessageHelper message = prepareMessage(mimeMessage, emailDto);

        // Create the HTML body using Thymeleaf
        String htmlContent = this.templateEngine.process(emailDto.getTemplateName(), ctx);
        message.setText(htmlContent, true /* isHtml */);
        emailDto.setEmailedMessage(htmlContent);
        message = prepareStaticResources(message, emailDto);

        // Send mail
        this.mailSender.send(mimeMessage);
        this.templateEngine.clearTemplateCache();
        return emailDto;
    }


    /**
     * Send multiple emails using templates in templates directory
     *
     * @param emailDtos - email data transfer object
     * @return list of send emails data transfer objects
     * @throws MessagingException
     * @throws IOException
     */
    @Override
    public @NotNull List<EmailDto> sendEmails(@NotNull List<EmailDto> emailDtos) throws MessagingException, IOException {

        @NotNull List<MimeMessage> mimeMessages = new ArrayList<>();
        MimeMessage mimeMessage;

        for (@NotNull EmailDto emailDto : emailDtos) {
            // Prepare the evaluation context
            final @NotNull Context ctx = prepareContext(emailDto);
            // Prepare message using a Spring helper
            mimeMessage = this.mailSender.createMimeMessage();
            @NotNull MimeMessageHelper message = prepareMessage(mimeMessage, emailDto);
            // Create the HTML body using Thymeleaf
            String htmlContent = this.templateEngine.process(emailDto.getTemplateName(), ctx);
            message.setText(htmlContent, true /* isHtml */);
            emailDto.setEmailedMessage(htmlContent);
            message = prepareStaticResources(message, emailDto);

            mimeMessages.add(mimeMessage);
        }

        // Send mail
        this.mailSender.send(mimeMessages.toArray(new MimeMessage[0]));
        this.templateEngine.clearTemplateCache();

        return emailDtos;
    }

    /**
     * Prepare email message using a Spring helper
     *
     * @param mimeMessage - Spring built-in message
     * @param emailDto    - target email DTO
     * @return prepared Spring email message helper
     * @throws MessagingException
     * @throws IOException
     */
    private @NotNull MimeMessageHelper prepareMessage(@NotNull MimeMessage mimeMessage, @NotNull EmailDto emailDto)
        throws MessagingException, IOException {

        // Prepare message using a Spring helper
        @NotNull MimeMessageHelper message = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
            "UTF-8");
        message.setSubject(emailDto.getSubject());
        message.setFrom(emailDto.getFrom());
        message.setTo(emailDto.getTo());

        if (emailDto.getCc() != null && emailDto.getCc().length != 0) {
            message.setCc(emailDto.getCc());
        }
        if (emailDto.getBcc() != null && emailDto.getBcc().length != 0) {
            message.setBcc(emailDto.getBcc());
        }
        if (emailDto.isHasAttachment()) {
            @NotNull List<File> attachments = loadResources(
                emailDto.getPathToAttachment() + "/*" + emailDto.getAttachmentName() + "*.*");
            for (@NotNull File file : attachments) {
                message.addAttachment(file.getName(), file);
            }
        }

        return message;
    }

    /**
     * Load all attachment files from classpath resource directory
     *
     * @param fileNamePattern - resource file name
     * @return list of attachment files
     * @throws IOException
     */
    private @NotNull List<File> loadResources(String fileNamePattern) throws IOException {
        @NotNull PathMatchingResourcePatternResolver fileResolver = new PathMatchingResourcePatternResolver();

        Resource @Nullable [] resources = null;

        try {
            resources = fileResolver.getResources("file:" + fileNamePattern);
        } catch (Exception e) {
            e.printStackTrace();
        }
        @NotNull List<File> attachFiles = new ArrayList<>();

        assert resources != null;
        for (@NotNull Resource resource : resources) {
            attachFiles.add(resource.getFile());
        }

        return attachFiles;
    }

    /**
     * Prepares Spring evaluation context (load parameters in context for using in email template)
     *
     * @param emailDto - target email DTO
     * @return prepared Spring context
     */
    private @NotNull Context prepareContext(@NotNull EmailDto emailDto) {
        // Prepare the evaluation context
        @NotNull Context ctx = new Context();

        final Map<String, Object> parameterMap = emailDto.getParameterMap();
        if (parameterMap != null) {
            @NotNull Set<String> keySet = parameterMap.keySet();
            if (!keySet.isEmpty()) {
                keySet.forEach(s -> ctx.setVariable(s, parameterMap.get(s)));
            }
        }
        @NotNull Set<String> resKeySet = emailDto.getStaticResourceMap().keySet();
        if (!resKeySet.isEmpty()) {
            resKeySet.forEach(s -> ctx.setVariable(s, emailDto.getStaticResourceMap().get(s)));
        }

        return ctx;
    }

    /**
     * Prepare all static files (images) for email from classpath resource directory
     *
     * @param message  - Spring email message helper
     * @param emailDto - target email DTO
     * @return Spring email message helper
     * @throws MessagingException
     */
    private @NotNull MimeMessageHelper prepareStaticResources(@NotNull MimeMessageHelper message,
                                                              @NotNull EmailDto emailDto) throws MessagingException {
        Map<String, Object> staticResources = emailDto.getStaticResourceMap();

        for (Map.@NotNull Entry<String, Object> entry : staticResources.entrySet()) {
            @NotNull ClassPathResource imageSource =
                new ClassPathResource("static/" + entry.getValue());
            message.addInline(entry.getKey(), imageSource, "image/png");
            message.addInline((String) entry.getValue(), imageSource, "image/png");
        }

        return message;
    }
}
