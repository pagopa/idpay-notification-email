package it.gov.pagopa.email.notification.connector.smtp;

import it.gov.pagopa.email.notification.dto.smtp.MailRequest;

public interface SMTPConnector {

    public void sendMessage(MailRequest mailRequest);

}
