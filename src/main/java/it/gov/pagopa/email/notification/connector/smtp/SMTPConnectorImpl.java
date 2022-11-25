package it.gov.pagopa.email.notification.connector.smtp;

import it.gov.pagopa.email.notification.dto.smtp.MailRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
public class SMTPConnectorImpl implements SMTPConnector {

    private final JavaMailSender mailSender;

    public SMTPConnectorImpl(JavaMailSender mailSender) {
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
        helper.setTo(InternetAddress.parse(mailRequest.getTo()));
        helper.setSubject(mailRequest.getSubject());
        mailSender.send(mimeMessage);
        log.trace("sendMessage end");
    }
}
