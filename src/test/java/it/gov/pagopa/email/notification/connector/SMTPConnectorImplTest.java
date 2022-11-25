package it.gov.pagopa.email.notification.connector;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import static org.mockito.Mockito.*;

@SpringJUnitConfig
@ImportAutoConfiguration(classes = {SMTPConnectorImpl.class})
class SMTPConnectorImplTest {

    private static final String CONTENT = "CONTENT";
    private static final String RECIPIENT_EMAIL = "RECIPIENT@RECIPIENT.RECIPIENT";
    private static final String SENDER_EMAIL = "SENDER@SENDER.SENDER";
    private static final String SUBJECT = "SUBJECT";

    @MockBean
    private JavaMailSender javaMailSender;

    @Autowired
    private SMTPConnector smtpConnector;

    /**
     * Method under test: {@link SMTPConnectorImpl#sendMessage(MailRequest)}
     */
    @Test
    void testSendMessage() throws MailException {
        doNothing().when(javaMailSender).send((MimeMessage) any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        MailRequest mailRequest = new MailRequest(SENDER_EMAIL, RECIPIENT_EMAIL, SUBJECT, CONTENT);
        smtpConnector.sendMessage(mailRequest);

        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send((MimeMessage) any());
    }
}

