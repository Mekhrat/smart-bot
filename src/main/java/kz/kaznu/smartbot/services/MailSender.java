package kz.kaznu.smartbot.services;

public interface MailSender {
    boolean send(String subject, String text, String recipient);
}
