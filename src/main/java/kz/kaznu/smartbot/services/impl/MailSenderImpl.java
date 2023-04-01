package kz.kaznu.smartbot.services.impl;

import kz.kaznu.smartbot.services.MailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailSenderImpl implements MailSender {

    @Value("${emailLogin}")
    private String senderEmail;

    @Value("${emailPassword}")
    private String senderPassword;

    private Session createSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });
    }

    @Override
    public boolean send(String subject, String text, String recipient) {
        log.info("Sending email to " + recipient);

        Session session = createSession();
        MimeMessage message = new MimeMessage(session);
        try {
            message.setContent(text, "text/html; charset=utf-8");
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            Transport.send(message);
            log.info("Message sent successfully!");
            return true;
        } catch (MessagingException e) {
            log.error("Message send error: {}", e.getMessage());
            return false;
        }
    }
}
