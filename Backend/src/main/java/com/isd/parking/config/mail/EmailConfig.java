package com.isd.parking.config.mail;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;


/**
 * Configures connection to the mail host
 */
@Configuration
@PropertySource("classpath:email-config-${spring.email-properties.prefix}.properties")
public class EmailConfig {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String userName;

    @Value("${spring.mail.password}")
    private String passWord;

    @Value("${spring.mail.transport.protocol}")
    private String protocol;

    @Value("${spring.mail.from.email}")
    private String from;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String auth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String startTls;

    @Value("${mail.smtp.debug}")
    private String debug;

    @Bean("gmail")
    public @NotNull JavaMailSender gmailMailSender() {
        @NotNull JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(host);
        mailSender.setPort(port);

        mailSender.setUsername(userName);
        mailSender.setPassword(passWord);

        // Standard email config
        @NotNull Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", startTls);
        props.put("mail.from.email", from);
        props.put("mail.debug", debug);

        // Digital Ocean email config (not working properly, not sending email)
        /* @NotNull Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtps.auth", auth);
        // props.put("mail.smtps.starttls.enable", startTls);
        props.put("mail.smtp.ssl", "true");
        // props.put("mail.smtps.ssl.checkserveridentity", "true");
        props.put("mail.smtps.ssl.trust", "*");
        props.put("mail.from.email", from);
        props.put("mail.smtps.socketFactory.port", port);
        props.put("mail.smtps.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.debug", debug); */

        // creates a new session with an authenticator
        @NotNull Authenticator auth = new Authenticator() {
            public @NotNull PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, passWord);
            }
        };

        Session session = Session.getInstance(props, auth);
        mailSender.setSession(session);

        return mailSender;
    }
}
