package it.gov.pagopa.email.notification.connector.smtp;

import it.gov.pagopa.email.notification.dto.smtp.MailRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "spring.mail", value = "test-connection")
public class SMTPConnectorImpl implements SMTPConnector {
    private final JavaMailSenderImpl mailSender;

    public SMTPConnectorImpl(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    @SneakyThrows
    @Override
    public void sendMessage(MailRequest mailRequest) {
        log.info("[SEND MESSAGE SMTP] Start processing message");
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false);
        helper.setFrom(mailRequest.getFrom());
        helper.setText(mailRequest.getContent(), true);
        helper.setTo(InternetAddress.parse(mailRequest.getTo()));
        helper.setSubject(mailRequest.getSubject());
        mailSender.send(mimeMessage);
        log.info("[SEND MESSAGE SMTP] End processing message");
    }
}
