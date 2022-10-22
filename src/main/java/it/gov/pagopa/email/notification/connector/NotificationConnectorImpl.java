package it.gov.pagopa.email.notification.connector;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Slf4j
@Service
public class NotificationConnectorImpl implements NotificationConnector {

    private final JavaMailSender mailSender;

    public NotificationConnectorImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @SneakyThrows
    @Override
    public void sendMessage(MailRequest mailRequest) {
        log.trace("sendMessage start");
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false);
        helper.setFrom(mailRequest.getFrom());
        helper.setText(mailRequest.getContent(), true);
        helper.setTo(mailRequest.getTo());
        helper.setSubject(mailRequest.getSubject());
        mailSender.send(mimeMessage);
        log.trace("sendMessage end");
    }
}
