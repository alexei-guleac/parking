package com.isd.parking.services;

import com.isd.parking.models.ConfirmationRecord;
import com.isd.parking.models.EmailDto;
import com.isd.parking.models.users.UserLdap;
import com.isd.parking.web.rest.payload.DeviceInfo;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.springframework.mail.SimpleMailMessage;

import javax.mail.MessagingException;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.List;


public interface EmailSenderService {

    void sendRegistrationConfirmMail(@NotBlank @NonNull UserLdap emailUser,
                                     @NotBlank @NonNull ConfirmationRecord confirmationRecord,
                                     @NotBlank @NonNull DeviceInfo deviceInfo) throws IOException, MessagingException;

    void sendPassResetMail(@NotBlank @NonNull UserLdap emailUser,
                           @NotBlank @NonNull ConfirmationRecord confirmationRecord,
                           @NotBlank @NonNull DeviceInfo deviceInfo) throws IOException, MessagingException;

    void sendEmail(SimpleMailMessage email);

    @NotNull EmailDto sendTextTemplateEmail(EmailDto emailDto) throws IOException, MessagingException;

    @NotNull EmailDto sendHtmlEmail(EmailDto emailDto) throws MessagingException, IOException;

    @NotNull List<EmailDto> sendEmails(List<EmailDto> emailDtos) throws MessagingException, IOException;
}
