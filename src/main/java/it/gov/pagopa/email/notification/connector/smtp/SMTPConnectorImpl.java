package it.gov.pagopa.email.notification.connector.smtp;

import it.gov.pagopa.email.notification.dto.smtp.MailRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import java.util.Arrays;

@Slf4j
@Service
public class SMTPConnectorImpl implements SMTPConnector {

    @Autowired
    private final MailSender mailSender;

    public SMTPConnectorImpl(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @SneakyThrows
    @Override
    public void sendMessage(MailRequest mailRequest) {
        log.info("[SEND MESSAGE SMTP] Start processing message");
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(mailRequest.getFrom());
        simpleMailMessage.setText(mailRequest.getContent());
        simpleMailMessage.setTo(mailRequest.getTo());
        simpleMailMessage.setSubject(mailRequest.getSubject());
        mailSender.send(simpleMailMessage);
        log.info("[SEND MESSAGE SMTP] End processing message");
    }
}
