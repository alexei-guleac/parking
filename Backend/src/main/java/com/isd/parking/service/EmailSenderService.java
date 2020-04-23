package com.isd.parking.service;

import com.isd.parking.models.EmailDto;
import com.isd.parking.models.users.UserLdap;
import com.isd.parking.security.model.ConfirmationRecord;
import com.isd.parking.security.model.payload.register.DeviceInfo;
import lombok.NonNull;
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

    EmailDto sendTextTemplateEmail(EmailDto emailDto) throws IOException, MessagingException;

    EmailDto sendHtmlEmail(EmailDto emailDto) throws MessagingException, IOException;

    List<EmailDto> sendEmails(List<EmailDto> emailDtos) throws MessagingException, IOException;
}
