package com.isd.parking.service;

import com.isd.parking.models.BaseEmailUser;
import com.isd.parking.models.Mail;
import com.isd.parking.security.AccountConfirmationPeriods;
import com.isd.parking.security.model.ConfirmationToken;
import com.isd.parking.utils.ColorConsoleOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

import static com.isd.parking.utils.ColorConsoleOutput.blTxt;

@Service("emailSenderService")
@Slf4j
public class EmailSenderService {

    private final JavaMailSender gmailMailSender;
    private final ColorConsoleOutput console;
    @Value("${spring.mail.from.email}")
    private String from;

    @Autowired
    public EmailSenderService(@Qualifier("gmail") JavaMailSender gmailMailSender, ColorConsoleOutput console) {
        this.gmailMailSender = gmailMailSender;
        this.console = console;
    }

    @Async
    public void sendPassResetMail(BaseEmailUser existingUser, ConfirmationToken confirmationToken) {
        // Create the message
        SimpleMailMessage mailMessage = createSimpleMailMessage(existingUser);
        mailMessage.setSubject("Complete Password Reset!");
        mailMessage.setFrom("parking_noreply_service@gmail.com");
        mailMessage.setText("To complete the password reset process, please click here: "
            + "http://localhost:4200/confirm_reset?confirmation_token="
            + confirmationToken.getConfirmationToken()
            + "\n\nThis link is valid for " + AccountConfirmationPeriods.CONFIRM_TOKEN_EXP_IN_MINUTES + " minute(s)");

        log.info(console.classMsg(getClass().getSimpleName(), " Forgot password email sended: ") + blTxt(String.valueOf(existingUser)));

        // Send the email
        sendEmail(mailMessage);
    }

    @Async
    public void sendRegistrationConfirmMail(BaseEmailUser user, ConfirmationToken confirmationToken) {
        // Create the message
        SimpleMailMessage mailMessage = createSimpleMailMessage(user);
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom(from);
        mailMessage.setText("To confirm your account, please click here : "
            + "http://localhost:4200/confirm-account?confirmation_token="
            + confirmationToken.getConfirmationToken()
            + "\n\nThis link is valid for " + AccountConfirmationPeriods.CONFIRM_TOKEN_EXP_IN_MINUTES + " minute(s)");

        // Send the email
        sendEmail(mailMessage);
    }

    private SimpleMailMessage createSimpleMailMessage(BaseEmailUser user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        return mailMessage;
    }


    @Async
    public void sendEmail(SimpleMailMessage email) {
        email.setFrom(from);
        gmailMailSender.send(email);
    }

    public void sendEmail(Mail mail) {
        MimeMessage mimeMessage = gmailMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setSubject(mail.getMailSubject());
            mimeMessageHelper.setFrom(new InternetAddress(mail.getMailFrom(), "parking-app.com"));
            mimeMessageHelper.setTo(mail.getMailTo());
            mimeMessageHelper.setText(mail.getMailContent());

            gmailMailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
